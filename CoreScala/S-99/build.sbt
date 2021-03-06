import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "org.gb.sample.scala",
      scalaVersion := "2.12.11",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "S-99",
    libraryDependencies += scalaTest % Test
  )
