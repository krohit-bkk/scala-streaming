ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.10"

lazy val root = (project in file("."))
  .settings(
    name := "scala-app"
  )

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.slf4j" % "slf4j-api" % "1.7.32",
  "ch.qos.logback" % "logback-classic" % "1.2.6",
  "org.apache.spark" %% "spark-core" % "3.3.0",
  "org.apache.spark" %% "spark-sql" % "3.3.0",
  "org.apache.spark" %% "spark-streaming" % "3.3.0",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.3.0",
  "org.apache.kafka" % "kafka-clients" % "3.0.0",
  "redis.clients" % "jedis" % "3.7.0"
)