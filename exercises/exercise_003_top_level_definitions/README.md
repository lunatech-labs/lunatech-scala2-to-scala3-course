# Top Level Definitions

## Background

Dotty now has support for `Top Level Definitions`. In short, these replace Scala
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

- Find any package objects available in the existing project

- Create a new source file inside the same package using any meaningful name

- Copy the contents of the `package object` to the newly created source file

- Remove the package object

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass

- Verify that the application runs correctly