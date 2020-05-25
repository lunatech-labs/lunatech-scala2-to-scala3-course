package org.lunatech.dotty.enumeration

@main def enumMemberVisibilityDefault = {
  
  enum Command {
    case Reset
    case IncrementBy(inc: Int)
    case Configure(init: Int, inc: Int)
  }

  val reset = Command.Reset         // `reset` is a singleton case mapped to a val definition 
  val inc = Command.IncrementBy(2)  // `inc` is NOT a case class

  val r1 = Command.Reset
  val r2 = Command.Reset

  println(r1 eq r2)                  // will print `true`
  println(s"reset.ordinal = ${reset.ordinal}") // will print reset.ordinal = 0
  println(s"inc.ordinal = ${inc.ordinal}")

}

@main def enumMemberVisibility = {
  
  enum Command {
    case Reset
    case IncrementBy(inc: Int)
    case Configure(init: Int, inc: Int)
  }

  val inc = new Command.IncrementBy(2)    // `inc` IS a case class
  val conf = new Command.Configure(0, 5)  // `conf` IS a case class

  println(s"inc.ordinal = ${inc.ordinal}")
  println(s"conf  = $conf")               // will print Configure(0, 5)
  val conf1 = conf.copy(inc = 7)
  println(s"conf1 = $conf1")              // will print Configure(0, 7)
  conf1                                   // which members are defined on this enum instance?
}
