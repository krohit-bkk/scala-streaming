package com.streaming.producer

import com.streaming.main.Main
import com.streaming.utils.{AvroUtils, KafkaUtils}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.ByteString

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, FiniteDuration}

object ProducerApp extends Main{
  // Check arguments
  def isArgumentsValid(args: Array[String]): Boolean = {
    println(s"INFO: Argument summary:")
    for(arg <- args){
      println(s"INFO: \targ: ${arg}")
    }

    if (args == null || args.length != 3) {
      logger.error(s"Invalid number of arguments [${args.length}].")
      logger.error(s"\t[0] - Kafka broker urls with port number")
      logger.error(s"\t[1] - Kafka topic to work with")
      logger.error(s"\t[2] - Flag isAvroSupported (default=False)")
      return false
    }
    true
  }

  // Read CSV using Alpakka (Akka Stream) and push to Kafka topic
  def readAndShootCSV() : Unit = {
    implicit val system: ActorSystem = ActorSystem("AlpakkaCsvReader")
    // val csvFilePath = "/Volumes/WD_SSD/Kumar_Rohit/Learning/CompanyUsecases/HonestBankStreamingUsecase/Data/accepted_2007_to_2018Q4.csv"
    val csvFilePath = "/home/krohit/Documents/f.txt"
    val delay: FiniteDuration = Duration.create(3, "seconds")

    try {
      var lineNumber = 1
      val source: Source[String, _] = FileIO.fromPath(java.nio.file.Paths.get(csvFilePath))
        .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 20000, allowTruncation = true))
        .map(byteString => byteString.utf8String)

      // Define a function that takes line and line number as arguments
      def processLineAndNumber(lineNumber: Int, line: String): Unit = {
        val formattedLine = s"[$lineNumber] >>>> $line"
        println(formattedLine)
        KafkaUtils.sendAsyncMessage(line)
        Thread.sleep(delay.toMillis) // Introduce a delay of 2 seconds
        // Add your more complex operations here
      }

      // Create a sink using the defined function
      val sink = Sink.foreach[String] { line =>
        processLineAndNumber(lineNumber, line)
        lineNumber += 1
      }

      val stream = source.runWith(sink)
      Await.result(stream, Duration.Inf)
    }
    finally {
      // Wait for the stream to complete before terminating the actor system
      system.terminate()
    }
  }

  // Entry point to the application
  override def run(args: Array[String]): Unit = {
    logger.info(s"Starting Producer App...")

    // Check input parameters and set defaults
    // Args(0) - Kafka broker urls with port number (comma separated)
    // Args(1) - Kafka topic
    // Args(2) - Flag to set AVRO support On/Off [True/False], (default=False)
    if (!isArgumentsValid(args))
      System.exit(-1)
    else {
      if(args.length > 2)
        KafkaUtils.setKafkaConfigs(args(0), args(1), args(2))
      else
        KafkaUtils.setKafkaConfigs(args(0), args(1), "False")
    }
    // Read CSV and push to Kafka topic
    readAndShootCSV

    // Close Kafka
    KafkaUtils.close()
    // println(AvroUtils.avroSchemaString(null))
  }
}
