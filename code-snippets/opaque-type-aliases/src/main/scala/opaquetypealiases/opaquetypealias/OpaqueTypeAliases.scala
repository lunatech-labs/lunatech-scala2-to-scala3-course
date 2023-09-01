package opaquetypelaliases.opaquetypealias

object OpaqueTypeAliasesDefinitions:

  opaque type Kilometres = Double
  object Kilometres:
    def apply(d: Double): Kilometres = d

  opaque type Miles = Double
  object Miles:
    def apply(d: Double): Miles = d

  extension (a: Kilometres)
    @scala.annotation.targetName("plusKm")
    def +(b: Kilometres): Kilometres = a + b
    def toMiles: Miles = a / 1.6

  extension (a: Miles)
    @scala.annotation.targetName("plusMiles")
    def +(b: Miles): Miles = a + b
    def toKm: Kilometres = a * 1.6
