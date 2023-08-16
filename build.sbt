import sbt.Keys._
import sbtassembly.AssemblyPlugin.autoImport._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "scala-app"
  )

val AkkaVersion = "2.8.1"

// Define your main class name here
val mainClassName = "com.streaming.main.MainApp"

assemblyJarName := s"scala-app-uber-${mainClassName}.jar"
assemblyOption := (assemblyOption in assembly).value.copy(
  prependShellScript = Some(
    Seq(
      "#!/bin/sh",
      """exec scala "$0" "$@""",
      "!#",
      ""
    )
  )
)

assemblyMergeStrategy in assembly := {
  case "reference.conf" => MergeStrategy.concat
  case PathList("org", "slf4j", xs @ _*) => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

//dependencyOverrides += "org.slf4j" % "slf4j-api" % "1.7.32"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.32",
  "ch.qos.logback" % "logback-classic" % "1.2.6",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "6.0.2" % "provided",
  "org.apache.kafka" % "kafka-clients" % "3.0.0"
)

mainClass in assembly := Some(mainClassName)
