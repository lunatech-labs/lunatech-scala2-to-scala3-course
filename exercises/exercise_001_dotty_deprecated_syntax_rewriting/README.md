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
$ cs complete-dep org.scala-lang:scala3-compiler_3: | grep -v RC
3.0.0
3.0.1
3.0.2
3.1.0
3.1.1
3.1.2
3.1.3
3.2.0
3.2.1
3.2.2
3.3.0
3.3.1
```

Now that you've figured out what the most recent version of the Scala 3 compiler is,
adapt the build to utilise this version (change `code/build.sbt`).

As we need to have a look at the options supported by the Scala 3 compiler, we need to install
the compiler first. Let's use Coursier to do this. For example, if we want to install version
3.3.0 (which certainly isn't the most recent version at this moment...), we do this as follows:

```bash
$ cs install scalac:3.3.0
<elided>
Wrote scalac

$ scalac -version
Scala compiler version 3.3.0 -- Copyright 2002-2023, LAMP/EPFL
```

After having installed the compiler, we can have a look at its options.
Let's first explore what the compiler can help us with when migrating our
Scala 2.13 based application to Scala 3. The compiler has an option `-source`
for which we can specify an additional argument. Here's the abbreviated output
from `scalac -help`. Note that the (reformatted) output you get from this command
may be different if you installed a different version.

```bash
$ scalac --help
Usage: scalac <options> <source files>
where possible standard options include:
     -Dproperty=value  Pass -Dproperty=value directly to the runtime system.
...
<elided>
...
          -new-syntax  Require `then` and `do` in control expressions.
              -indent  Together with -rewrite, remove {...} syntax when possible
                       due to significant indentation.
           -no-indent  Require classical {...} syntax, indentation is not
                       significant.
          -old-syntax  Require `(...)` around conditions.
                       Default 80
             -rewrite  When used in conjunction with a `...-migration` source
                       version, rewrites sources to migrate to new version.
                       (Scala.js only)
...
<elided>
...
              -source  source version
                       Default 3.3
                       Choices : 3.0-migration, 3.0, 3.1, 3.2-migration, 3.2,
                       3.3-migration, 3.3, future-migration, future
...
<elided>
...
              -source  source version
```

We will add some code that triggers a number of compiler warnings which can
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
$ git commit -a -m "Snapshot before Scala 3 compiler syntax rewrites"
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
  compiles successfully.
- Remove the `-rewrite` from `scalacOptions` in the sbt build definition.
- Checkpoint the current state of your code by commiting the changes to git:

```scala
$ git commit -a -m "Snapshot after Scala 3 compiler syntax rewrites"
```

### Next steps

After successfully completing the tasks in this exercise, move to the next one by
running the `cmtc next-exercise` from the command line.

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

## Source code formatting & Markdown viewer in IntelliJ

### Source code formatting

[scalafmt](https://github.com/scalameta/scalafmt) based source code formatting is
in place in this project. scalafmt supports both Scala 2 and Scala 3. You can
[re]format the code by running `scalafmtAll` from the sbt prompt. As we switch from
Scala 2 to Scala 3, you need to make sure that a matching scalafmt configuration is
in place. In any of the exercises, you can run `cmtc pull-template .scalafmt.conf`
to "pull-in" the correct configuration file.

### Markdown viewer in IntelliJ

The font size can be a bit too small for the taste of some people. You can change the
Markdown zoom setting in IntelliJ by pasting the following CSS snippet in the
markdown setting in _" Settings" -> "Languages & Frameworks" -> "Custom CSS -> CSS rules"_
and adjust the font-size setting to your liking:

```
body {
  font-size: 120% !important;
  }
```

![IntelliJ Markdown viewer settings](images/Markdown-viewer-IntelliJ.png)
