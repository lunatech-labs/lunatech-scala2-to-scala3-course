<!-- .slide: data-background-color="#781010" data-background-image="images/bg-reveal.ps.png" -->

[//]: # (The following is a hack to move the slide H2 section down)
## &#173;
## &#173;
## &#173;
## &#173;
## CONTEXTUAL ABSTRACTIONS - II
## `given`, `using` and `summon`

---

## Scala 3's Contextual Abstractions
### ***`using`*** and ***`given`***


* In the chapters on ***`extension method`***&#173;s, we mentioned abstraction over context 
* via ***`implicit`***&#173;s in Scala 2
* via ***given*** and ***`using`*** in Scala 3
* ***`given`*** instances define a *"canonical"* value of a certain type that can be used for synthesising arguments for context parameters (***`using`*** clauses)
* The introduction of the new keywords given and using allow us to make a clear distinction between the <ins>definition</ins> of *"canonical"* values and the actual <ins>use</ins> of them
* This eliminates the confusion that was caused by the multiple distinctive use cases of the Scala 2 ***`implicit`*** keyword
* When moving from Scala 2, we have the benefit to move to Scala 3's *Contextual Abstractions* in steps

---

## ***`using`*** Clauses and ***`given`*** Instances

* Let's start with a trivial example to illustrate the concept
* We start with a few definitions for the example:

```scala
final case class EngineConfig(initialSpeed: Int, maxSpeed: Int, label: String)
final case class Engine(name: String)
```

* In Scala 2, we can encode a method with a [single] implicit parameter list:

```scala
def startEngine(engine: Engine)(implicit config: EngineConfig): Unit =
    println(s"Starting $engine with $config”)
```

* And use it:

```scala
implicit val cfg: EngineConfig = EngineConfig(10, 50, “base-config")

startEngine(Engine(“AC-35-B/002"))
```

* Let's see how that is encoded in Scala 3

---

## ***`using`*** Clauses and ***`given`*** Instances

* Using the same definitions:

```scala
final case class EngineConfig(initialSpeed: Int, maxSpeed: Int, label: String)
final case class Engine(name: String)
```

* We encode the ***`startEngine`*** method

```scala
 def startEngine(engine: Engine)(using config: EngineConfig): Unit =
   println(s"Starting $engine with $config")
```

* And use it:

```scala
given cfg: EngineConfig = EngineConfig(10, 50, "base-config")

startEngine(Engine("AC-35-B/002"))
```

* Pretty straightforward!
* Let's now look at the details

---

## Anonymous ***`given`*** instances

* The name of a ***`given`*** can be left out. The definitions from the previous slide can also be expressed as:

```scala
 given EngineConfig = EngineConfig(10, 50, "base-config")
 ```

* If the name of a ***`given`*** is missing, the compiler will synthesise a name from the implemented type(s). The above instance will become:

```scala
 lazy val given_EngineConfig: EngineConfig
```

* The synthesised type names use the following rules:
* The prefix ***given_***
* The simple name(s) of the implemented type(s)
* The simple name(s) of the toplevel argument type constructors to these types

---

## ***`using`*** in detail
## &#173;

* First of all, some terminology
    * In the previously used definition:

```scala
def startEngine(engine: Engine)(using config: EngineConfig): Unit =
   println(s"Starting $engine with $config")
```

* We have:
    * A [single] regular parameter list with a single parameter
    * A [single] ***`using`*** clause with a single context parameter
* Scala 3 allows for more than one using clause. Let's have a look at this

---

## Multiple ***`using`*** clauses
## &#173;

* ***`recordAndMonitor`*** is a method that has 2 ***`using`*** clauses and 2 regular parameter lists:

```scala
 final case class RecordDev(recordType: String)
 final case class PlayerDev(playerType: String)

 def recordAndMonitor(recordGain: Int)
                     (using recordDev: RecordDev)
                     (volume: Int)
                     (using player: PlayerDev) =
   println(s"Recording with gain $recordGain from $recordDev to $player with volume $volume")
```

* ***`recordAndMonitor`*** can be invoked in different ways:

```scala
 given PlayerDev = PlayerDev("Hifi chain")
 given RecordDev = RecordDev("Blue Yeti")

 recordAndMonitor(8)(20)
 recordAndMonitor(9)(using RecordDev("Built-in mic"))(5)
 recordAndMonitor(19)(80)(using PlayerDev("Second chain"))
 recordAndMonitor(15)(using RecordDev("Screen mic"))(70)(using PlayerDev("Car radio"))
```

---

## Context Bounds - I

* A *context bound* describes an ***implicit value***. 
* It is used to represent a constraint that an ***implicit value*** of a particular type class exists
* given the following Type class:

```scala
trait Ord[T]:
  def compare(x: T, y: T): Int
  extension (x: T)
    def > (y: T) = compare(x, y) > 0
```

* we can define

```scala
def max[T: Ord](x: T, y: T): T = // where max requires a context parameter Ord[T]
    if (x > y) x else y
```

* It would expand to

```scala
  def max[T](x: T, y: T)(using _: Ordering[T]): T
```

* when we invoke ***`max`***:

```scala
scala> max(2, 3)
1 |max(2, 3)
  |         ^
  |No given instance of type Ord[Int] was found for an implicit parameter of method max
```

---

## Context Bounds - II

* Let's try to solve this error by making use of a ***`given`*** definition:

```scala
given intOrd: Ord[Int] with
  def compare(x: Int, y: Int): Int =
    x - y
```

* and we try again:

```scala
scala> max(2, 3)
val res1: Int = 3
```

* And we can be even more generic

```scala
final case class Toto[T](n: T)

given [T](using Ord[T]): Ord[Toto[T]] with
  def compare(t1: Toto[T], t2: Toto[T]): Int =
    summon[Ord[T]].compare(t1.n, t2.n)
```

* and use it:

```scala
scala> max(Toto(12), Toto(5))
val res3: Toto[Int] = Toto(12)

scala> Toto(6) > Toto(5)
val res4: Boolean = true
```

---

## Context Bounds - III

* If you want to explicitly pass an instance of ***`Ord[Int]`***, do it as follows

```scala
scala> max(1,3)(using intOrd)
val res5: Int = 3
```

* Context bounds in Scala 3 maps to old-style implicit parameters in Scala 3.0 to ease the migration.
* From Scala 3.1 onwards, they will map to context clauses instead.
* It means, the following will still be allowed in Scala 3.0, but generate a warning in later versions

```scala
[warn] -- Migration Warning: Test.scala:29:5
[warn] 29 |  max(1,3)(intOrd)
[warn]    |  ^^^^^^^^
[warn]    |Context bounds will map to context parameters.
[warn]    |A `using` clause is needed to pass explicit arguments to them.
[warn]    |This code can be rewritten automatically under -rewrite -source future-migration.
```

---

## Summoning instances - I

* The method ***`summon`*** can be used to retrieve the ***`given`*** of a specific type.
* It replaces the ***`implicitly`*** method in Scala 2.
* Let's take a look at an example

```scala
 sealed trait Engine
 final case class CarEngine(cylinder: Int) extends Engine
 final case class TruckEngine(cylinder: Int) extends Engine

 trait Starter[T]:
   def start(e: T): Unit

 given Starter[CarEngine] with
   override def start(engine: CarEngine): Unit = 
     println(s"Starting CarEngine with ${engine.cylinder} cylinder(s)")

 given Starter[TruckEngine] with
   override def start(engine: TruckEngine): Unit = 
     println(s"Starting TruckEngine with ${engine.cylinder} cylinder(s)")
```

```scala
 def startEngine[E <: Engine: Starter](engine: E): Unit =
   val starter = summon[Starter[E]]
   starter.start(engine)
```

---

## Summoning instances - II

* We invoke the method ***`startEngine`*** and see what is returned by ***`summon[Starter[E]]`***

```scala
scala> startEngine(CarEngine(6))
Starting CarEngine with 6 cylinder(s)

scala> startEngine(TruckEngine(8))
Starting TruckEngine with 8 cylinder(s)
```

---

## **`given`** imports

* A special form of wildcard selector can be used to import given instances.

```scala
 object A:
   class TC
   given tc: TC = new TC
   def f(using TC) = ???

 object B:
   import A.*
   import A.{given}
```

* The import ***`A.*`*** clause inside ***`object B`*** will import all members of ***`A`*** except the given instance.
* ***`import A.{given}`*** will import only the given instances
* The two import clauses can be merged into one as follows:

```scala
object B
   import A.{given, *}
```

* A specific ***`given`*** can be imported by its name. eg.: ***`import A.tc`***
* Givens can be anonymous and importing them by name is not always practical. Hence, wildcard imports are used.

---

## **`given`** imports by Type

* A more specific alternative to wildcard import is available: *by-type imports*

```scala
 import A.{given TC}
```

* This imports any ***`given`*** in ***`A`*** that has a type which conforms to ***`TC`***
* Importing givens of several types ***`T1`***, ... ,***`Tn`*** is expressed by multiple given selectors

```scala
 import A.{given T1, ..., given Tn}
```

* Importing all ***`given`*** instances of a parameterised type is expressed by wildcard arguments

```scala
 object Instances:
   trait Monoid[T]
   trait Ord[T]

   given intOrd: Ord[Int] = ???
   given listOrd[T: Ord]: Ord[List[T]] = ???
   given im: Monoid[Int] = ???
```

```scala
 import Instances.{given Ord[?]}
```

* Which will import ***`intOrd`***, ***`listOrd`***, but not ***`im`***

---

## Summary

* In this chapter we have learned
    * Various Scala 3 constructs that will replace ***`implicit`*** and ***`implicitly`***
    * ***`given`*** *instances* - they replace implicit definitions
    * ***`using`*** *clauses* - new syntax for implicit parameters and their arguments
    * Anonymous ***`given`*** instances
    * Multiple ***`using`*** clauses
    * Context bounds
    * ***`summon`***&#173;*ing* instances
    * ***`given`*** imports

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## Contextual abstractions - ***`using`*** and ***`summon`***
## &#173;

* In this exercise we will use using and summon to remove the use of implicit and implicitly
    * Make sure you're positioned at exercise *"using and summon"*
    * Follow the exercise instructions provided in the README.md file in the code folder

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## Contextual abstractions - ***`given`***
## &#173;

* In this exercise we will use given to replace implicit definitions
    * Make sure you're positioned at exercise *"givens"*
    * Follow the exercise instructions provided in the README.md file in the code folder
