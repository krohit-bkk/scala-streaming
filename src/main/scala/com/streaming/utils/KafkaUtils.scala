package com.streaming.utils

object KafkaUtils extends LoggingUtils {
  // Set your Kafka broker address and topic name
  var kafkaBrokers : String = null
  var topic : String = null

  // Set the Kafka defaults
  // This should be the first configuration
  def setKafkaConfigs(kafkaBrokers: String, topic: String) = {
    this.kafkaBrokers = kafkaBrokers
    this.topic = topic
    logger.info(s"SET Kafka broker : ${kafkaBrokers}")
    logger.info(s"SET Kafka topic  : ${topic}")
  }

}