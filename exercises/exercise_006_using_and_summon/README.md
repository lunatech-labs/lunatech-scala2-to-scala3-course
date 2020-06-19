# Contextual abstractions - using and summon

## Background

In this exercise, we will utilize Dotty's `using` and `summon` to replace the
use of _scala 2_'s `implicit`s and `implicitly`.

> In _scala 2_,`implicitly` is used to check if an implicit value of type `T` 
> is available and return it. A more precise variant of it is available in Dotty 
> named `summon`.

## Steps

- Find all occurrences of scala 2 `implicit` keyword. These should all be at the 
  parameter side

- Replace all `implicit` parameters with the new `using` keyword
  - `Tip`: When a context parameter is passed explicitly it must be preceded by
  the `using` keyword

- Keep the `implicit`s at definition side for now, we can change them in  the 
  next exercise

- Replace all occurrences of `implicitly` with `summon`

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass

- Verify that the application runs correctly