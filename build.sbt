lazy val scala212 = "2.12.10"
lazy val supportedScalaVersions = List(scala212)
ThisBuild / scalaVersion := scala212
ThisBuild / turbo := true

lazy val root = project.in(file(".")).
    aggregate(crossed.js, crossed.jvm).
    settings(
      crossScalaVersions := Nil,
      publish / skip := true
    )


lazy val crossed = crossProject(JSPlatform, JVMPlatform).in(file(".")).
    settings(
      name := "hmt-textmodel",
      organization := "org.homermultitext",
      version := "8.1.0",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),

      resolvers += Resolver.jcenterRepo,
      resolvers += Resolver.bintrayRepo("neelsmith", "maven"),
      resolvers +=  Resolver.bintrayRepo("cibotech", "public"),
      libraryDependencies ++= Seq(
        "org.scalatest" %%% "scalatest" % "3.1.2" % "test",
        "org.wvlet.airframe" %%% "airframe-log" % "20.5.2",

        "edu.holycross.shot.cite" %%% "xcite" % "4.3.0",
        "edu.holycross.shot" %%% "ohco2" % "10.20.0",
        "edu.holycross.shot" %% "dse" % "7.1.3",
        "edu.holycross.shot" %% "scm" % "7.3.0",
        "edu.holycross.shot" %% "citerelations" % "2.7.0",
        "edu.holycross.shot" %% "citevalidator" % "1.2.1",
        "edu.holycross.shot.mid" %% "markupreader" % "1.0.1",
        "edu.holycross.shot.mid" %% "orthography" % "2.0.0",
        "edu.holycross.shot" %% "greek" % "5.5.1",

        // NEED GSPHONE

      )
    ).jvmSettings(
      libraryDependencies ++= Seq(
        "org.scala-js" %% "scalajs-stubs" % "1.0.0" % "provided",
        "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
        "edu.holycross.shot" %% "xmlutils" % "2.0.0",

      )
    ).
    jsSettings(
      // JS-specific settings:
      scalaJSUseMainModuleInitializer := true,
    )

    lazy val docs = project       // new documentation project
      .in(file("docs-build")) // important: it must not be docs/
      .dependsOn(crossed.jvm)
      .enablePlugins(MdocPlugin)
      .settings(
        mdocIn := file("guide"),
        mdocOut := file("docs"),
        mdocExtraArguments := Seq("--no-link-hygiene"),
        mdocVariables := Map(
          "VERSION" -> "8.1.0"
        )
      )
