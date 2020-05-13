val rewriteToNewSyntax =
  Seq(
    "-new-syntax",
    "-rewrite"
  )

val rewriteToIndentBasedSyntax =
  Seq(
    "-indent",
    "-rewrite"
  )

val rewriteToNonIndentBasedSyntax =
  Seq(
    "-noindent",
    "-rewrite"
  )


val rewriteToOldSyntax =
  Seq(
    "-old-syntax",
    "-rewrite"
  )

// scalacOptions ++= rewriteToNewSyntax
// scalacOptions ++= rewriteToIndentBasedSyntax
// scalacOptions ++= rewriteToNonIndentBasedSyntax
// scalacOptions ++= rewriteToOldSyntax
  