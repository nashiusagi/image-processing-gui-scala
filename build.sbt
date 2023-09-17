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
libraryDependencies += "org.scalafx" %% "scalafx" % "19.0.0-R30"

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
    )
  )

addCommandAlias(
  "fix",
  "; all scalafmtSbt scalafmtAll; all Compile/scalafix Test/scalafix; scalafmtAll"
)

addCommandAlias(
  "check",
  "; scalafmtSbtCheck; scalafmtCheckAll; Compile/scalafix --check; Test/scalafix --check"
)
