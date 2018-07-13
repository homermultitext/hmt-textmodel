
organization := "org.homermultitext"
name := "hmt-textmodel"

version := "4.0.0"

crossScalaVersions  := Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions ).value.last


enablePlugins(TutPlugin)

licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")


tutTargetDirectory := file("docs")


libraryDependencies ++= Seq(
  "edu.holycross.shot.cite" %% "xcite" % "3.5.0",
  "edu.holycross.shot" %% "ohco2" % "10.9.0",
  "edu.holycross.shot" %% "orca" % "4.2.0",
  "edu.holycross.shot" %% "greek" % "1.4.0",
  "edu.holycross.shot" %% "gsphone" % "1.1.0",

  "org.scalatest" %% "scalatest" % "3.0.1" %  "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
)
