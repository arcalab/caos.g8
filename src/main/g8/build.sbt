val caosScalaVersion = "3.0.0-RC1"

lazy val caos = (project in file ("$caos_path$"))
  .enablePlugins(ScalaJSPlugin)
  .settings(scalaVersion := caosScalaVersion)

lazy val `$name;format="norm"$` = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "$name$",
    version      := "0.1.0",
    organization := "$organization$",
    scalaVersion := "$scala_version$",
    scalacOptions ++= Seq("-Ytasty-reader"),
    scalaJSUseMainModuleInitializer := true,
    Compile / mainClass := Some("$package$.Main"),
    Compile / fastLinkJS / scalaJSLinkerOutputDirectory := baseDirectory.value / "$tool_path$" / "js" / "gen"
  ).dependsOn(caos)
