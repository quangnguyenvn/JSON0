# JSON0

JSON0 is the fastest Java library that converts Java Objects into their JSON format. This library focused on performance and was designed to handle huge converting tasks with minimum time. The development team used algorithms to maximize the reuse and limit the abuse of creating new objects in Java that could consume a lot of memory.

Performance Benchmark using JHM library with the result shown below (Jquill is the previous name for JSON0):

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
| BenchmarkLoop.loopForJquill   	| 10000000  |	avgt	|	8		|24819.857		|	+	|	3235.933	|	ms/op	|

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

## Introduction

In a modern JVM, the cost of creating a Java object may be greatly exaggerated because Java essentially increments a pointer. However, there are numerous other situations in which we should consider the cost of creating objects, such as when we focus on high frequency.
When your application creates many objects in a short period of time, this can lead to:
-	Higher CPU utilization: The GC itself consumes CPU cycles, potentially impacting application performance.
-	Increased GC pauses: The GC needs to stop application threads to reclaim memory, leading to pauses and reduced application responsiveness, which can increase the latency of requests, especially in real-time or low-latency applications.
While object creation in Java is generally efficient, there are some cases where it can be costly, particularly if not managed appropriately. Here are some of the elements that can lead to greater costs:
•	Use fixed size for object.
•	Reduce unnecessary object creation in frequently called methods or loops in arrays.
•	Use Object Pools to reuse objects instead of generating them regularly. This can reduce the overhead associated with object creation and garbage collection.
•	Optimize data structures by utilizing bytes.
The performance of your Java applications can be significantly improved by carefully managing object lifecycles and selecting appropriate data structures. 
JSON0 implements these approaches above for a better performance when comparing with other  and this article will mention more details about these approaches.

## JSON format should be fixed size for designing 

Fixed size is critical for memory management and program speed; it is not merely a fixed number, but also one of the most fundamental principles in software design. Fixed size is significant in numerous situations, especially in programming and data structures, for several reasons. 

• Predictable Memory Allocation: Fixed-size elements ensure effective memory management. This predictability aids in the rapid calculation of memory addresses and the efficient access to data.

• Fixed-size elements perform better for direct memory access. Because their size is constant, the system can quickly determine the memory offset required to access certain pieces, which is very useful in low-level and system programming.

• Implementing arrays requires consistent element sizes. Knowing the size of each element enables the system to efficiently calculate the memory offset required to access array indices.

• Fixed-size elements reduce memory fragmentation and overhead. They ensure that memory is used efficiently and predictably.
The fixed size for the JSON0 is 512 elements.

## Use byte data type

In Java, the byte data type is used for memory efficiency since a byte is an 8-bit signed integer, it can store values from -128 to 127. This makes it useful for saving memory in large arrays where the memory savings can be significant compared to using larger data types like int. Using the byte data type in programming can be beneficial in several scenarios:
It will improve memory efficiency: It is useful when you need to save memory, especially in large arrays were using int (which is 32 bits) would be wasteful.
Bytecode and Serialization: In languages like Java, bytecode is the intermediate representation of your code that the JVM executes. Serialization, which involves converting objects into a byte stream, also relies on bytes. Smaller objects lead to faster serialization and deserialization.
By using the byte type appropriately, you can optimize memory usage and improve the efficiency of your applications.

## Use array effectively to save memory in Java
Furthermore, using arrays effectively can help save memory in Java, especially when dealing with large amounts of data. Here are a few reasons why:
•	Fixed Size: Arrays have a fixed size, which means the memory allocation is done once and remains constant. This can be more efficient than using dynamic data structures like Array List, which may need to resize and copy elements to new arrays as they grow.
•	Primitive Types: When you use arrays of primitive types (like int, byte, char, etc.), they are stored more compactly in memory compared to arrays of objects. For example, an int array uses 4 bytes per element, while an Integer array uses more memory due to object overhead.
•	Contiguous Memory Allocation: Arrays are stored in contiguous memory locations, which can improve cache performance and access speed.
•	Batch Processing: Arrays allow you to process multiple elements in a single operation, reducing the need to create and manage multiple objects. This can be particularly useful in scenarios like image processing, numerical computations, and data analysis.
•	Reduced Garbage Collection Pressure: By using arrays, you can reduce the number of objects created and subsequently the load on the garbage collector. Fewer objects mean less frequent garbage collection cycles, which can improve application performance.

## Implement object pool design pattern with array of bytes

The Object Pool design pattern in Java manages a pool of reusable objects, optimizing memory management and application performance by recycling objects rather than creating and destroying them repeatedly.
In a real-world example, imagine a library with a limited number of study rooms that are frequently in demand. Instead of each student building their own study room whenever they need one, the library manages a pool of available study rooms. When a student needs a study room, they check one out from the pool. After they are done, they return the room back to the pool for others to use. This ensures that the study rooms are efficiently utilized without the need to build new rooms each time, thus saving time and resources, like how the Object Pool pattern manages the reuse of expensive objects in software.
Use the Object Pool pattern when:

•	You need to frequently create and destroy objects, leading to high resource allocation and deallocation costs.
•	The objects are expensive to create and maintain (e.g., database connections, thread pools).
•	A fixed number of objects need to be controlled, like in connection pooling.
•	Object reuse can significantly improve system performance and resource management

Byte is a smallest unit of Java object, tackle on bytes, arrange and make the logic from the level of bytes could improve the performance of the program. By converting all data to bytes then tackling the data.
Using arrays of bytes can significantly improve performance in various scenarios. Here are a few reasons why:
•	Reduced Memory Allocation Overhead: By reusing byte arrays, you minimize the need for frequent memory allocation and deallocation, which can be costly in terms of performance.
•	Improved Cache Utilization: Byte arrays are contiguous blocks of memory, which can be more cache friendly. This means that accessing elements in a byte array can be faster compared to more complex data structures.
•	Lower Garbage Collection Pressure: Frequent creation and destruction of objects can lead to increased garbage collection activity. By reusing byte arrays, you reduce the number of objects that need to be collected, which can improve overall application performance.
•	Efficient Data Processing: Byte arrays are often used for low-level data processing tasks, such as reading from or writing to streams, handling network packets, or manipulating binary data. Their simplicity and direct access to memory make them ideal for these tasks.
In Java, a byte is a primitive data type that can hold an 8-bit signed two’s complement integer, instantiated by using the byte data type before a variable name. It has a minimum value of -128 and a maximum value of 127. 
One of the main advantages of using byte in Java is its small size. A byte takes up only 8 bits of memory, making it a memory-efficient choice when dealing with large amounts of data.
The byte data type in Java plays a crucial role in memory-efficient programming. Its small size (8 bits) makes it an ideal choice when working with large amounts of data. For example, when reading data from a file or a network, using byte allows you to process the data byte by byte, reducing the memory footprint of your program.

## Conclusion

Using Object Pools in Java can significantly enhance performance, especially when dealing with expensive-to-create objects like byte arrays. The byte data type in Java is a powerful tool for developers aiming to optimize the memory footprint of their applications. 
The approaches are perfect for simulations where large data sets are involved and every byte of memory count, understanding and utilizing byte offers an edge in creating efficient, high-performing Java applications. However, adopting byte must be considered in the context of its value range limitations and should be aligned with the specific needs of the application being developed.


## License

JSON0 is released under the Apache 2.0 license.
