package com.streaming.main

import com.streaming.producer.ProducerApp
import com.streaming.consumer.ConsumerApp
import com.streaming.utils.LoggingUtils
object MainApp extends LoggingUtils{
  def main(args: Array[String]): Unit = {
    if (args.nonEmpty) {
      args.head match {
        case "ProducerApp" => ProducerApp.main(args.tail)
        case "ConsumerApp" => ConsumerApp.main(args.tail)
        case _ => println("Invalid argument")
      }
    } else {
      println("Usage: MainApp [ProducerApp|ConsumerApp] [args...]")
      logger.info("Usage: MainApp [ProducerApp|ConsumerApp] [args...]")
    }
  }
}
