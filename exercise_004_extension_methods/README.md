# Extension Methods

## Background

Extension methods can be used to add methods to types after they are defined.

Lets say we need to add a method `square` to type `Int`. We used to achieve it 
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

With dotty `extension methods`, we can rewrite the above as

```scala
def (i: Int).square: Int = i * i

3.square
// val res0: Int = 9
```

More than one extension methods can be wrapped inside an `Extension Instance`.
```scala
extension {
  def (x: Int).square : Int = x * x
  def (x: Int).isEven: Boolean = x % 2 == 0
}
```

`Collective Extensions` can be used if all your extension methods share the 
same left-hand parameter type.

```scala
extension on (x: Int){
  def square : Int = x * x
  def isEven: Boolean = x % 2 == 0
}
```

## Steps

There are quite a few instances of Extension Methods in this project. A good 
starting point is to take a look at the `TopLevelDefinitions` and `ReductionRules`
which we have created in one of the previous exercises.

- Identify all extension methods defined using the _Scala 2 way_
- Replace them with `Dotty` extension methods
- Wrap one or more occurances of such methods in an `extension instance`
- If you find you need to add more than one extension methods to a same type,
  wrap them inside a `collective extension`
- Verify that the application compiles and run correctly.

