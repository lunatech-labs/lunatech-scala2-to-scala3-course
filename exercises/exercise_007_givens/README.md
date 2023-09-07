# Contextual abstractions - givens

## Background

In this exercise, we will utilise Scala 3's `given`s. The goal is to replace
all occurrences of Scala 2's `implicit` in places where an *implicit value*
is defined.

Important to note is that Scala 3 allows us to define a `given` instance without
having to name it. This is pretty cool as it allows us to not have to come up
with a name for a `given`, first of all because naming stuff can be hard and
secondly because these names don't really have a useful application in most cases.

> Note: In the previous exercise, we have eliminated all occurrences of the
>Scala 2 `implicit` and `implicitly` keywords. Note that the code compiled
> and ran successfully even though `implicit` is still present in our code
> on the definition side.

## Steps

- Find all remaining occurrences of the Scala 2 `implicit` keyword. These
  should all be at the definition side.

- Change all definitions to utilise the new `given` keyword. Use the
  appropriate form of the definitions so that the `given`s don't have a name
  defined by yourself. Also, the definitions can be further streamlined
  using the `with` syntax.

- Check that the modified code compiles without errors.

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass.

- Verify that the application runs correctly.

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
