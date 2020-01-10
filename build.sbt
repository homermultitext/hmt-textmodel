ThisBuild / crossScalaVersions  := List("2.12.4") //List("2.11.8", "2.12.4")

name := "hmt-textmodel"
organization := "org.homermultitext"


version := "5.2.1"




licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

resolvers +=  Resolver.bintrayRepo("cibotech", "public")

libraryDependencies ++= Seq(
  "edu.holycross.shot.cite" %% "xcite" % "4.2.0",
  "edu.holycross.shot" %% "ohco2" % "10.18.1",

  "edu.holycross.shot" %% "greek" % "2.4.0",
  "edu.holycross.shot" %% "gsphone" % "1.4.3",
  "edu.holycross.shot" %% "midvalidator" % "10.0.0",


  "edu.holycross.shot" %% "xmlutils" % "2.0.0",

  "org.scalatest" %% "scalatest" % "3.0.1" %  "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",


  "org.wvlet.airframe" %% "airframe-log" % "19.8.10"
  //"com.cibo" %% "evilplot" % "0.6.3"
)

tutSourceDirectory := file("tut")
tutTargetDirectory := file("docs")
enablePlugins(TutPlugin)
