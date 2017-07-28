name := """/home/frecano/projects/cristal"""

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.7",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.6" % Test,
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.7",
  "org.scalatest" %% "scalatest" % "3.0.3" % "test",
  "org.reactivemongo" % "reactivemongo_2.12" % "0.12.3",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.mockito" % "mockito-core" % "2.8.47" % "test",
  "com.softwaremill.akka-http-session" %% "core" % "0.5.0",
  "com.github.t3hnar" %% "scala-bcrypt" % "3.0"
)
