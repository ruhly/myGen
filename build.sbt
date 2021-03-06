name := "myGen"

version := "0.1.0"

scalaVersion := "2.12.1"

scalacOptions += "-target:jvm-1.8"

//val circeVersion = "0.5.3"
//val monixVersion = "2.0.3"

libraryDependencies ++= Seq(
  "org.pegdown" % "pegdown" % "1.6.0",
  "com.lihaoyi" % "scalatags_2.12" % "0.6.3",
  "com.lihaoyi" %% "ammonite-ops" % "0.8.2"
  //"com.lihaoyi" % "ammonite_2.12.1" % "0.8.2"
  //"org.freemarker" % "freemarker" % "2.3.23",
  //"org.slf4j" % "slf4j-api" % "1.7.19",
  //"ch.qos.logback" %  "logback-classic" % "1.1.6",
  //"com.beachape.filemanagement" %% "schwatcher" % "0.3.1",
  //"commons-io" % "commons-io" % "2.4",
  //"commons-cli" % "commons-cli" % "1.3.1",
  //"io.monix" %% "monix" % monixVersion,
  //"io.circe" %% "circe-core" % circeVersion,
  //"io.circe" %% "circe-generic" % circeVersion,
  //"io.circe" %% "circe-parser" % circeVersion,
  //"org.eclipse.jetty" % "jetty-server" % "9.3.11.v20160721",
  //"net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.16"
)

enablePlugins(JavaAppPackaging)
