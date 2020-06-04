# Exploring Dotty's new control structure and indentation based syntax


## Background

Dotty introduces some new syntax which can be divided in two categories:

- A new control structure syntax
- The possibility to use an indentation based syntax as opposed to the traditional
  syntax using curly braces

The Dotty compiler includes a rewrite functionality to rewrite existing source code
to a different syntax.

It should be noted that the indentation based syntax only works with the new control
structure syntax. As a consequence, changes to the syntax goes to a specific sequence:

- Curly braces syntax + old control structure syntax
- Curly braces syntax + new control structure syntax
- Indentation based syntax + new control structure syntax

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

- The values on the right side of the `++=` operator are defined in the beginning
  of the files and each contains a specific set of compiler options.

- Now go through the following sequence of actions:
  - Uncomment one `scalacOptions ++= ...` line at a time
  - As a consequence of this, the build definition is changed, so don't forget
    to run the `reload` command on the `sbt` prompt
  - From the sbt prompt, run the `clean` command followed by running `compile`.
    You will see that the compiler will _patch_ the source files
  - Explore the changes applied by the rewrites

Note: we are using dotty release 0.24.0-RC1. At the time of writing, there
      is an issue with the rewrite to an indentation based syntax. See:
      [Dotty issue 8982](https://github.com/lampepfl/dotty/issues/8982) for more info.
      As a consequence, the code will not compile after the rewrite in the
      second step. From the compilation error, you should be able to figure
      out the issue (consult the dotty reference documentation on this topic
      and look at the examples). If you wish, you can correct all errors and
      run the application. Returning to the curly braces based syntax and the
      old control structure syntax should work as advertised.
