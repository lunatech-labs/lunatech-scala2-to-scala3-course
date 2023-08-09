<!-- .slide: data-background-color="#781010" data-background-image="images/bg-reveal.ps.png" -->

[//]: # (The following is a hack to move the slide H2 section down)
## &#173;
## &#173;
## &#173;
## &#173;
## OPAQUE TYPE ALIASES

---

## ***`Opaque Type Aliases`***: Motivation
## &#173;

* Opaque types were originally proposed in [SIP-35](https://docs.scala-lang.org/sips/opaque-types.html) (cf. the [Motivation section](https://docs.scala-lang.org/sips/opaque-types.html#motivation))
* They aim to *"provide type abstraction without any overhead"*
    * [https://dotty.epfl.ch/docs/reference/other-new-features/opaques.html](https://dotty.epfl.ch/docs/reference/other-new-features/opaques.html)
* Can be thought of as "compile-time wrapper types"
    * Provide type-safety at compile-time
    * But without runtime overhead (e.g. no extra memory allocations)
* To illustrate, let us compare to two features in Scala 2
    * Plain type aliases
    * Value classes

---

## Plain (Transparent) ***`Type Alias`***&#173;*es*
## &#173;

* Typical Scala 2 approach is to use plain type aliases for readability

```scala
  type Kilometres = Double
  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )
```

* But this offers no type safety because the aliased type is transparent

```scala
  type Miles = Double
  class Booster():
    def advanceRocket(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
      // Kilometres and Miles are transparent. They are both Double so this bug is allowed
      rocket.advance(distanceToAdvance)
    }
```

---

## ***`case class`*** wrappers
## &#173;

* Simplest approach for type-safety would be to create distinct new types

```scala
  case class Kilometres(value: Double)
  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      Kilometres(distanceTravelled.value + distanceToAdvance.value)
    )
```

* So now the compiler helps us to prevent the bug...

```scala
  class Booster():
    def advanceRocket(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
      // Kilometres and Miles are different types. So compiler prevents this bug
      rocket.advance(distanceToAdvance)
  |                  ^^^^^^^^^^^^^^^^^
  |                  Found:    (distanceToAdvance : Miles)
  |                  Required: Kilometres
    }
  
```

* ...but at the cost of runtime overhead (allocating wrapper ***`Kilometres`*** and ***`Miles`*** objects) 

---

## Value-class wrappers
## &#173;

* Extending the wrappers with '***`AnyVal`***' promises to eliminate overhead

```scala
  case class Kilometres(value: Double) extends AnyVal
  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      Kilometres(distanceTravelled.value + distanceToAdvance.value)
    )
```

* The compiler still prevents the bug...

```scala
  case class Miles(value: Double) extends AnyVal
  class Booster():
    def advanceRocket(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
      // Kilometres and Miles are different types. So compiler prevents this bug
      rocket.advance(distanceToAdvance)
  |                  ^^^^^^^^^^^^^^^^^
  |                  Found:    (distanceToAdvance : Miles)
  |                  Required: Kilometres
    }
  
``` 
* ...and in theory we eliminate the runtime overhead of allocating wrapper objects


---

## Value-class wrappers limitations
## &#173;

* BUT! Allocations happen in many cases (e.g. parametric polymorphism)

```scala
  case class Kilometres(value: Double) extends AnyVal
  case class Miles(value: Double) extends AnyVal

  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      Kilometres(distanceTravelled.value + distanceToAdvance.value)
    )

  type Conversion[A] = A => Kilometres
  class Booster():
    def advanceRocket[A: Conversion](rocket: Rocket, distanceToAdvance: A): Rocket = {
      val distanceInKm = summon[Conversion[A]](distanceToAdvance)
      rocket.advance(distanceInKm)
    }
```
```scala

  val rocket1 = new Rocket(Kilometres(0))
  val rocket2 = new Rocket(Kilometres(0))
  val booster = new Booster()

  given Conversion[Kilometres] = identity
  given Conversion[Miles] = miles => Kilometres(miles.value * 1.6)

  booster.advanceRocket(rocket1, Kilometres(100)) // Allocation of Kilometres object
  booster.advanceRocket(rocket2, Miles(200))      // Allocation of Miles object
```

---

## Value-class wrappers limitations
## &#173;

* BUT! Allocations happen in many cases (e.g. subtyping)

```scala
  sealed trait Distance extends Any
  case class Kilometres(value: Double) extends AnyVal with Distance
  case class Miles(value: Double) extends AnyVal with Distance

  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      Kilometres(distanceTravelled.value + distanceToAdvance.value)
    )

  class Booster():
    def advanceRocket(rocket: Rocket, distanceToAdvance: Distance): Rocket = {
      val distanceInKm = distanceToAdvance match {
        case miles: Miles => Kilometres(miles.value * 1.6)
        case km: Kilometres => km
      }
      rocket.advance(distanceInKm)
    }
```
```scala
  val rocket1 = new Rocket(Kilometres(0))
  val rocket2 = new Rocket(Kilometres(0))
  val booster = new Booster()

  booster.advanceRocket(rocket1, Kilometres(100)) // Allocation of Kilometres object
  booster.advanceRocket(rocket2, Miles(200))      // Allocation of Miles object
```

---

## Value-class wrappers limitations
## &#173;

* BUT! Allocations happen in many cases (e.g. array assignment)

```scala
  case class Kilometres(value: Double) extends AnyVal
  case class Miles(value: Double) extends AnyVal

  val distances: Array[Kilometres] = Array(Kilometres(10)) // Allocation of Kilometres object
```

* Limitation especially significant for numeric computing
    * *"...There has been concern for numerical computing. We think future SIP(s), using work from SIP-15, can provide more benefit to numerical computing users..."*
    * See: [SIPs: Value Classes](https://docs.scala-lang.org/sips/value-classes.html)
### &#173;
* For other limitations, see: [Value-classes - Limitations](https://docs.scala-lang.org/overviews/core/value-classes.html#limitations)

---

## ***`Opaque Type Aliases`*** - I
## &#173;

* Opaque Type Aliases provide compile-time wrapper types
* Scala 3 introduces the opaque keyword add in front of plain type alias

```scala
  object Scala3OpaqueTypeAliasesDefinitions:
    opaque type Kilometres = Double
    opaque type Miles = Double
```

* Must be members of ***`class`***&#173;*es*, ***`trait`***&#173;*s*, or ***`object`***&#173;*s*, or defined at the top-level. They cannot be defined in local blocks.
* But this by itself is not useful
    * Outside of the scope of ***`object Scala3OpaqueTypeAliasesDefinitions`*** we only know the type names but we cannot do anything
    * At a minimum we need to provide a way to 'introduce' values of our opaque type and a public API for accepting and handling values of opaque type
    * So in practice ***`opaque type aliases`*** would have a companion object

---

## ***`Opaque Type Aliases`*** - II
## &#173;

* So we have opaque types and extension methods that define public API

```scala
object Scala3OpaqueTypeAliasesDefinitions:

  opaque type Kilometres = Double
  object Kilometres:
    def apply(d: Double): Kilometres = d

  opaque type Miles = Double
  object Miles:
    def apply(d: Double): Miles = d

  extension (a: Kilometres)
    @scala.annotation.targetName("plusKm")
    def + (b: Kilometres): Kilometres = a + b
    def toMiles: Miles = a / 1.6

  extension (a: Miles)
    @scala.annotation.targetName("plusMiles")
    def + (b: Miles): Miles = a + b
    def toKm: Kilometres = a * 1.6

```

---

## ***`Opaque Type Aliases`*** - III
## &#173;

* Outside of the scope where the opaque type alias is defined the knowledge of underlying representation is hidden
* So revisiting our ***`Rocket`*** and ***`Booster`*** example, we get type-safety...

```scala
  import Scala3OpaqueTypeAliasesDefinitions._

  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )

  class Booster():
    def advanceRocket(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
      // Kilometres and Miles are different types. So compiler prevents this bug
      rocket.advance(distanceToAdvance)
-- [E007] Type Mismatch Error: -------------------------------------------------
11 |      rocket.advance(distanceToAdvance)
   |                     ^^^^^^^^^^^^^^^^^
   |Found:    (distanceToAdvance : Scala3OpaqueTypeAliasesDefinitions.Miles)
   |Required: Scala3OpaqueTypeAliasesDefinitions.Kilometres
   |
   | longer explanation available when compiling with `-explain`
    }
```

---

## ***`Opaque Type Aliases`*** - IV
## &#173;

* ...but without allocation cost, even in context of parametric polymorphism

```scala
  import Scala3OpaqueTypeAliasesDefinitions.*

  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )

  type Conversion[A] = A => Kilometres
  class Booster():
    def advanceRocket[A: Conversion](rocket: Rocket, distanceToAdvance: A): Rocket = {
      val distanceInKm = summon[Conversion[A]](distanceToAdvance)
      rocket.advance(distanceInKm)
    }

  val rocket1 = new Rocket(Kilometres(0))
  val rocket2 = new Rocket(Kilometres(0))
  val booster = new Booster()

  given Conversion[Kilometres] = identity
  given Conversion[Miles] = _.toKm

  booster.advanceRocket(rocket1, Kilometres(100)) // No allocation of Kilometres object
  booster.advanceRocket(rocket2, Miles(200))      // No allocation of Miles object
```

---

## ***`Opaque Type Aliases`*** - V
## &#173;

* ...and no allocation costs when assigning to arrays

```scala
  import Scala3OpaqueTypeAliasesDefinitions.*

  val distances: Array[Kilometres] = Array(Kilometres(10)) // No allocation of Kilometres object
```

* The wrapper type only exists at compile-time
* At runtime the opaque type is erased to its runtime representation
### &#173;
* NOTE: This means type tests (e.g. when type casing in a pattern match -- ***`case myType: MyType)`*** are done on the underlying representation, ***not*** the ***`opaque type alias`***
* So beware opaque type alias pattern matching!!

---

## ***`Opaque Type Aliases`*** - VI

* Buggy version with pattern matching though...

```scala
  import Scala3OpaqueTypeAliasesDefinitions.*

  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )

  type Distance = Kilometres | Miles
  class Booster():
    // THIS GIVES A WARNING. THE 'Kilometres' CASE IS UNREACHABLE due to erasure.
    // SO WE HAVE A BUG. Any 'Kilometres' passed to this method will be multiplied by 1.6
    def advanceRocket(rocket: Rocket, distanceToAdvance: Distance): Rocket =
      val distanceInKm = distanceToAdvance match {
        case miles: Miles => miles.toKm
        case km: Kilometres => km
[warn] -- [E030] Match case Unreachable Warning: dottyslidescodesnippets/src/main/scala/org/lunatech/dotty/opaquetypes/Units.scala:16:13
[warn] 16   |        case km: Kilometres => km
[warn]      |             ^^^^^^^^^^^^^^
[warn]      |             Unreachable case
[warn] one warning found
      }
      rocket.advance(distanceInKm)
```
```scala
  val rocket1 = new Rocket(Kilometres(0))
  val rocket2 = new Rocket(Kilometres(0))
  val booster = new Booster()

  booster.advanceRocket(rocket1, Kilometres(100)) // BUG! Will actually advance by 160km
  booster.advanceRocket(rocket2, Miles(200))
```

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## OPTIONAL EXERCISE
## Using ***`Opaque Type Aliases`***
## &#173;

* In this exercise, we will explore the mechanism for creating Opaque Type aliases
    * Make sure you're positioned at exercise *"opaque type aliases"*
    * Follow the exercise instructions provided in the README.md file in the code folder