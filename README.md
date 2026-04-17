# JSON0

## Recent Updates

- Fixed parser edge cases:
  - Correctly handles keys containing `:`.
  - Correctly handles braces/brackets inside quoted strings.
- Fixed serializer delimiter handling to avoid invalid output when leading fields are `null`.
- Improved parse rollback safety by restoring full internal state on parse failure.
- Improved charset consistency for key/value serialization and `parse(String)`.
- Fixed Maven Central GitHub Actions command typo in workflow.
- Added regression tests for parser, serializer, and rollback behavior.

## 1. Introduction

```text
      _  ____   ___   _   _    ___
     | |/ ___| / _ \ | \ | |  / _ \
  _  | |\___ \| | | ||  \| | | | | |
 | |_| | ___) | |_| || |\  | | |_| |
  \___/ |____/ \___/ |_| \_|  \___/
                  000000000
```

Recently, we needed to decide the message format for internal communication between modules in our financial software system. JSON got our attention because it has many advantages:

- It's self-descriptive and flexible.
- It's easy to convert from FIX (Financial Information eXchange) format.
- It's ubiquitous and easy for third-party integration.
- It's easy to produce, parse, and verify with many available tools.

In short, JSON seemed to be a good match for our needs. However, JSON is often known for lower performance than binary formats. So before adopting it, we ran performance tests focused on JSON production, which is the main workload on our servers.

## 2. Server Test Setup

Note:
- These tests ran on an ordinary PC: Linux 2.6 kernel, AMD Ryzen 7 1700 Eight-Core Processor.
- JVM option used: `-Xms1500M`.

For producing JSON objects, we first used `json-simple`.

The sample source code is in `JSONSimplePerf.java`.

Test source code and outputs:
- https://github.com/quangnguyenvn/JSON0/tree/main/perf_test/java/server_scripts

## 3. JSONSimple Test Results

We observed a stable result of about 3s for producing 1 million JSON objects.

For our case (around 100K messages/second, often larger and more complex payloads), this could still be a bottleneck.

## 4. Points of Improvement

Quick optimization opportunities from the sample:

- `json-simple` is map-based, so each `put` triggers hash operations.
- Limited support for object reuse can increase allocation and GC pressure.
- No direct object-to-byte[] API in the tested flow (requires extra conversion/copy steps).

## 5. JSON0 Approach

We switched to JSON0 while keeping test cases as close as possible.

- Reuse JSON objects with `reset()`.
- Create children with `createJSON` / `createJSONArray`.
- Serialize directly to bytes with `toBytes`.

Output file:
- `JSON0Output.txt`

## 6. JSON0 Test Results

We observed a stable result of about 1.35s for producing 1 million JSON objects, more than 2x faster than the JSONSimple baseline in this scenario.

## 7. In a Nutshell

JSON0 helps improve JSON production performance through software-level optimizations while keeping the API simple.

## References & Installation

JSON0 is published on Maven Central.

### Add Dependency

```xml
<dependency>
    <groupId>io.github.kwangng</groupId>
    <artifactId>JSON0</artifactId>
    <version>1.0.8</version>
</dependency>
```

### Basic Usage

```java
JSON0 json = new JSON0();

json.reset();
json.put("Town", "Hoi An Town");
json.put("DayLength", 3);
json.put("Vehicles", "Taxi or Bike");

JSON0 visits = json.createJSON("MustVisits");
visits.put("Morning", "Tra Que Vegetable Village");
visits.put("Afternoon", "The Japanese Bridge");
visits.put("Evening", "An Bang Beach");

JSON0 mustTry = json.createJSONArray("MustTries");
JSON0 foods = mustTry.createJSON("Foods");
foods.put("Food1", "Banh Mi, Banh Xeo");
foods.put("Food2", "Com Ga Hoi An");

JSON0 drinks = mustTry.createJSON("Drinks");
drinks.put("Drink1", "Cafe Sua Da");
drinks.put("Drink2", "Mot Hoi An");

Object element = json.get("MustVisits");
String out = json.toString();
```

### Local PC Testing with JMH

JMH library:
- https://github.com/openjdk/jmh

Local computer:

```text
JMH 1.33
Java 16
Maven 4.0
CPU i5-7300
```

Tested library versions:

```text
Gson: 2.11.0
Jackson: 2.13.3
Json-simple: 1.1.1
Flexjson: 3.3
JSON0: 1.0
jmh: 1.33
```

Test source code:
- https://github.com/quangnguyenvn/JSON0/tree/main/perf_test/java/jhm/octeam/com

Result:

| Benchmark | (N) | Mode | Cnt | Score | Error | Units |
| -------- | ------- | ------- | ------- | ------- | ------- | ------- |
| BenchmarkLoop.loopForFlexJson | 10000000 | avgt | 8 | 124901.560 | 16257.584 | ms/op |
| BenchmarkLoop.loopForGson | 10000000 | avgt | 8 | 172903.027 | 9400.753 | ms/op |
| BenchmarkLoop.loopForJSONSimple | 10000000 | avgt | 8 | 133672.380 | 9083.245 | ms/op |
| BenchmarkLoop.loopForJackson | 10000000 | avgt | 8 | 48434.752 | 11094.471 | ms/op |
| BenchmarkLoop.loopForJSON0 | 10000000 | avgt | 8 | 24819.857 | 3235.933 | ms/op |

### License

JSON0 is released under the Apache 2.0 license.
