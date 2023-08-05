package com.streaming.consumer

import com.streaming.main.Main
import com.streaming.utils.{AvroUtils, KafkaUtils}


object ConsumerApp extends Main{
  // Check arguments
  def isArgumentsValid(args: Array[String]) : Boolean = {
    if(args == null || args.length != 2){
      logger.error(s"Invalid number of arguments [${args.length}].")
      logger.error(s"\t[0] - Kafka broker urls with port number")
      logger.error(s"\t[1] - Kafka topic to work with")
      return false
    }
    true
  }

  // Entry point to the application
  override def run(args: Array[String]): Unit = {
    val args = Array("Brokers", "topic")
    logger.info(s"Starting Consumer App...")

    // Check input parameters and set defaults
    // Args(0) - Kafka broker urls with port number (comma separated)
    // Args(1) - Kafka topic
    if(!isArgumentsValid(args))
      System.exit(-1)
    else
      KafkaUtils.setKafkaConfigs(args(0), args(1))

    println(AvroUtils.avroSchemaString(null))

  }
}
