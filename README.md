# JSON0

JSON0 is the fastest Java library that converts Java Objects into their JSON format. This library focused on performance and was designed to handle huge converting tasks with minimum time. The development team used algorithms to maximize the reuse and limit the abuse of creating new objects in Java that could consume a lot of memory.

Performance Benmark will be shown later.

## Installation

Download the [JSON0 jar file](https://drive.google.com/file/d/1xL_0PCIwQdb1DjLI_GRZ_lrVlvL3Vrv3/view?usp=drive_link).

## Examples

*Build JSON format with Java objects as inputs:*

	JSON json = new JSON();
	json.put("place1","hoi an town");
	json.put("daylength",3);
	JSON nested = new JSON();
	nested.put("schedule", json);

*Get the element in JSON:*

    json.get("daylength");

*Convert to String:*

    json.toString()

## License

JSON0 is released under the Apache 2.0 license.
