# Project: scala-streaming
This is a project which domonstrates end to end streaming application using Kafka, Spark and Redis. 
1. Kafka is the layer to support `pub-sub` and manage streaming messages into Kafka `topic`. This is done by streaming a lartge CSV file record-by-record into Kafka `topic`.
2. The messages (or `events`) streamed over Kafka is consumed via Spark Structured Streaming application. This is going to do two things:

   1. Write the event stream into some sort of database/warehouse (say, PostgreSQL) in a `stateless` manner.

   2. Implement sliding window and compute aggregates `stateful` for both - last n records and last n min
 4. We would also be connecting the Spark Streaming application witn Redis and implement `stateful` pipeline to show current aggregate value and compare it against previous aggregate values. the previous
    aggregate value will be coming in from Redis.

# Project Structure
This is a Scala-SBT project. There are a few things to take care of:
1. `com.streaming.utils.LoggingUtils` - This is a Scala `trait` which initializes the `Logger` (using `SLF4J` library).
   All class `extend`-ing from this `trait` would have the `logger` object, which can be used for logging.
2. `com.streaming.main.Main` - This is an Scala `abstract` class and it has two important methods to note:
   1. `main(args: Array[String])` - the main method, the entry point to the application. It gives a call to the `run` method described below.
   2. `run(args Array[String])` - this is an abstract method which has same `arguments` as passed in `main` method.
      This method needs to be implemented in all the classed extending from `Main` class, thereby, making the extending class runnable (having `main` method indirectly).
3. `com.streaming.producer.ProducerApp` - This is also a Scala class `extend`-ing from the abstract `Main` class described above. Since, it is a subclass of `Main`, it already has `main()` and `run` method.
    But this class must implement `run` method as this class is extending from an abstract class which has an abstract method `run`.
4. `com.streaming.consumer.ConsumerApp` - This is also a Scala class `extend`-ing from the abstract `Main` class described above. Since, it is a subclass of `Main`, it already has `main()` and `run` method.
    But this class must implement `run` method as this class is extending from an abstract class which has an abstract method `run`.
5. `com.streaming.utils` - base package for managing utility classes and functions.

# Building Project
* Extract the project in your working directoy. Make sure you have JDK (1.8 or above) and Scala SDK installed.
* Use `sbt reload` - when building the project for the first time. This will fetch all missing dependencies from global repositories.
* Use `sbt run` - to run the application. If you extract and build the project successfully, you would see a promt on cli like shown below. You can choose the class which you want to run.

![image](https://github.com/krohit-bkk/scala-streaming/assets/137164694/e3be7b0c-42da-4cbb-b3b0-ee12ba145a0b)

**Note**: In case the project build fails, try removing the `.m2` directory from user's home directory and try `sbt reload` once again to fetch all dependencies. Once all dependencies are avaiable, try running `sbt run`

**Upcoming changes**: Changes for creating `fat/uber` jar for the application with SBT assembly modules.
