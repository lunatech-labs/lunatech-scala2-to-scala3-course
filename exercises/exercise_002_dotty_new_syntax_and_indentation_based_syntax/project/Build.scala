import sbt._

object CompileOptions {

  lazy val rewriteNewSyntax = Seq("-rewrite", "-new-syntax")
  lazy val rewriteIndent = Seq("-rewrite", "-indent")
  lazy val rewriteNoIndent = Seq("-rewrite", "-noindent")
  lazy val rewriteOldSyntax = Seq("-rewrite", "-old-syntax")

  lazy val compileOptions = Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-source:future-migration")
}

object Versions {
  lazy val PekkoVersion = "1.0.1"
  lazy val logbackVer = "1.2.3"
  lazy val mUnitVer = "0.7.26"
}

object Dependencies {

  private lazy val akkaDeps = Seq(
    "org.apache.pekko" %% "pekko-actor-typed",
    "org.apache.pekko" %% "pekko-slf4j",
    "org.apache.pekko" %% "pekko-stream").map(_ % Versions.PekkoVersion)

  private lazy val akkaTestkitDeps = Seq("org.apache.pekko" %% "pekko-actor-testkit-typed" % Versions.PekkoVersion % Test)

  private lazy val logbackDeps = Seq("ch.qos.logback" % "logback-classic").map(_ % Versions.logbackVer)

  private lazy val munitDeps = Seq("org.scalameta" %% "munit" % Versions.mUnitVer % Test)

  lazy val dependencies: Seq[ModuleID] =
    logbackDeps ++
      munitDeps ++
      akkaDeps ++
      akkaTestkitDeps
}
