<!-- .slide: data-background-color="#781010" data-background-image="images/bg-reveal.ps.png" -->

[//]: # (The following is a hack to move the slide H2 section down)
## &#173;
## &#173;
## &#173;
## &#173;
## CONTEXTUAL ABSTRACTIONS - I
## Extension methods

---

## Contextual Abstractions - I
## &#173;

* Scala's *implicits* enable abstraction over context
* *implicits* enables overcoming common phenomena in Functional Programming:
    * Writing out complex instances in full can be very cumbersome
    * Functions lead to long argument lists, which render code less readable and feels like boilerplate
    * The killer application for *implicits* are so-called Type classes
    * Note that using *implicits* to pass in parameters implicitly is powerful but can also be quite confusing.

---

## Contextual Abstractions - II
## &#173;

* Scala's *implicits* provide a solution to this:
    * Defining a value of some type marked as ***`implicit`***
    * Adding a separate so-called *implicit parameter* list to a function
    * When such a function is called, the compiler will look-up the *implicit value *in scope (applying various scoping rules) and pass it to the function
    * In case no *implicit value* is found, the code will not compile
## &#173;
* Instead of typing out complex instances in full, contextual abstractions allow one to *"summon"* such instances based on their type

---

## Contextual Abstractions - III
## &#173;

* *Implicits* turned out to be extremely useful for other reasons than the one quoted on the previous slide
    * Enables the implementation of Scala Type Classes
    * Dependency injection
* So-called *implicit conversions* were touted for some time and misused by many, leading to code that was very difficult to understand
* Scala 2's *implicits* confused many Scala users because of the multiple meanings of the ***`implicit`*** keyword
    * Used to define *implicit values*
    * Used to mark a function's parameter list as *implicit*
    * As a modifier on a ***`class`*** definition as the basis to define extension methods
    * The keyword ***`implicitly`*** to look-up an instance of an implicit value
* "By design", in Scala 2, a function can have only one implicit parameter list

---

## Contextual Abstractions - IV
## &#173;

* Scala 3 redesigns the contextual abstractions to eliminate the aforementioned problems
* The following contextual abstractions are defined in Scala 3:
    * ***`given`*** instances: used to define "canonical" values in some context
    * ***`using`*** clauses & ***`summon`***-ing given instances
    * *Context bounds*
    * ***`given`*** imports
    * Extension methods
    * *Multiversal equality*
    * *Context Functions*, *implicit conversions*, *type class derivation* (not discussed in this course)
* Let's start with the new extension methods syntax

---

## Extension Methods
## &#173;

* Extension methods are a great way to add functionality to a type after it's been defined.
* They are a simpler alternative to ***`implicit class`***&#173;es for the *'Enrich my library'* pattern
* Let's take a look at a Scala 2 example:

```scala
 implicit class IntOps(val i: Int) extends AnyVal {
   def isEven: Boolean = i % 2 == 0
   def unary_! : Int = -i
 }

 println(6.isEven) // true
 println(!6)       // -6
```

* With the help of an ***`implicit class`*** we just added an extension method to ***`Int`***
* But still we have to write quite a lot of boilerplate code...

---

## Extension Methods
## &#173;

* Scala 3 completely replaces the use of the ***`implicit`*** keyword with some contextual abstractions

```scala
 extension (i: Int)
   def isEven: Boolean = i % 2 == 0

 extension (i: Int)
   def unary_! : Int = -i
```

* Extension methods are defined as follows:
* Multiple extensions on the same type can be written more concisely:

```scala
 // Definition of a collective extension
 extension (i: Int)
   def isEven: Boolean = i % 2 == 0
   def unary_! : Int = -i
```

---

## Generic Extensions
## &#173;

* Extension methods can be generic with *type parameters*
* The type parameters come immediately after the ***`extension`*** keyword and are followed by the extended parameter

```scala
 extension [T](xs: List[T])
   def second = xs.tail.head

 extension [T](xs: List[List[T]])
   def flattened = xs.foldLeft[List[T]](Nil)(_ ++ _)

 extension [T : Numeric](x: T)
   def + (y: T): T = summon[Numeric[T]].plus(x, y)
```

* The generic extension method defined above can be invoked as follows:

```scala
 scala> List(1, 2, 3).second
 val res1: Int = 2

 scala> List(List(1, 2, 3), List(9, 8, 7)).flattened
 val res2: List[Int] = List(1, 2, 3, 9, 8, 7)
```

---

## Extension Methods - Operators
## &#173;

* We can define operators with the help of extension methods

```scala
 extension (a: String)
   def < (b: String): Boolean = a.compareTo(b) < 0
  
 extension (a: Int)
   def +: (b: List[Int]) = a :: b


 println("abc" < "pqr")      // true

 val lst = 1 +: List(2,3,4)  // val lst: List[Int] = List(1, 2, 3, 4)
```

---

## Extension Methods - Applicability - I

* Four possible conditions may make an extension method applicable.
* Let's have a look at each of them:
    1. It is visible under a simple name, by being defined inherited or imported in a scope enclosing the application

```scala
  trait IntOps:
    extension (i: Int) def isZero: Boolean = i == 0
    extension (i: Int) def safeMod(x: Int): Option[Int] =   // extension method defined in same scope IntOps
      if x.isZero then None
      else Some(i % x)

  object IntOpsEx extends IntOps:
    extension (i: Int) def safeDiv(x: Int): Option[Int] =   // extension method brought into scope via inheritance from IntOps
      if x.isZero then None
      else Some(i / x)

  object SafeDiv:
    import IntOpsEx._                                       // brings safeDiv and safeMod into scope
    extension (i: Int) def divide(d: Int) : Option[(Int, Int)] =
     (i.safeDiv(d), i.safeMod(d)) match
        case (Some(d), Some(r)) => Some((d, r))
        case _ => None
  
  import SafeDiv.*; import SafeDiv.*
    
  val d1 = 25.divide(3)   // Some((8,1))
  val d2 = 25.divide(0)   // None
```

---

## Extension Methods - Applicability - II

2. It is a member of some given instance at the point of the application

```scala
  trait IntOps:
    extension (i: Int)
      def isZero: Boolean = i == 0
      def safeMod(x: Int): Option[Int] =
        // extension method defined in same scope IntOps
        if x.isZero then None
        else Some(i % x)

  given IntOps = new IntOps{}  // IntOps is now a given in this scope

  20.safeMod(3)   // Some(2)
  20.safeMod(0)   // None
```

---

## Extension Methods - Applicability - III

3. The reference is of the form ***`r.m`*** and the extension method is defined in the implicit scope of the type of ***`r`***.
4. The reference is of the form ***`r.m`*** and the extension method is defined in some given instance in the implicit scope of the type of ***`r`***.

```scala
  class List[T]:
    ...
  object List:

    extension [T](xs: List[List[T]])
      def flatten: List[T] = xs.foldLeft(Nil: List[T])(_ ++ _)

    given [T: Ordering] as Ordering[List[T]]:
      override def compare(l1: List[T], l2: List[T]): Int = ...
      extension (xs: List[T])
        def < (ys: List[T]): Boolean = ...

  // Rule 3 - extension method available since it is in the implicit scope of List[List[Int]]
  List(List(1, 2), List(3, 4)).flatten // List(1, 2, 3, 4)

  // Rule 4 - extension method available since it is in the given Ordering[List[T]],
  // which is itself in the implicit scope of List[Int]
  List(1, 2) < List(3)                 // True
```

---

## Summary
## &#173;

* In this chapter we have learned:
    * ***`extension method`***&#173;s allow adding methods to a type after the type is defined
    * It replaces Scala 2's implicit classes for the *"Enrich my library pattern"*
    * We can define operators with the help of extension methods
    * ***`extension method`***&#173;s can be generic with Generic extensions with type parameters
    * Ease the definition of multiple extensions of a common type with Collective extensions

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## Extension Methods
## &#173;

* In this exercise we will use extension methods instead of implicit classes to add methods to our types.
    * Make sure you're positioned at exercise *"extension methods"*
    * Follow the exercise instructions provided in the README.md file in the code folder