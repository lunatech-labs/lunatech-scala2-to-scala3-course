<!-- .slide: data-background-color="#781010" data-background-image="images/bg-reveal.ps.png" -->

[//]: # (The following is a hack to move the slide H2 section down)
## &#173;
## &#173;
## &#173;
## &#173;
## TOP-LEVEL DEFINITIONS

---

## Scala 3 Top-level definitions

* In Scala 2:
    * The source code for a package object is put in a separate file named ***`package.scala`***
    * Each package is allowed to have only one package object
    * Any definitions placed inside a package object are considered members of the package itself
    * ***`type`***, ***`def`***, and ***`val`*** definitions have to be put in an ***`object`***, ***`class`***, or ***`trait`***
* Scala 3 introduces top-level definitions:
    * In essence, it replaces package objects
    * Any source file may contain top-level definitions that go to the enclosing package
    * The restriction of "everything you write needs to be inside an ***`object`***, ***`class`***, or ***`trait`***" is removed with top-level definitions
    * Scala 3 will deprecate package objects at some point in time

---

## Package objects - issues
## &#173;

* ***`package object`***&#173;*s* were a source of contention and caused some Scala 2 warts
* ***`package object`***&#173;*s* are closed (in contrast with packages themselves)
* The exact behaviour of ***`package object`***&#173;*s* wasn't clearly specified which led to surprises
* Putting ***`class`***&#173;*es* and ***`trait`***&#173;*s* inside a ***`package object`*** was allowed but discouraged

---

## Package objects - an example

- Let's create a Scala 2 ***`package object`*** and put some definitions in it

```scala
// File: src/main/scala/org/lunatechlabs/greeting/package.scala
package org.lunatechlabs

package object greeting {
  case class Person(name: String)

  def greetPerson(person: Person): Unit = {
    println(s"Hello ${person.name}")
  }
}
```

- The following snippet shows the use of definitions from the package object

```scala
// File: src/main/scala/org/lunatechlabs/greeting/Application.scala
package org.lunatechlabs.greeting

object Application extends App {
  val bob = Person("Bob")
  greetPerson(bob)
}
```

* Anyone outside the package can import them by ***`import org.lunatechlabs.greeting._`***
* How can we achieve the same using Scala 3's top-level definitions?

---

## Top-level Definitions

* In Scala 3, the ***`package object`*** defined in the previous step becomes

```scala
// File: src/main/scala/org/lunatechlabs/greeting/TopLevelDefinitions.scala
package org.lunatechlabs.greeting

case class Person(name: String)

def greetPerson(person: Person): Unit =
  println(s"Hello ${person.name}")
```

* The compiler generates synthetic objects to wrap top-level definitions
    * In the above example, the contents of the source file ***`ToplevelDefinitions.scala`*** will be put in a synthetic object named ***`ToplevelDefinitions$package
`***
* Top-level definitions don't have the ability to inherit from another ***`trait`*** or ***`class`*** as we could do with ***`package object`***&#173;*s*
* *A workaround for this is to use a regular object and import all needed members*

---

## Top-level Definitions

* Top-level definitions for a given package can be spread across multiple source files if that's convenient for some reason
* A simple example:

```scala
// File src/main/scala/org/lunatechlabs/multi/TopLevel1.scala
package org.lunatechlabs.multi

val x = 5
private val private_x = 27
```

```scala
// File src/main/scala/org/lunatechlabs/multi/TopLevel2.scala
package org.lunatechlabs.multi

val y = 15
private val private_y = 730
```

```scala
// File src/main/scala/org/lunatechlabs/multi/Application.scala
package org.lunatechlabs.multi

object Application extends App {
  println(s"$x $y $private_x $private_y")
}
```

---

## Summary
## &#173;

* In this chapter, we have taken a closer look at top-level definitions. 
    * Package objects are now redundant and will be removed from Scala at some point in time
    * A source file can freely mix top-level
        * value
        * methods
        * type definitions
        * classes and objects
    * The top-level definitions in a source file can be accessed as members of the enclosing package.

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## Top-level Definitions
## &#173;

* In this exercise we will use top-level definitions to remove package object from our code base.
    * Make sure you're positioned at exercise *"top level definitions"*
    * Follow the exercise instructions provided in the README.md file in the code folder
