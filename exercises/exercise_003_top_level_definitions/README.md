# Top Level Definitions

## Background

Scala 3 now has support for `Top Level Definitions`. In short, these replace Scala
2's `package object`s. Definitions such as methods, [given] values, type aliases
can be written at the top level.

The goal of this exercise is to remove package objects from our codebase and
replace it with `Top Level Definitions`.

The following is a typical `package object`

```scala
package foo.bar

package object baz {
  def x(a: Int): Int = {
    a
  }
}
```

This can be written in a toplevel definition as follows

```scala
package foo.bar.baz

def x(a: Int): Int = {
  a
}
```

## Steps

Just a reminder: it's probably a good idea to have the Scala 3 `-source:future-migration`
compiler option enabled permanently in the build definition of our project.
If you haven't done this already, do it now:

- Change the compiler options in `project/CompileOptions.scala` to include
  `-source:future-migration`.

Let's continue with the core topic of this exercise:

- Find any package objects available in the existing project

- Create a new source file inside the same package using any meaningful name

- Copy the contents of the `package object` to the newly created source file

- Remove the package object

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass

- Verify that the application runs correctly

### Next steps

After successfully completing the tasks in this exercise, move to the next one by
running the `cmtc next-exercise` from the command line.

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
