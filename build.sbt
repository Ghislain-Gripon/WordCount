ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.0"

lazy val root = (project in file("."))
  .settings(
    name := "WordCount"
  )

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.11",
  "org.scalatest" %% "scalatest" % "3.2.11" % "test"
)
