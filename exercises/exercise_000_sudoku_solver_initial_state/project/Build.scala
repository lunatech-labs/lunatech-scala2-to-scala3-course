import sbt._

object CompileOptions {

  lazy val rewriteNewSyntax = Seq("-rewrite", "-new-syntax")
  lazy val rewriteIndent = Seq("-rewrite", "-indent")
  lazy val rewriteNoIndent = Seq("-rewrite", "-noindent")
  lazy val rewriteOldSyntax = Seq("-rewrite", "-old-syntax")

  lazy val compileOptions = Seq("-unchecked", "-deprecation", "-encoding", "UTF-8")
}

object Versions {
  lazy val akkaVer = "2.6.20"
  lazy val kamonVer = "2.6.3"
  lazy val logbackVer = "1.2.3"
  lazy val mUnitVer = "0.7.26"
}

object Dependencies {

  private lazy val akkaDeps = Seq(
    "com.typesafe.akka" %% "akka-actor-typed",
    "com.typesafe.akka" %% "akka-slf4j",
    "com.typesafe.akka" %% "akka-stream").map(_ % Versions.akkaVer)

  private lazy val akkaTestkitDeps = Seq("com.typesafe.akka" %% "akka-actor-testkit-typed" % Versions.akkaVer % Test)

  private lazy val logbackDeps = Seq("ch.qos.logback" % "logback-classic").map(_ % Versions.logbackVer)

  private lazy val munitDeps = Seq("org.scalameta" %% "munit" % Versions.mUnitVer % Test)

  private lazy val kamonDeps = Seq(
    "io.kamon" %% "kamon-bundle" % Versions.kamonVer,
    "io.kamon" %% "kamon-prometheus" % Versions.kamonVer
  )

  lazy val dependencies: Seq[ModuleID] =
    akkaDeps ++
      akkaTestkitDeps ++
      kamonDeps ++
      logbackDeps ++
      munitDeps
}
