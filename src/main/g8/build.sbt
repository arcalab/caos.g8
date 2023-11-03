val scala3Version = "3.3.1"

lazy val caos = project.in(file("lib/caos"))
  .enablePlugins(ScalaJSPlugin)
  .settings(scalaVersion := scala3Version)

lazy val $name;format="camel"$ = project.in(file("."))
   .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "$name$",
    version := "0.1.0",
    scalaVersion := scala3Version,
    scalaJSUseMainModuleInitializer := true,
    Compile / mainClass := Some("$name;format="camel"$.frontend.Main"),
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory := baseDirectory.value / "lib" / "caos"/ "tool" / "js" / "gen",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-parse" % "0.3.4",  // parser combinators
      "org.scalameta" %% "munit" % "0.7.29" % Test // unit tests
    )
  )
  .dependsOn(caos)