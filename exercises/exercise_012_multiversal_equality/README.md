# Multiversal Equality

## Background

Scala 2.x uses _universal equality_, meaning that any two values of any two
types can be compared with each other using `==` and `!=`. However, this can
sometimes lead to buggy programs where we assume that we are comparing two
values of the same type but in fact the types are different. In this case,
because of universal equality, the compiler validates the buggy code but at
runtime the comparison will _always_ return the same result regardless of the two
values being compared — it will always return `false` in the case of `==`,  and
always `true` in the case of `!=`. This is almost certainly not the desired
outcome!

This kind of bug often occurs after a refactoring that modifies the types in a
program — e.g. like the modification we have just finished to convert a plain
type alias to an opaque type alias.

Scala 3 introduces _multiversal equality_ to help deal with this problem. We can
explicitly "opt in" to the new, safer multiversal equality, either with an
`import` in the relevant source files or with the addition of a compiler option.
Once enabled, multiversal equality checks for the presence of an `Eql`
("equality") typeclass instance that allows the comparison of the types of the
two values being compared with `==` or `!=`. If no valid `Eql` instance is
found, then the comparison does not type-check and the compilation fails with an
error.

## Steps
- Create a new file `exercises/build.sbt`

- Add the code `scalacOptions in Compile += "-language:strictEquality"` to opt
  in to safer multiversal equality

- Use the course notes on Multiversal Equality and your experience from the
  exercise on `given`s to make the code compile with the flag
  `-language:strictEquality` enabled
    - Hint: The simplest solution will probably involve using `Eql.derived`

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass