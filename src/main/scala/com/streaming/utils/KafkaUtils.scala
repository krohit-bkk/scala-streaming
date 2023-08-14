package com.streaming.utils

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

object KafkaUtils extends LoggingUtils {
  // Set your Kafka broker address and topic name
  var kafkaBrokers : String = null
  var topic : String = null
  var props: Properties = null
  var producer : KafkaProducer[String, String] = null
  var isAvroSupported : Boolean = false

  // LOGIC FOR DECIDING KEYS
  // AsyncProducer = 1, SyncProducer = 2
  // Keys (Avro disabled): Default1, Default2
  // Keys (Avro  enabled): Avro1, Avro2

  // Set the Kafka defaults
  // This should be the first configuration
  def setKafkaConfigs(kafkaBrokers: String, topic: String, avroFlag: String) = {
    this.kafkaBrokers = kafkaBrokers
    this.topic = topic
    this.isAvroSupported = avroFlag.toBoolean
    logger.info(s"SET Kafka broker : ${kafkaBrokers}")
    logger.info(s"SET Kafka topic  : ${topic}")
    logger.info(s"SET AVRO Support : ${avroFlag}")
  }

  // Property object for Kafka - default when not using AVRO
  def setDefaultProperties: Unit = {
    this.props = new Properties()
    props.put("bootstrap.servers", this.kafkaBrokers)
    if (this.isAvroSupported) {
      // AVRO based serializers
      // TBD
      // props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      // props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }
    else{
      props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }
    this.producer = new KafkaProducer[String, String](props)
  }

  // Asynchronous producer method
  def sendAsyncMessage(message: String): Unit = {
    // Initialize props if not already
    if(this.props == null)
      this.setDefaultProperties

    // Key for messages over Kafka topics
    val key = if(this.isAvroSupported) "Avro1" else "Default1"

    val record = new ProducerRecord[String, String](this.topic, key, message)
    this.producer.send(record, (metadata: RecordMetadata, exception: Exception) => {
      if (exception == null)
        logger.info(s"Sent message with offset ${metadata.offset()} to partition ${metadata.partition()}")
      else
        logger.error(s"Failed to send message: ${exception.getMessage}")
    })
  }

  // Synchronous producer method
  def sendSyncMessage(message: String): Unit = {
    // Initialize props if not already
    if (this.props == null)
      this.setDefaultProperties

    // Key for messages over Kafka topics
    val key = if (this.isAvroSupported) "Avro2" else "Default2"

    val record = new ProducerRecord[String, String](this.topic, key, message)
    try {
      val metadata = this.producer.send(record).get()
      logger.info(s"Sent message with offset ${metadata.offset()} to partition ${metadata.partition()}")
    } catch {
      case ex: Exception => logger.error(s"Failed to send message: ${ex.getMessage}")
    }
  }

    // Close producer
  def close(): Unit = {
    this.producer.close()
  }
}