import sbt._

object CompileOptions {

  val rewriteNewSyntax = Seq("-rewrite", "-new-syntax")
  val rewriteIndent = Seq("-rewrite", "-indent")
  val rewriteNoIndent = Seq("-rewrite", "-noindent")
  val rewriteOldSyntax = Seq("-rewrite", "-old-syntax")

  val compileOptions = Seq(
    "-unchecked",
    "-deprecation",
    "-encoding", "UTF-8",
  )
}

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
    Seq.empty[ModuleID]

  val dependencies: Seq[ModuleID] =
    logbackDeps ++
    munitDeps ++
    akkaDeps ++
    akkaTestkitDeps
}
