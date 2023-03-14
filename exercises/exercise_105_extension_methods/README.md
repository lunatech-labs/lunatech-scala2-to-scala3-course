# Extension Methods

## Background

Extension methods can be used to add methods to types after they are defined.

Let's say we need to add a method `square` to type `Int`. We used to achieve it 
with the help of `implicit class`es in _Scala 2_

```scala
implicit class RichInt(val i: Int) extends AnyVal {
  def square = i * i
}
```

Now you can use it as follows

```scala
4.square
// res1: Int = 16
```

With Scala 3's `extension methods`, we can rewrite the above as

```scala
extension (i: Int)
  def square: Int = i * i

4.square
// val res0: Int = 16
```

Multiple extension methods on the same type can be defined just as easily. For example:

```scala
extension (i: Int)
  def square: Int = i * i
  def isEven: Boolean = i % 2 == 0
```

> Note: extension methods can be generic, i.e., at the base level, type parameters, used
> in the type of the extended object or the individual extension methods are allowed.
> Also, individual extension methods can have additional type parameters.

## Steps - part I

You should look for extension methods defined with the Scala 2 syntax. How would
you approach this. As part of the exercise, you will define a few new extension
methods. In fact, for this exercise, the tests have been modified assuming these
new extension methods are already present. Let's start with adapting the existing
extension methods. Wait with running the tests until you start tackling the second
part of this exercise (adding the new extension methods).

- Identify all extension methods defined using the _Scala 2 way_.

- Replace them with Scala 3 extension methods.

- Wrap one or more occurrences of such method in an `extension instance`.

- If you find you need to add more than one extension method to a same type,
  wrap them inside a `collective extension`.

## Steps - part II

- Run the provided tests by executing the `test` command from the `sbt` prompt.
  You will see that the test code, which has been modified specifically for this
  exercise, doesn't compile. Figure out what's wrong (or rather,
  what's missing; remember we're doing an exercise on extension methods).
  Fix the problem (and don't change the test code).
  
> Tip: consider creating new extension methods (names as suggested by the tests)
  at the `org.lunatechlabs.dotty.sudoku` package level.
  
- Run the tests and make adjustments to your code until they pass.

- Verify that the application runs correctly.

