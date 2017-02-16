
organization := "org.homermultitext"
name := "hmt-textmodel"

version := "1.2.0"

scalaVersion := "2.12.1"
crossScalaVersions := Seq("2.11.8", "2.12.1")


resolvers += Resolver.jcenterRepo


libraryDependencies ++= Seq(
  "edu.holycross.shot.cite" %% "xcite" % "1.3.0",
  "edu.holycross.shot" %% "ohco2" % "4.0.0",
  "edu.holycross.shot" %% "orca" % "2.1.0",
  "edu.holycross.shot" %% "greek" % "1.3.1",
  "edu.holycross.shot" %% "gsphone" % "1.0.1",


   "org.scalatest" %% "scalatest" % "3.0.1" %  "test",
   "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
)
