import sbt._

organization := "io.paradoxical"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.4"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-language:postfixOps",
  "-unchecked",
  "-Ywarn-nullary-unit",
  "-Xfatal-warnings",
  "-Xlint",
  "-Xfuture"
)

scalacOptions in console ~= (prevOptions => {
  (prevOptions.toSet -- Set("-Xfatal-warnings", "-Xlint", "-encoding", "UTF-8")).toSeq ++ Seq("-Yrepl-sync")
})

name := "macro-test"

val versions = new  {
  val mockito = "1.10.19"
  val scalatest = "3.0.1"
}

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.github.ghik" %% "silencer-lib" % "0.5",
  compilerPlugin("com.github.ghik" %% "silencer-plugin" % "0.5")
) ++ Seq(
  "org.scalatest" %% "scalatest" % versions.scalatest,
  "org.mockito" % "mockito-all" % versions.mockito
).map(_ % "test")

lazy val showVersion = taskKey[Unit]("Show version")

showVersion := {
  println(version.value)
}

// custom alias to hook in any other custom commands
addCommandAlias("build", "; compile")
