Global / onChangedBuildSource := ReloadOnSourceChanges

Compile / run / fork := true

inThisBuild(
  Seq(
    version := "1.0",
    scalaVersion := "2.13.8",
    scalafixScalaBinaryVersion := "2.13",
    scalafixDependencies ++= Seq(
      "com.github.liancheng" %% "organize-imports" % "0.5.0",
      "com.github.vovapolu" %% "scaluzzi" % "0.1.18"
    )
  )
)

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"

lazy val root = (project in file("."))
  .settings(
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-Xlint",
      "-Xfatal-warnings"
    ),
  )
