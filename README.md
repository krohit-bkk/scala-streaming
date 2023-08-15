## Project: scala-streaming
This is a project which domonstrates end to end streaming application using Kafka, Spark and Redis. 
1. Kafka is the layer to support `pub-sub` and manage streaming messages into Kafka `topic`. This is done by streaming a lartge CSV file record-by-record into Kafka `topic`.
2. The messages (or `events`) streamed over Kafka is consumed via Spark Structured Streaming application. This is going to do two things:

   1. Write the event stream into some sort of database/warehouse (say, PostgreSQL) in a `stateless` manner.

   2. Implement sliding window and compute aggregates `stateful` for both - last n records and last n min
 4. We would also be connecting the Spark Streaming application witn Redis and implement `stateful` pipeline to show current aggregate value and compare it against previous aggregate values. the previous
    aggregate value will be coming in from Redis.

## Project Structure
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
5. `com.streaming.main.MainApp` - the main class registered in `build.sbt` as the main runnable class. There is a switch-case in this class and the first argument in the command line is used to determine which class to be executed. The next parameters should be the ones as needed by the class that we want execute. The advantage of this implementation is you can have n number of main classes having their own variation in implementation, yet all of them can be registered with `MainApp` class and they could be executed with single command pattern. Snippet of implementation from `MainApp` class is provided below: 
   ```scala
   if (args.nonEmpty) {
     args.head match {
       case "ProducerApp" => ProducerApp.main(args.tail)
       case "ConsumerApp" => ConsumerApp.main(args.tail)
       case _ => println("Invalid argument")
     }
   } else {
     println("Usage: MainApp [ProducerApp|ConsumerApp] [args...]")
   }
   ```
      For example, use this command to run the `ProducerApp` class of the application in the target environment:
   ```shell
   java -jar /path/to/scala-app-uber-com.streaming.main.Main.jar ProducerApp "param1" "param2" ...
   ```
6. `com.streaming.utils` - base package for managing utility classes and functions.


## Building and Executing Project
### Building the application in IDE ###
* Extract the project in your working directoy. Make sure you have JDK (1.8 or above) and Scala SDK installed.
* Use `sbt reload` - when building the project for the first time. This will fetch all missing dependencies from global repositories.
* Use `sbt run` - to run the application (in IDE). If you extract and build the project successfully, you would see a promt on cli like shown below. You can choose the class which you want to run.

   ![image](https://github.com/krohit-bkk/scala-streaming/assets/137164694/e3be7b0c-42da-4cbb-b3b0-ee12ba145a0b)
  
  **Note**: In case the project build fails, try removing the `.m2` directory from user's home directory and try `sbt reload` once again to fetch all dependencies. Once all dependencies are avaiable, try running `sbt run`
* Use `sbt clean assembly` - to clean the target folder and create the `fat`/`uber` jar file as specified in the `build.sbt` file. Default file name for jar file is given by the below line in `build.sbt` file:
```sbt
val mainClassName = "com.streaming.main.MainApp"
assemblyJarName := s"scala-app-uber-${mainClassName}.jar"
``` 

### Executing the application in target environment ###
There are two ways to execute the application in the target environment:
   * Execute one of the modules of the application using `java -jar` command uses `MainApp` class by default:
     ```shell
     # Execute the Producer Application
     java -jar /path/to/scala-app-uber-com.streaming.main.MainApp.jar ProducerApp "param1" "param2" ...
     # Execute the Consumer Application
     java -jar /path/to/scala-app-uber-com.streaming.main.MainApp.jar ConsumerApp "param1" "param2" ...
     ```
   * Execute one of the modules of the application (directly) using `java -cp` command:
     ```shell
     # Execute the Producer Application
     java -cp /path/to/scala-app-uber-com.streaming.main.MainApp.jar com.streaming.producer.ProducerApp "param1" "param2" ...
     # Execute the Consumer Application
     java -cp /path/to/scala-app-uber-com.streaming.main.MainApp.jar com.streaming.consumer.ConsumerApp "param1" "param2" ...
     ```


## Upcoming changes ##
* Spark Streaming Consumer
* Changes for enabling schema evolution by providing AVRO support at Producer and Consumer side.
