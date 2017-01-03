
organization := "org.homermultitext"
name := "HmtEdition"

version := "0.2.1"

scalaVersion := "2.11.7"


resolvers += "uh-nexus" at "http://beta.hpcc.uh.edu/nexus/content/groups/public"

//import io.github.cite_architecture.cite._


libraryDependencies += "edu.holycross.shot" %% "cite" % "1.3.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" %  "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.2"

publishTo := Some("Sonatype Snapshots Nexus" at "http://beta.hpcc.uh.edu/nexus/content/repositories/releases/")

credentials += Credentials(Path.userHome / "nexusauth.txt" )
