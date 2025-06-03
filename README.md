# JSON0

## 1. Introduction

Recently, we needed to make a decision on the messages format of internal communication between different modules in our financial software system. JSON format got our attention as it has many advantages:

-	It’s self-describe and flexible. It could represent any business logic in a clear, transparent, readable way, avoiding us of spending hours and hours to debug lest we choose some other format (binary one especially).
-	It’s easy to convert from FIX (Financial Information eXchange) format (the format of messages come from outside to our server) to JSON format.
-	It’s ubiquitous. Any third parties who need to integrate their software to our system could plug in using our communication method, get the messages and process it right away, saving us from the need of going through hundreds pages of manuals.
-	It’s easy to producing, parsing, verifying JSON format with many online tools. Again, great for third parties integration as we could independently verify messages and know which side has done something wrong.

In short, JSON seem to be a perfect match to our need. Unfortunately, everything has its down side. JSON format is also well-known for its low performance in producing and consuming compared to others. So before using it, we need to run some simple test scenarios to see if it matches our requirement. We focus on producing side as it would be the main task of our servers.

## 2. Server Test Setup

Note: The tests in this article run on an old, ordinary PC running Linux 2.6 kernel. CPU AMD Ryzen 7 1700 Eight-Core Processor. Therefore you probably will get much better results on a nowadays modern server
-	JVM in these tests start with option –Xms1500M

For producing JSON objects, we used json-simple library.

The source code is in JSONSimplePerf.java. It’s pretty straight-forward.

ProduceUsingJSONSimple method produces JSON objects using json-simple library, convert them to wire format. Note that we change them slightly between iterator and put them in global memory to prevent compiler optimizations. Parameter size specifies the number of objects will be produced

In execute method, we run produceUsingJSONSimple with size about 1 million continuously a number of time (0 mean infinite) and print out the elapse time in between. Note that the first run with size only 1000 to warm up purpose.

The output is in the file JSONSimpleOutput.txt

Source code of testings and outputs can be found in the link belows:

https://github.com/quangnguyenvn/JSON0/tree/main/perf_test/java/server_scripts


## 3.	JSONSimple test results

We see a pretty stable number of about 3s for producing 1 million JSON objects which is not bad at all. This performance should be enough for almost all real-life purposes.

However, In our case we need to send a lot of messages in this format (about 100K/s) so event at producing speed 3us/object rate could be problematic, not to mention our messages tend to be bigger and more complex than in this sample.

## 4. Points of improvement

A quick look at the sample code gives the following idea of optimization
-	Json-simple library is Map based, which mean every time you put in an object, a hash function is called upon. It’s a waste in case we only need to produce JSON objects but not get anything out them (a very typical case as we often produce or consume messages, not both).
-	The library offer very limit capabilities in reusing JSON objects. While it’s very fast to creating object in modern JVM compared to the earlier versions, it’s still not zero. Furthermore, creating many objects will ultimately trigger GC, something real-time applications don’t like at all.
-	There is no supported method to directly convert from JSON object to bytes (wire format) but we need first convert it to String, then using String getBytes method, which again require an extra operation of memory allocation and memory copy.

## 5. JSON0 approaches

We change the library to use JSON0 but keep the above test cases (or keep it as close as possible). The test will produce the same JSON, run the same number of time etc.
Note that we reuse the json object using reset method. And json childs are create using createJSON, createJSONArray methods instead of creating a new one. The library also supports toBytes method to copy the content to a bytes array.
The run output is in the file JSON0Output.txt

## 6. JSON0 test results
We see a stable number of about 1.35s for producing 1 million JSON objects, which is more than double the performance of json-simple library.

## 7. In a nutshell
While it’s rare nowadays, if problem is one of your main issue, using JSON0 could give it a boost. The library is straight-forward and easy to use.


## References & Installation

JSON0 is already deployed on Maven Central Repositories.

### Add the dependency below in your pom.xml
```
<dependency>
    	<groupId>io.github.kwangng</groupId>
    	<artifactId>JSON0</artifactId>
    	<version>1.0.8</version>
</dependency>


Build JSON format with Java objects as inputs:

  JSON0 json = new JSON0();

  json.reset();
  json.put("Town",  "Hoi An Town");
  json.put("DayLength", 3);
  json.put("Vehicles", "Taxi or Bike");

  JSON0 visits = json.createJSON("MustVisits");
  visits.put("Morning", "Tra Que Vengetable Village");
  visits.put("Afternoon", "The Japanese Bridge");
  visits.put("Evening", "An Bang Beach");

  JSON0 mustTry = json.createJSONArray("MustTries");
  JSON0 foods = mustTry.createJSON("Foods");
  foods.put("Food1", "Banh Mi, Banh Xeo");
  foods.put("Food2", "Com Ga Hoi An");

  JSON0 drinks = mustTry.createJSON("Drinks");
  drinks.put("Drink1", "Cafe Sua Da");
  drinks.put("Drink2", "Mot Hoi An");

Get the element in JSON:

  json.get("MustVisits");

Convert to String:

  json.toString()

```

### Local PC Testing with JMH

Performance Benchmark using JHM library with the result shown below:

JHM library:
https://github.com/openjdk/jmh

Local computer:
|JMH 1.33|
|Java 16|
|Maven 4.0|
|CPU i5-7300|

Test source codes:
https://github.com/quangnguyenvn/JSON0/tree/main/perf_test/java/jhm/octeam/com

Result:
| Benchmark    						| (N) 		|  Mode  	| Cnt 		| Score 		| 		| Error	 		|	 Units 	|
| -------- 							| ------- 	|  -------  |-------   	|------- 		|-------|------- 		|------- 	|
| BenchmarkLoop.loopForFlexJson		| 10000000 	|	avgt	|	8		|124901.560		|	+	|	16257.584	|	ms/op	|
| BenchmarkLoop.loopForGson 		| 10000000  |	avgt	|	8		|172903.027		|	+	|	9400.753	|	ms/op	|
| BenchmarkLoop.loopForJSONSimple   | 10000000  |	avgt	|	8		|133672.380		|	+	|	9083.245	|	ms/op	|
| BenchmarkLoop.loopForJackson   	| 10000000  |	avgt	|	8		|48434.752		|	+	|	11094.471	|	ms/op	|
| BenchmarkLoop.loopForJSON0  	| 10000000  |	avgt	|	8		|24819.857		|	+	|	3235.933	|	ms/op	|

### License
JSON0 is released under the Apache 2.0 license.

