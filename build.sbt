name := """iseptraining"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.25",
  "org.apache.poi" % "poi" % "3.13",
  "org.apache.poi" % "poi-ooxml" % "3.9",
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m"
)
