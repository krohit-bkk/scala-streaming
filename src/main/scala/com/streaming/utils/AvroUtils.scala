package com.streaming.utils

object AvroUtils extends LoggingUtils {
  // AVRO schema representing your data
  def avroSchemaString(headers: String): String = {
    logger.info(s"Processing headers for avro: ${headers}")

    if(headers == null)
      return null

    val columns = headers.split((','))
    var avroSchemaString = """
                             |{
                             |  "type": "record",
                             |  "name": "csvData",
                             |  "fields": ["""
    val n = columns.length
    var i = 0

    while(n > 0 && i < n){
      if(columns(i).trim.length >0) {
        var col_detail = s"""\n|    {"name": "${columns(i).trim}", "type": "string"}"""
        col_detail = if (i < n - 1) col_detail + "," else col_detail
        avroSchemaString = avroSchemaString + col_detail
      }
      i += 1
    }

    avroSchemaString = avroSchemaString +
    """
      |  ]
      |}
    """
    avroSchemaString.stripMargin
  }
}