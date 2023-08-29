package opaquetypelaliases.opaquetypealias

object UsingTheAliases {
  import OpaqueTypeAliasesDefinitions.*

  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(distanceTravelled + distanceToAdvance)

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

  val r1 = booster.advanceRocket(rocket1, Kilometres(100)) // No allocation of Kilometres object
  val r2 = booster.advanceRocket(rocket2, Miles(200)) // No allocation of Miles object

}
