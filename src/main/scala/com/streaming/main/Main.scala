package com.streaming.main

import com.streaming.utils.LoggingUtils

abstract class Main extends LoggingUtils {
  // Logger
  // @transient val logger : Logger = LoggerFactory.getLogger(getClass)

  // Abstract method to write executable codes in the subclass
  def run(args: Array[String]): Unit

  // Print argument details
  def printArgs(args: Array[String]) = {
    println(s"INFO: Argument summary:")
    logger.info(s"INFO: Argument summary:")
    for (arg <- args) {
      println(s"INFO: \targ: ${arg}")
      logger.info(s"INFO: \targ: ${arg}")
    }
  }
  def main(args: Array[String]): Unit = {
    logger.info(s"Starting application...")
    run(args)
  }
}
