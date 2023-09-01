package opaquetypelaliases.valueclasses

case class Kilometres(value: Double) extends AnyVal

class Rocket(distanceTravelled: Kilometres):
  def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
    Kilometres(distanceTravelled.value + distanceToAdvance.value))

val rocket1 = new Rocket(Kilometres(0))

val rocket2 = rocket1.advance(Kilometres(12000))
