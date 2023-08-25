package org.lunatech.dotty.intersectionanduniontypes

type S // Some type S
type T // Some type T
type ST = S & T // Intersection Type ST which has all the properties of both S & T

@main def inter() = {
  trait Growable {
    def growBy(percent: Int): this.type = {
      println(s"Growing by $percent%")
      this
    }
  }

  trait Paintable {
    def paint(color: Int): this.type = {
      println(s"Painted with color $color")
      this
    }
  }

  def resizeAndPaint(obj: Growable & Paintable): Unit = {
    obj.growBy(20).paint(0x10ff00).growBy(40).paint(0x0010ff)
  }

  resizeAndPaint(new Growable with Paintable)
}
