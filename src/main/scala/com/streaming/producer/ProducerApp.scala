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
    // Print args details...
    printArgs(args)

    if (args == null || args.length < 4) {
      logger.error(s"Invalid number of arguments [${args.length}].")
      logger.error(s"\t[0] - Kafka broker urls with port number")
      logger.error(s"\t[1] - Kafka topic to work with")
      logger.error(s"\t[2] - Flag isAvroSupported (default=False)")
      logger.error(s"\t[3] - Path of CSV to be streamed")
      return false
    }
    true
  }

  // Read CSV using Alpakka (Akka Stream) and push to Kafka topic
  def readAndShootCSV(defaultPath: String) : Unit = {
    implicit val system: ActorSystem = ActorSystem("AlpakkaCsvReader")
    val csvFilePath = if(defaultPath == null) "/home/krohit/Documents/f.txt" else defaultPath
    val delay: FiniteDuration = Duration.create(3, "seconds")

    try {
      val source: Source[String, _] = FileIO.fromPath(java.nio.file.Paths.get(csvFilePath))
        .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 20000, allowTruncation = true))
        .map(byteString => byteString.utf8String)

      // Create a sink using the defined function
      val sink = Sink.foreach[String] { line =>
        KafkaUtils.sendAsyncMessage(line)
        Thread.sleep(delay.toMillis)
      }

      val stream = source.runWith(sink)
      Await.result(stream, Duration.Inf)
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
        logger.error(e.getStackTrace.toString)
      }
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
    readAndShootCSV(defaultPath = args(3))

    // Close Kafka
    KafkaUtils.close()
    // println(AvroUtils.avroSchemaString(null))
  }
}
