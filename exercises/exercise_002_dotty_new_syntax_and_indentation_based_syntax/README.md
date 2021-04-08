# Exploring Scala 3's new control structure and significant indentation based syntax


## Background

Scala 3 introduces some new syntax which can be divided in two categories:

- A new control structure syntax
- The possibility to use a significant indentation based syntax as opposed
  to the traditional syntax using curly braces

The Scala 3 compiler is able to rewrite existing source code to a different syntax.
Note that this rewriting is done one step at a time. In other words, rewriting to 
the new significant indentation based syntax -and- to the new control structure syntax
cannot be done in a single step.

Changing the syntax is a reversible process (except that after going back to where
one came from, the formatting may be different, but semantically equivalent).

## Steps

- Run the `pullSolution` command from the `sbt` prompt

- Inspect the `build.sbt` file in the `exercises` folder
  - At the end of this file, you will see the following 4 lines that have
    been commented out:

```scala
   // scalacOptions ++= rewriteToNewSyntax
   // scalacOptions ++= rewriteToIndentBasedSyntax
   // scalacOptions ++= rewriteToNonIndentBasedSyntax
   // scalacOptions ++= rewriteToOldSyntax
```

- Before proceeding, let's take a snapshot of the current state of the exercises
  by executing the following commands:

```scala
$ git add -A
$ git commit -m "Snapshot before Scala 3 compiler syntax rewrites"
```

- The values on the right side of the `++=` operator are defined in the beginning
  of the files and each contains a specific set of compiler options.

- Now go through the following sequence of actions:
  - Uncomment one `scalacOptions ++= ...` line at a time
  - As a consequence of this, the build definition is changed, so don't forget
    to run the `reload` command on the `sbt` prompt
  - From the sbt prompt, run the `clean` command followed by running `compile`.
    You will see that the compiler will _patch_ the source files
  - Explore the changes applied by the rewrites (you can use the `git diff` command
    for this)
