# Jquill

Jquill is a Java library that converts Java Objects into their JSON format. This library focused on performance and was designed to handle huge converting tasks with minimum time. The development team used algorithms maximizing the reuse and limiting the abuse of creating new objects in Java that could consume a lot of memory.

Jquill is inspired by the features of swan quills, the best quill in the world, light, strong and elegant.

## Installation

Download the [Jquill jar file](https://drive.google.com/file/d/1xL_0PCIwQdb1DjLI_GRZ_lrVlvL3Vrv3/view?usp=drive_link).

## Examples

*build json format with Java objects as inputs:*

	JSON json = new JSON();
	json.put("place1","hoi an town");
	json.put("daylength",3);
	JSON nested = new JSON();
	nested.put("schedule", json);

*get the element in json:*

    json.get("daylength");

*convert to String:*

    json.toString()

## License

Jquill is released under the Apache 2.0 license.
