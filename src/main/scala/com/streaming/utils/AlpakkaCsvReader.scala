package com.streaming.utils

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

object AlpakkaCsvReader {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("AlpakkaCsvReader")

    val csvFilePath = "/Volumes/WD_SSD/Kumar_Rohit/Learning/CompanyUsecases/HonestBankStreamingUsecase/Data/accepted_2007_to_2018Q4.csv" // Replace with your CSV file path
    val delay: FiniteDuration = Duration.create(2, "seconds")

    try{
      var lineNumber = 1
      val source: Source[String, _] = FileIO.fromPath(java.nio.file.Paths.get(csvFilePath))
        .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 20000, allowTruncation = true))
        .map(byteString => byteString.utf8String)

      // Define a function that takes line and line number as arguments
      def processLineAndNumber(lineNumber: Int, line: String): Unit = {
        val formattedLine = s"[$lineNumber] >>>> $line"
        println(formattedLine)
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
}