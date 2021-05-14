
sealed trait Engine
final case class CarEngine(cylinder: Int) extends Engine
final case class TruckEngine(cylinder: Int) extends Engine

trait Starter[T]:
  def start(e: T): Unit

given Starter[CarEngine] with {
  override def start(engine: CarEngine): Unit = 
    println(s"Starting CarEngine with ${engine.cylinder} cylinder(s)")
}

given Starter[TruckEngine] with {
  override def start(engine: TruckEngine): Unit = 
    println(s"Starting TruckEngine with ${engine.cylinder} cylinder(s)")
}

def startEngine[E <: Engine: Starter](engine: E): Unit = {
  val starter = summon[Starter[E]] // summons Starter[CarEngine] or Starter[TruckEngine] based on context
  starter.start(engine)
}

startEngine(CarEngine(6))
startEngine(TruckEngine(8))
startEngine(TruckEngine(1))(using summon[Starter[TruckEngine]])
