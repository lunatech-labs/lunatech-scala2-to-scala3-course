package org.lunatech.dotty.using

import scala.annotation.Annotation

object Scala2Example {

  def startEngine(engine: Engine)(implicit config: EngineConfig): Unit =
    println(s"Starting $engine with $config")

  implicit val cfg: EngineConfig = EngineConfig(10, 50, "base-config")

  @main def CA_0: Unit = {
    startEngine(Engine("AC-35-B/002"))
  }

}

object MovingToDotty_1 {

  def startEngine(engine: Engine)(using config: EngineConfig): Unit =
    println(s"Starting $engine with $config")

  implicit val cfg: EngineConfig = EngineConfig(10, 50, "base-config")

  @main def CA_1: Unit = {
    startEngine(Engine("AC-35-B/002"))
  }

}

object MovingToDotty_2 {

  def startEngine(engine: Engine)(using config: EngineConfig): Unit =
    println(s"Starting $engine with $config")

  given cfg: EngineConfig = EngineConfig(10, 50, "base-config")

  @main def CA_2: Unit = {
    startEngine(Engine("AC-35-B/002"))
  }

}
