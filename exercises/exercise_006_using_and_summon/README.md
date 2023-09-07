# Contextual abstractions - using and summon

## Background

In this exercise, we will utilize Scala 3's `using` and `summon` to replace the
use of _scala 2_'s `implicit`s and `implicitly`.

> In _scala 2_,`implicitly` is used to check if an implicit value of type `T` 
> is available and return it. A more precise variant of it is available in Scala 3 
> named `summon`.

## Steps

- Find all occurrences of the scala 2 `implicit` keyword. You should look
  for implicit parameter lists.

- Replace all `implicit` parameters with the new `using` keyword

> `Tip`: When a context parameter is passed explicitly it must be preceded by
>  the `using` keyword

- Keep the `implicit`s at definition side for now, we can change them in the 
  next exercise

- Replace all occurrences of `implicitly` with `summon`

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
