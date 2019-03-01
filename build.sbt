ThisBuild / crossScalaVersions  := List("2.11.8", "2.12.4")

name := "hmt-textmodel"
organization := "org.homermultitext"


version := "5.0.0"

//scalaVersion := (crossScalaVersions ).value.last




licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")


libraryDependencies ++= Seq(
  "edu.holycross.shot.cite" %% "xcite" % "3.7.0",
  "edu.holycross.shot" %% "ohco2" % "10.11.2",
  "edu.holycross.shot" %% "orca" % "4.3.0",

  "edu.holycross.shot" %% "greek" % "2.1.0",
  "edu.holycross.shot" %% "gsphone" % "1.3.0",
  "edu.holycross.shot" %% "midvalidator" % "6.0.0",

  "org.scalatest" %% "scalatest" % "3.0.1" %  "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
)

tutSourceDirectory := file("tut")
tutTargetDirectory := file("docs")
enablePlugins(TutPlugin)
