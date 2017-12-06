name := "play-swagger-reactivemongo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

val reactiveMongoVer = "0.12.6-play26"

libraryDependencies ++= Seq(
  guice,
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",
  "com.typesafe.play"       % "play-json-joda_2.12" % "2.6.0-RC1",
  "org.reactivemongo"      %% "play2-reactivemongo" % reactiveMongoVer,
  "io.swagger"             %% "swagger-play2"       % "1.6.0",
  "org.webjars"            %  "swagger-ui"          % "3.2.2",
  "org.scalatestplus.play" %% "scalatestplus-play"  % "3.1.1" % Test
)

import play.sbt.routes.RoutesKeys

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"