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

Download the [JSON0 jar file](https://drive.google.com/file/d/1SxBHkMswdQY70T9e8Dz0TzNWJKKHV-dY/view?usp=sharing).

## Examples
*import the library:*

	import snailteam.core.json.JSON0

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

