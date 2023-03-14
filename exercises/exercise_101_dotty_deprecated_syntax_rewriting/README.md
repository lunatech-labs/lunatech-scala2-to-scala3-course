#  Rewriting deprecated Scala syntax

## Background

In this exercise, we will play with the Scala 3 compiler's capability to report 
and rewrite occurrences of some of the Scala 2 language features that are deprecated
in Scala 2.13. In fact, this is a feature that also applies to syntax that gets
deprecated between different Scala 3 releases.

In order to do this, we first need to update the build definition to switch to the most recent
version of Scala 3.

## Steps

We will start by updating the sbt build definition to use the most recent version of Scala 3.
Figuring out what this version is can be done in multiple ways. The first way is to look it
up on the [scala-lang](https://www.scala-lang.org) website. As an alternative, we can use
[`cs`](https://github.com/coursier/coursier) (Coursier) to look up the information. Here's how to do it with `cs`:

```bash
$ cs complete-dep org.scala-lang:scala3-compiler_3:
3.0.0
3.0.1-RC1
3.0.1-RC1-bin-20210413-f3c1468-NIGHTLY
3.0.1-RC1-bin-20210415-59ea58e-NIGHTLY
3.0.1-RC1-bin-20210416-8086e4b-NIGHTLY
.
.
.
```

Now that you've figured out what the most recent version of the Scala 3 compiler is,
adapt the build to utilise this version (change `code/build.sbt`).

As we need to have a look at the options supported by the Scala 3 compiler, we need to install
the compiler first. Let's use Coursier to do this. For example, if we want to install version
3.0.2 (which certainly isn't the most recent version at this moment...), we do this as follows:

```bash
$ cs install scalac:3.0.2
Wrote scalac

$ scalac -version
Scala compiler version 3.0.2 -- Copyright 2002-2021, LAMP/EPFL
```

After having installed the compiler, we can have a look at its options.
Let's first explore what the compiler can help us with when migrating our
Scala 2.13 based application to Scala 3. The compiler has an option `-source`
for which we can specify an additional argument. Here's the abbreviated output
from `scalac -help`. Note that the (reformatted) output you get from this command
may be different if you installed a different version.

```bash
$ scalac -help
Usage: scalac <options> <source files>
where possible standard options include:
     -Dproperty=value  Pass -Dproperty=value directly to the runtime system.
             -J<flag>  Pass <flag> directly to the runtime system.
                   -P  Pass an option to a plugin, e.g. -P:<plugin>:<opt>
                   -V  Print a synopsis of verbose options.
                   -W  Print a synopsis of warning options.
...
   <elided>
...
-new-syntax        Require `then` and `do` in control expressions.
-old-syntax        Require `(...)` around conditions.
...
   <elided>
...
-indent            Together with -rewrite, remove {...} syntax when possible
                   due to significant indentation.
-no-indent         Require classical {...} syntax, indentation is not
                   significant.
...
   <elided>
...
-rewrite           When used in conjunction with a `...-migration` source
                   version, rewrites sources to migrate to new version.
...
   <elided>
...
-source            source version
                   Default 3.2
                   Choices 3.0-migration, 3.0, 3.1, 3.2-migration, 3.2,
                   future-migration, future
```

We will add some code that trigger a number of compiler warnings which can
be corrected by the compiler's code rewriting capabilities.

Add the following code snippet to the `src/main/scala/org/lunatechlabs/dotty/sudoku/SudokuSolver.scala` source code file:

```scala
  private def checkHaha(s: String) {
    val haha = 'Haha
    val noHaha = 'NoHaha
    if (s startsWith haha.name) println(haha.name) else println(noHaha.name)
  }
```

> NOTE: The course repository you're using at the moment is a git repository.
>      This will be helpful to see the changes that the compiler applies
>      when re-writing source files

- Let's start by taking a snapshot in git of the current state of the exercise
  source code. Do this by executing the following commands in the exercises
  root folder:

```scala
$ git add -A
$ git commit -m "Snapshot before Scala 3 compiler rewrite"
```

- Compile and investigate what the compiler reports.

- Let the compiler correct the problem by adding two compiler options to the
  one's that are already in place in the `project/Build.scala` build file:
  -  `-rewrite` compiler option which will automatically rewrite the source code,
     if possible.
  - `-source:3.0-migration` 

```scala
scalacOptions ++=
  Seq(
    .
    .
    .
    "-source:3.0-migration",
    "-rewrite",
  )
```

- Compile the code again and watch the magic... (at the `sbt` prompt, run `compile` and `Test/compile`.

> NOTE:  The easiest way to see what the compiler changed is to run the `git diff` command.

- After completing this step, repeat the process by setting `-source` to `3.2-migration`.

We can take this process a step further by changing the compiler
`-source` compiler option to `-source:future-migration`.
This will apply some syntax changes that are already scheduled for a
a Scala release after the one you're currently using. One example is the change of
wildcard import syntax from using an asterix (`*`) instead of an underscore (`_`).

- Try this out for yourself and check what is reported and what changes.
- Add the "-rewrite" option to have the compiler apply all the reported
  changes.
- The end result should be that, after the compiler has applied its rewrites, the source code
  compiles clean.

> NOTE: The extra bit of code that was added via `cmtc pull-template ...` can either be left as-is
>  or be removed. Your choice.

And finally, **a tip**. You may perform a migration from Scala 2 to Scala 3 in
different steps.
After using the compiler's help via a given combination of `-rewrite` and
`source:xxx-migration`,
you may want to leave the latter enabled permanently in your Scala build, but
take the first one out.
By doing so, your attention will be drawn to code modifications that create issues (warning or errors)
explicitly instead of them being corrected without you noticing them.
