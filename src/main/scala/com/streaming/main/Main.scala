package com.streaming.main

import com.streaming.utils.LoggingUtils

abstract class Main extends LoggingUtils {
  // Logger
  // @transient val logger : Logger = LoggerFactory.getLogger(getClass)

  // Abstract method to write executable codes in the subclass
  def run(args: Array[String]): Unit

  def main(args: Array[String]): Unit = {
    logger.info(s"Starting application...")
    run(args)
  }
}
