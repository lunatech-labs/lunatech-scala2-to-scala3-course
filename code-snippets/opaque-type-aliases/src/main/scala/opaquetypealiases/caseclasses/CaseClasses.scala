package opaquetypelaliases.caseclasses

case class Kilometres(value: Double)

class Rocket(distanceTravelled: Kilometres):
  def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
    Kilometres(distanceTravelled.value + distanceToAdvance.value))

val rocket = Rocket(Kilometres(1000))

val r1 = rocket.advance(Kilometres(5000))
