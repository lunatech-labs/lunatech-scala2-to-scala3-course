package org.lunatech.dotty.using

final case class RecordDev(recordType: String)
final case class PlayerDev(playerType: String)

object MultipleUsingClauses {
  def recordAndMonitor(recordGain: Int)
                      (using recordDev: RecordDev)
                      (volume: Int = 3)
                      (using player: PlayerDev) = {
    println(s"Recording with gain $recordGain from $recordDev to $player with volume $volume")
  }

  given PlayerDev = PlayerDev("Hifi chain")
  given RecordDev = RecordDev("Blue Yeti")

  @main def MUC_0: Unit = {
    recordAndMonitor(8)(20)
    recordAndMonitor(9)(using RecordDev("Built-in mic"))(5)
    recordAndMonitor(19)(80)(using PlayerDev("Second chain"))
    recordAndMonitor(15)(using RecordDev("Screen mic"))(70)(using PlayerDev("Car radio"))
  }
}

// Experiment with opaque type aliases & type erasure & contextual abstractions
object MultipleUsingClauses2 {
  opaque type Mul = Int
  opaque type Add = Int

  object Mul { def apply(n: Int): Mul = n}
  object Add { def apply(n: Int): Add = n}

  import scala.annotation.targetName

  extension (m: Mul)
    @targetName("mullAsInt")
    def asInt: Int = m

  extension (a: Add)
    @targetName("addAsInt")
    def asInt: Int = a
 
  @main def MUC_1: Unit = {
    val r = 5
    println(r)
  }
}

object Use {
  import MultipleUsingClauses2.{Mul, Add}
  import MultipleUsingClauses2._

  given Mul = Mul(5)
  given Add = Add(20)

  @main def useIt = {
    val r = 10 * summon[Mul].asInt + summon[Add].asInt
    println(r)
  }
}