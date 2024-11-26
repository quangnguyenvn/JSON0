# JSON0

JSON0 is the fastest Java library that converts Java Objects into their JSON format. This library focused on performance and was designed to handle huge converting tasks with minimum time. The development team used algorithms to maximize the reuse and limit the abuse of creating new objects in Java that could consume a lot of memory.

Performance Benchmark using JHM library with the result shown below:

JHM library:
https://github.com/openjdk/jmh

Local computer:
|JMH 1.33|
|Java 16|
|Maven 4.0|
|CPU i5-7300|

Result:
| Benchmark    						| (N) 		|  Mode  	| Cnt 		| Score 		| 		| Error	 		|	 Units 	|
| -------- 							| ------- 	|  -------  |-------   	|------- 		|-------|------- 		|------- 	|
| BenchmarkLoop.loopForFlexJson		| 10000000 	|	avgt	|	8		|124901.560		|	+	|	16257.584	|	ms/op	|
| BenchmarkLoop.loopForGson 		| 10000000  |	avgt	|	8		|172903.027		|	+	|	9400.753	|	ms/op	|
| BenchmarkLoop.loopForJSONSimple   | 10000000  |	avgt	|	8		|133672.380		|	+	|	9083.245	|	ms/op	|
| BenchmarkLoop.loopForJackson   	| 10000000  |	avgt	|	8		|48434.752		|	+	|	11094.471	|	ms/op	|
| BenchmarkLoop.loopForJSON0  	| 10000000  |	avgt	|	8		|24819.857		|	+	|	3235.933	|	ms/op	|

## Installation

Since there are some complicated issues when deploying on Maven Central from the early of 2024. Then this library will be deployed on Github Packages. The configuration process included two steps.

### Step 1

Firstly, get your github token. Navigate to <strong>Your Github Account</strong> -> <strong>Settings</strong> -> <strong> <> Developer settings </strong> -> <strong> Personal access tokens </strong> -> <strong>Tokens (classic)</strong> -> <strong>Generate new token (classic)</strong> -> tick on `read:packages` and click `Generate token`. Then keep this github token.

Secondly, update file settings.xml in the folder .m2, the folder .m2 should be in the similar path: C:\Users\YourUserName\.m2\settings.xml, then add following information:

```
  <servers>
    <server>
      <id>github</id>
      <username>your github username</username>
      <password>your github token</password>
    </server>
  </servers>

```

### Step 2
 *configure on pom.xml:*

```
	<repositories>
		<repository>
			<id>github</id>
			<name>maven-JSON0</name>
			<url>https://maven.pkg.github.com/quangnguyenvn/JSON0</url>
		</repository>
	</repositories>
	<dependencies>
		<dependency>
			<groupId>snailteam.core</groupId>
			<artifactId>json0</artifactId>
			<version>0.0.2-SNAPSHOT</version>
		</dependency>
	</dependencies>

```
*Build JSON format with Java objects as inputs:*

	JSON0 json = new JSON0();
	json.put("place1","hoi an town");
	json.put("daylength",3);
	JSON0 nested = new JSON0();
	nested.put("schedule", json);

*Get the element in JSON:*

    json.get("daylength");

*Convert to String:*

    json.toString()

## License

JSON0 is released under the Apache 2.0 license.

