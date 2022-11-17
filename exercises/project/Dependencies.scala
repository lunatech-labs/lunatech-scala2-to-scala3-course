import sbt._

object Version {
  val akkaVer           = "2.6.19"
  val logbackVer        = "1.2.3"
  val mUnitVer          = "0.7.26"
  val scalaVersion      = "3.1.2"
}

object Dependencies {

  private val akkaDeps = Seq(
    "com.typesafe.akka"             %% "akka-actor-typed",
    "com.typesafe.akka"             %% "akka-slf4j",
    "com.typesafe.akka"             %% "akka-stream",
  ).map (_ % Version.akkaVer)

  private val akkaTestkitDeps = Seq(
    "com.typesafe.akka"             %% "akka-actor-testkit-typed" % Version.akkaVer % Test
  )

  private val logbackDeps = Seq (
    "ch.qos.logback"                 %  "logback-classic",
  ).map (_ % Version.logbackVer)

  private val munitDeps = Seq(
    "org.scalameta" %% "munit" % Version.mUnitVer % Test
  )

  val crossDependencies: Seq[ModuleID] =
    akkaDeps ++
    akkaTestkitDeps

  val dependencies: Seq[ModuleID] =
    logbackDeps ++
    munitDeps
}
