# Scala 3 - new control and indentation based syntax


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

- Have a look at the `project/Build.scala` file and notice this section at the top:

```scala
  val rewriteNewSyntax = Seq("-rewrite", "-new-syntax")
  val rewriteIndent = Seq("-rewrite", "-indent")
  val rewriteNoIndent = Seq("-rewrite", "-noindent")
  val rewriteOldSyntax = Seq("-rewrite", "-old-syntax")
```

- Before proceeding, let's take a snapshot of the current state of the exercises
  by executing the following commands:

```scala
$ git add -A
$ git commit -m "Snapshot before Scala 3 compiler syntax rewrites"
```

You can now have the compiler rewrite the source code to switch to one of the
alternative syntax options.

- The values mentioned above each contain a specific set of compiler options
  for a specific syntax rewrite.

- Now go through the following sequence of actions:
  - Add one of the syntax rewrite values to the compiler option section.

Question: you will need to concatenate the `Seq` of settings with the compiler
option setting. How would you do that. If needed, consult the `Seq` Scala collection
documentation.

  - When the changes are applied, these will be picked up automatically by sbt because
    the build has been configured as such.
  - From the sbt prompt, run the `clean` command followed by running `compile`.
    You will see that the compiler will _patch_ the source files.
  - Repeat this by compiling the test code (`Test / compile`).
  - Explore the changes applied by the rewrites (you can use the `git diff` command
    for this).
  - Repeat the process for another rewrite.
