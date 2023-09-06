<!-- .slide: data-background-color="#781010" data-background-image="images/bg-reveal.ps.png" -->

[//]: # (The following is a hack to move the slide H2 section down)
## &#173;
## &#173;
## &#173;
## &#173;
## MULTIVERSAL EQUALITY

---

## Multiversal Equality: Motivation
## &#173;

* One major selling point of Scala is type-safety
* But Scala 2 universal equality reduces type-safety in some cases
* Universal Equality means that '***`==`***' and '***`!=`***' will compare ***<ins>any</ins>* two values** no matter their types
* This can lead to bugs, typically after refactorings - even refactorings that are just about changing types
    * *"This is a real worry in practice. I recently abandoned a desirable extensive refactoring because I feared that it would be too hard to track down such errors."*
    * See [Dotty issue 1247](https://github.com/lampepfl/dotty/issues/1247)

---

## Universal Equality: Risk of bugs
## &#173;

* Imagine a simple ***`Item`*** model...

```scala
final case class Id(value: Long) extends AnyVal
final case class Item(id: Id)
```

* ...which we refactor to use ***`UUID`*** as the '***`id`***' type...

```scala
import java.util.UUID

final case class Id(value: Long) extends AnyVal // We keep because used elsewhere in our project
final case class Item(id: UUID)
```

* ...but we forget to change the ***`Repository`***...

```scala 

class Repository(items: Seq[Item]):
  def findById(id: Id): Option[Item] = {
     // The following type-checks but will always return false
     // so we never find any items
    items.find(_.id == id)  // Comparing an `id` with a `UUID`
  }
```

---

## Multiversal Equality: Typeclass approach
## &#173;

* Attempts to improve equality checking in Scala Community go back at least to 2010
* Scalaz library first introduced the ***`Equal`*** typeclass, inspired by the ***`Eq`*** typeclass in Haskell
* Scala 3 also adopts this typeclass approach:
    * *"Ultimately it's the developer who [is] best placed to characterize which equalities make sense. ... The best known way to characterize such relationships is with type classes"*
    * See [Dotty issue 1247](https://github.com/lampepfl/dotty/issues/1247)
* In Scala 3 the typeclass is called ***`CanEqual`***

---

## Opt-in Multiversal Equality
## &#173;

* The CanEqual typeclass is defined as follows

```scala
 @implicitNotFound("Values of types ${L} and ${R} cannot be compared with == or !=")
sealed trait CanEqual[-L, -R]
```
* To compare two values of the two types ***`L`*** and ***`R`*** the compiler searches for an ***`CanEqual`*** instance linking the two types
* By default (when multiversal equality is not enabled), the compiler falls back to default ***`CanEqual[Any, Any]`*** instance
### &#173;
* Is 'opt-in' to keep backwards compatibility
    * Either locally with import: ***`import scala.language.strictEquality`***
    * Or globally with compiler option: ***`-language:strictEquality`***

---

## Backward Compatibility: Warning
## &#173;

* WARNING: Even without opting-in, Scala 3 equality comparison isn't exactly backwards compatible with Scala 2

### &#173;
* Scala 2:

```scala
scala> Seq(1) == Set(1)
res0: Boolean = false
```

### &#173;
* Scala 3:
```scala
scala> Seq(1) == Set(1)
1 |Seq(1) == Set(1)
  |^^^^^^^^^^^^^^^^
  |Values of types Seq[Int] and Set[Int] cannot be compared with == or !=
```

---

## Multiversal Equality: Example I
## &#173;

* Revisiting our ***`Item`*** refactoring example...

```scala
import java.util.UUID

final case class Id(value: Long) extends AnyVal // We keep because used elsewhere in our project
final case class Item(id: UUID)
```

* ...forgetting to change the ***`Repository`*** will produce a type error (if we opt-in to multiversal equality)

```scala
import scala.language.strictEquality

class Repository(items: Seq[Item]):
  def findById(id: Id): Option[Item] = {
    items.find(_.id == id)
                ^^^^^^^^^^
                Values of types java.util.UUID and Id cannot be compared with == or !=
  }
```

---

## Multiversal Equality: Example II
## &#173;

* So we realise our mistake can we change 'Id' to 'UUID' in the Repository method

```scala
import scala.language.strictEquality

class Repository(items: Seq[Item]):
  def findById(id: UUID): Option[Item] = {
    items.find(_.id == id)
                ^^^^^^^^^^
               Values of types java.util.UUID and java.util.UUID cannot be compared with == or !=
  }
```

* Even though the type is correct, we need to tell the compiler that the type can be compared for equality. We need an ***`CanEqual[UUID, UUID]`*** instance
## &#173;
* NOTE: Out of the box, Scala 3 provides reflexive ***`CanEqual`*** instances for each of:
    * The primitives - ***`Byte`***, ***`Short`***, ***`Char`***, ***`Int`***, ***`Long`***, ***`Float`***, ***`Double`***, ***`Boolean`***
    * ***`j.l.Number`***, ***`j.l.String`***, ***`j.l.Character`***, ***`j.l.Boolean`***
    * ***`scala.collection.Seq[A]`***, ***`scala.collection.Set[A]`***

---

## Multiversal Equality: Example III
### &#173;

* The easiest way to provide new ***`CanEqual`*** instances is to use Typeclass Derivation mechanism available in Scala 3
* Scala 3 built-in typeclasses like ***`CanEqual`*** and ***`Ordering`*** provide a derived method for generating new typeclass instances of types defined elsewhere (like ***`UUID`***)

```scala
import scala.language.strictEquality

given CanEqual[UUID, UUID] = CanEqual.derived

class Repository(items: Seq[Item]):
  def findById(id: UUID): Option[Item] = {
    items.find(_.id == id)
  }
```

* Now the ***`Repository`*** compiles and the equality is correct
* NOTE: For classes under our control we can use the '***`derives CanEqual`***' syntax, e.g.

```scala
final case class Item(id: UUID) derives CanEqual
```

---

## BTW: Why the name "Multiversal Equality"?
## &#173;

* [Dotty issue 1247](https://github.com/lampepfl/dotty/issues/1247)
* *"The scheme effectively leads to a partition of the former universe of types into sets of types. Values with types in the same partition can be compared among themselves but values with types in different partitions cannot ... "*
    * Providing / deriving a new ***`CanEqual`*** instances creates a new partition
    * "...So instead of a single universe of values that can be compared to each other we get a multiverse of partitions. Hence the name of the proposal: Multiversal Equality"

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## Multiversal Equality
## &#173;

* In this exercise, we will see how to 'opt-in' to Multiversal Equality and how to create an ***`CanEqual`*** Type class instance
    * Make sure you're positioned at exercise *"multiversal equality"*
    * Follow the exercise instructions provided in the README.md file in the code folder
