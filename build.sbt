
lazy val supportedScalaVersions = List("2.11.8", "2.12.4")

lazy val root = project.in(file(".")).
    aggregate(crossedJVM, crossedJS).
    settings(
      crossScalaVersions := Nil,
      publish / skip := true
    )

lazy val crossed = crossProject.in(file(".")).
    settings(
          name := "hmt-textmodel",
          organization := "org.homermultitext",
          version := "6.0.1",
          licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),

          resolvers += Resolver.jcenterRepo,
          resolvers += Resolver.bintrayRepo("neelsmith", "maven"),
          libraryDependencies ++= Seq(
              "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
              "org.scalatest" %%% "scalatest" % "3.0.5" % "test",

              "edu.holycross.shot.cite" %%% "xcite" % "4.0.2",
              "edu.holycross.shot" %%% "ohco2" % "10.12.5",
              "edu.holycross.shot" %%% "greek" % "2.1.1",
              "edu.holycross.shot" %%% "gsphone" % "1.4.0",
              "edu.holycross.shot" %%% "midvalidator" % "6.0.2",
          )
        ).
        jvmSettings(
          tutTargetDirectory := file("docs"),
          tutSourceDirectory := file("tut"),
          libraryDependencies ++= Seq(
            "edu.holycross.shot" %% "xmlutils" % "2.0.0",
            "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
          ),

          crossScalaVersions := supportedScalaVersions
        ).
        jsSettings(
          skip in packageJSDependencies := false,
          scalaJSUseMainModuleInitializer in Compile := true,
          crossScalaVersions := supportedScalaVersions
        )

/*


libraryDependencies ++= Seq(
  "edu.holycross.shot.cite" %% "xcite" % "4.0.2",
  "edu.holycross.shot" %% "ohco2" % "10.12.5",
  "edu.holycross.shot" %% "greek" % "2.1.1",
  "edu.holycross.shot" %% "gsphone" % "1.4.0",
  "edu.holycross.shot" %% "midvalidator" % "6.0.2",




)*/


lazy val crossedJVM = crossed.jvm.enablePlugins(TutPlugin)
lazy val crossedJS = crossed.js.enablePlugins(ScalaJSPlugin)
