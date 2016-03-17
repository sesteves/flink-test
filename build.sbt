name := "flink-test"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.flink" % "flink-streaming-scala" % "0.10.1-hadoop1",
  "org.apache.flink" % "flink-clients" % "0.10.1-hadoop1"
)
