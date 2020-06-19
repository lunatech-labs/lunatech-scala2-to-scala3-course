# Contextual abstractions - givens

## Background

In this exercise, we will utilise Dotty's `given`s. The goal is to replace
all occurrences of Scala 2's `implicit` in places where an *implicit value*
is defined.

Important to note is that Dotty allows us to define a `given` instance without
having to name it. This is pretty cool as it allows us to not have to come up
with a name for a `given`, first of all because naming stuff can be hard and
secondly because these names don't really have a useful application.

> Note: In the previous exercise, we have eliminated all occurrences of the
Scala 2 `implicit` and `implicitly` keywords. Note that the code compiled and ran
successfully even though `implicit` is still present in our code on the
definition side. In all likelihood, this will be possible too in the initial
release of Scala 3.

## Steps

- Find all remaining occurrences of the Scala 2 `implicit` keyword. These
  should all be at the definition side

- Change all definitions to utilise the new `given` keyword. Use the
  appropriate form of the definitions so that the `given`s don't have a name
  defined by yourself

- Check that the modified code compiles without errors

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass

- Verify that the application runs correctly
