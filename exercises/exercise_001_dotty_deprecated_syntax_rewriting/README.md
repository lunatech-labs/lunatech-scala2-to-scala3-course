# Automatic rewriting  by the Dotty compiler of deprecated Scala 2 syntax 

## Background

In this exercise, we will play with `dotc`'s capability to report and rewrite
occurrences of some of the Scala 2 language features that are deprecated in
Scala 2.13

## Steps

Let's first explore what the compiler can help us with when migrating our
Scala 2.13 based application to Dotty. The compiler has a compile option `-source`
for which we can specify an additional argument. Here's the abbreviated output
from `dotc -help`

```bash
$ dotc -help
Usage: dotc <options> <source files>
where possible standard options include:
-P                 Pass an option to a plugin, e.g. -P:<plugin>:<opt>
-X                 Print a synopsis of advanced options.
-Y                 Print a synopsis of private options.
...
   <elided>
...
-rewrite           When used in conjunction with a `...-migration` source version, rewrites sources to migrate to new version.
...
   <elided>
...
-source            source version
                   Default: 3.0.
                   Choices: 3.0, 3.1, 3.0-migration, 3.1-migration.
```

We have introduced some deprecated Scala 2 syntactic constructions on purpose so
that you can see the rewriting at work. Let's get started!

- Execute the following command from the `sbt` prompt:

```scala
pullTemplate scala/org/lunatechlabs/dotty/sudoku/SudokuSolver.scala
```

> NOTE: The course repository you're using at the moment is a git repository.
>      This will be helpfull to see the changes that the compiler applies
>      when re-writing source files

- Let's start by taking a snapshot in git of the current state of the exercise
  source code. Do this by executing the following commands in the exercises
  root folder:

```scala
$ git add -A
$ git commit -m "Snapshot before Dotty compiler rewrite"
```

- Create a new `build.sbt` file in this exercise's base folder
- In that file, add the following lines:

```scala
scalacOptions ++=
  Seq(
    "-source:3.0-migration",
  )
```

> Note: When changing the build definition by adding the `build.sbt` and
  subsequently editing its content, don't forget to reload the build
  definition in `sbt` (by issuing the `reload` command)

- Compile and investigate what the compiler reports

- Let the compiler correct the problem by adding the `-rewrite` compiler option

```scala
scalacOptions ++=
  Seq(
    "-source:3.0-migration",
    "-rewrite"
  )
```

- Compile the code again and watch the magic...

> NOTE:  The easiest way to see what the compiler changed is to run the `git diff` command

We can repeat this process by changing the compiler `-source` compiler option
to `-source:3.1-migration`

- Try this out for your self and check what is reported and what changes
