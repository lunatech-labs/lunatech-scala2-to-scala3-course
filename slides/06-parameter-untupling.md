<!-- .slide: data-background-color="#781010" data-background-image="images/bg-reveal.ps.png" -->

[//]: # (The following is a hack to move the slide H2 section down)
## &#173;
## &#173;
## &#173;
## &#173;
## PARAMETER UNTUPLING

---

## Parameter Untupling
## &#173;

* The need to write a pattern matching decomposition when mapping over a sequence of tuples is inconvenient.
* Consider you have a list of pairs:

```scala
 val pairs = List(1, 2, 3).zipWithIndex
 // pairs: List[(Int, Int)] = List((1,0), (2,1), (3,2))

```

* Imagine you want to map ***`pairs`*** to a list of ***Int***s so that each pair of numbers is mapped to their sum.
* The best way to do this in Scala 2 is with a pattern matching anonymous function

```scala
 pairs map {
   case (x, y) => x + y
 }
 // res1: List[Int] = List(1, 3, 5)
```
---

## Parameter Untupling
## &#173;

* Scala 3 now allows us to write:

```scala
 pairs map ((x, y) => x + y)
 // val res0: List[Int] = List(1, 3, 5)
```

* Or it can be written as:

```scala
pairs.map(_ + _)
 // val res1: List[Int] = List(1, 3, 5)
```

---
<!-- .slide: data-background-color="#94aabb" data-background-image="images/bg-reveal.ps.png" -->

## Parameter Untupling
## &#173;

* In this exercise we will use parameter untupling
    * Make sure you're positioned at exercise *"parameter untupling"*
    * Follow the exercise instructions provided in the README.md file in the code folder
