<!-- .slide: data-background-color="#781010" data-background-image="images/bg-reveal.ps.png" -->

[//]: # (The following is a hack to move the slide H2 section down)
## &#173;
## &#173;
## &#173;
## &#173;
## ENUMERATIONS and `export`

---

## Scala 3 Enumerations with ***`enum`***
## &#173;

* Scala 2 has ***`Enumerations`***
    * Their implementation is awkward for even simple use-cases such as defining a finite number of user-defined elements
#### &#173;
* Scala 3 adds the new and versatile ***`enum`*** construct which is syntactic sugar
#### &#173;
* The design of ***`enum`*** meets the following objectives:
    * Allow for a concise expression and efficient definition of enumerations
    * Allow the modelling of Java enumerations as Scala enumerations
    * Allow for a concise expression of ADTs
    * Support of all idioms expressible with case classes
#### &#173;
* Let's have a short look at each of them

---

## ***`enum`*** - Encoding values - I
## &#173;

* Encoding of a series of values is simple:

```scala
enum Color:
  case Red, Green, Blue
```

* And using them

```scala
val red = Color.Red
println(s"""Ordinal of Red = ${red.ordinal}""")

// Get all values in enumeration
val enums: Array[Color] = Color.values
println(s"Values in enumeration Color: ${enums.mkString("Array(", ", ", ")")}")

// Access a value by its name
val blue = Color.valueOf("Blue")
println(s"""Ordinal of Blue = ${blue.ordinal}""")
```

* Which will print:

```scala
Ordinal of Red = 0
Values in enumeration Color: Array(Red, Green, Blue)
Ordinal of Blue = 2
```

---

## ***`enum`*** - Encoding values - II
## &#173;

* ***`enum`***&#173;*s* can be parametrised

```scala
  enum Color(val rgb: Int):
    case Red   extends Color(0xFF0000)
    case Green extends Color(0x00FF00)
    case Blue  extends Color(0x0000FF)
```

---

## ***`enum`*** - Encoding values - III
## &#173;

* Definitions can be added to an enum

```scala
enum Planet(mass: Double, radius: Double):
  private final val G = 6.67300E-11
  def surfaceGravity = G * mass / (radius * radius)
  def surfaceWeight(otherMass: Double) =  otherMass * surfaceGravity

  case Mercury extends Planet(3.303e+23, 2.4397e6)
  case Venus   extends Planet(4.869e+24, 6.0518e6)
  case Earth   extends Planet(5.976e+24, 6.37814e6)
  case Mars    extends Planet(6.421e+23, 3.3972e6)
  case Jupiter extends Planet(1.9e+27,   7.1492e7)
  case Saturn  extends Planet(5.688e+26, 6.0268e7)
  case Uranus  extends Planet(8.686e+25, 2.5559e7)
  case Neptune extends Planet(1.024e+26, 2.4746e7)
```

* Defining an explicit companion object for an enum

```scala
object Planets:
  import Planet.*
  val earthWeight = 1.0
  val mass = earthWeight / Earth.surfaceGravity
  val weightOnPlanets =
    for p <- values yield (p, p.surfaceWeight(mass))
```

---

## Modelling of Java enumerations as Scala enumerations
## &#173;

* Achieved by extending ***`java.lang.Enum`***

```scala
scala> enum Color extends java.lang.Enum[Color]:
         case Red, Green, Blue
// defined class Color

scala> Color.Green.compareTo(Color.Green)
val res0: Int = 0

scala> Color.Green.compareTo(Color.Blue)
val res1: Int = -1
```

---

## Representing ADTs using ***`enum`***
## &#173;

```scala
scala> enum MyOption[+T] {
         case MySome(x: T)
         case MyNone
       }
// defined class MyOption

scala> import MyOption._

scala> val oInt = MySome(12)
val oInt: MyOption[Int] = MySome(12)

scala> val oNone = MyNone
val oNone: MyOption[Nothing] = MyNone

scala> val oInt: MyOption[Nothing] = MyNone
val oInt: MyOption[Int] = MyNone
```

* Note that ***`MyNone`*** is inferred as ***`MyOption[Nothing]`***
* As any ***`enum`***, ADTs can have fields

---

## Application of ***`enum`*** for protocol definitions
## &#173;

* Instead of using the Scala 2 way of defining an Actor's protocol with ***`sealed trait`***&#173;*s*, we can use ***`enum`***&#173;*s*

```scala
  object SomeActor:

    enum Command:
      case Reset
      case Execute(times: Int)
      case Shutdown
      case Restart(jobId: Int)

    def apply(): Behavior[Command] = ???

```

* The above ***`enum`*** is de-sugared into ***`case class`***&#173;*es* and ***`val`***&#173;*ues*
* There is one issue: we have to refer to ***`enum`*** members using a qualification

```scala
someActor ! SomeActor.Command.Execute(5)
```

* We can solve this using Scala 3's ***`export`*** feature

---

## ***`enum`*** internals - I
## &#173;

* The different cases on an ***`enum`*** are defined in the ***`enum`***&#173;*'s* companion object
* The underlying case classes are not visible in the return type of the ***`apply`*** methods used to create them
    * They can be made visible by constructing them with a ***`new`***
* The default behaviour

```scala
scala> enum Command:
         case Reset
         case IncrementBy(inc: Int)
         case Configure(init: Int, inc: Int)

scala> val reset = Command.Reset         // `reset` is a singleton case mapped to a val definition 
       val inc = Command.IncrementBy(2)  // `inc` is NOT a case class
       val r1 = Command.Reset
       val r2 = Command.Reset

scala> println(r1 eq r2)
true
scala> inc.<TAB>                         // which members are defined on this enum instance?
               ->             ne             equals         $ordinal       getClass       formatted      isInstanceOf
!=             ==             wait           notify         ensuring       hashCode       notifyAll      synchronized
##             eq             clone          ordinal        finalize       toString       asInstanceOf
```

---

## ***`enum`*** internals - II
## &#173;

* Making underlying ***`case class`*** visible by using ***`new`***

```scala
scala> enum Command:
         case Reset
         case IncrementBy(inc: Int)
         case Configure(init: Int, inc: Int)

scala> val inc = new Command.IncrementBy(2)    // `inc` IS a case class
       val conf = new Command.Configure(0, 5)  // `conf` IS a case class

scala> println(s"inc.ordinal = ${inc.ordinal}")
inc.ordinal = 1

scala> val conf1 = conf.copy(inc = 7)
       println(conf1)                          // will print Configure(0, 7)
Configure(0,7)

scala> conf1.<TAB>                             // which members are defined on this enum instance?
                _2              wait            canEqual        formatted       productPrefix
!=              eq              clone           ensuring        notifyAll       productElement
##              ne              equals          finalize        asInstanceOf    productIterator
->              inc             notify          getClass        isInstanceOf    productElementName
==              copy            ordinal         hashCode        productArity    productElementNames
_1              init            $ordinal        toString        synchronized
```

---

## Scala 3's ***`export`*** clause - I
## &#173;

* Scala 3's export clause allows us to create aliases for selected members

```scala
object A:
  object B:
    object C:
      val x: Int = 5
  export B.C.x
```

* We can now do the following:

```scala
scala> A.x
val res1: Int = 5
```

* We have only scratched the surface of the ***`export`*** clause. It enables us to compose new functionality rather than going through OOP's inheritance based approach. More details on this in the ***`export`*** [***reference documentation***](https://dotty.epfl.ch/docs/reference/other-new-features/export.html)

---

## Scala 3's ***`export`*** clause - II

* Composing functionality using ***`export`***

```scala
private class HotAirOven:
  var on = false
  var tempSetting = 180
  def setTemp(temp: Int): Unit = {tempSetting = temp; println(s"Setting temperature to $temp")}
  def turnOn: Unit = on = true
  def turnOff: Unit = on = false
  def status: String = s"Hot-air oven is ${if (on) "on" else "off"} - Temperature setting at $tempSetting”
```
```scala
private class MicrowaveOven:
  var on = false
  var powerSetting = 600
  def setPower(watts: Int): Unit = {powerSetting = watts; println(s"Setting power to $powerSetting")}
  def turnOn: Unit = on = true
  def turnOff: Unit = on = false
  def status: String = s"Microwave oven is ${if (on) "on" else "off"} - Power setting at $powerSetting Watt"
```
```scala
class CombiOven:
  private val hotAirOven = new HotAirOven
  private val microwaveOven = new MicrowaveOven
  def status = s"${hotAirOven.status}\n${microwaveOven.status}"

  export hotAirOven.{setTemp, turnOn => hotAirOn, turnOff => hotAirOff}
  export microwaveOven.{setPower, turnOn => microwaveOn, turnOff => microwaveOff}
```

---

## Scala 3's ***`export`*** clause - III

* And using it...

```scala
scala> val oven = new CombiOven
val oven: CombiOven = CombiOven@71065f9e

scala> println(oven.status)
Hot-air oven is off - temperature setting at 180
Microwave oven is off - Power setting at 600 Watt
```
```scala
scala> oven.setTemp(90);oven.setPower(900)
Setting temperature to 90
Setting power to 900

scala> println(oven.status)
Hot-air oven is off - temperature setting at 90
Microwave oven is off - Power setting at 900 Watt
```
```scala
scala> oven.hotAirOn

scala> println(oven.status)
Hot-air oven is on - temperature setting at 90
Microwave oven is off - Power setting at 900 Watt
```
```scala
scala> oven.hotAirOff;oven.microwaveOn

scala> println(oven.status)
Hot-air oven is off - temperature setting at 90
Microwave oven is on - Power setting at 900 Watt
```

---

## Summary

* In this chapter, we have covered:
    * Scala 3's new features such as
        * ***`enum`***&#173;: a concise manner to define enumerations and ADT's
        * ***`export`***&#173;: enables the definition of new objects through composition as an alternative to inheritance 
* Using ***`enum`***&#173;*s* is easy, but there are still quite a few unknowns
    * No default exposure of the underlying ***`case class`***&#173;*es*

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## Using ***`enum`*** and ***`export`***
## &#173;

* In this exercise, we will swap the ***`sealed trait`*** / ***`case object`*** / ***`case class`*** Akka protocol encoding with an ***`enum`*** based one 
* Make sure you're positioned at exercise *"enum and export"*
* Follow the exercise instructions provided in the README.md file in the code folder

