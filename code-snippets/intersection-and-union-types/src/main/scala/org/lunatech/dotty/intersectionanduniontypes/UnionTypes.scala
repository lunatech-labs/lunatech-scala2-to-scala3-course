package org.lunatech.dotty.intersectionanduniontypes

object UnionTypesExample {
  enum Command {
    case Reset
    case Run(times: Int)
  }
  enum Response {
    case RunFailed(reason: String)
    case RunFinished
  }
  trait Behavior[-A] {
    def treatMsg(message: A): Unit = {
      println(s"Treating message: $message")
    }
  }
  type CommandAndResponse = Command | Response

  @main def unionTypes() = {
    // implicitly[Behavior[CommandAndResponse] <:< Behavior[Command]]
    val internalBehavior: Behavior[CommandAndResponse] = new Behavior[CommandAndResponse]{}
    val externalBehavior: Behavior[Command] = internalBehavior   // Contravariance at work

    // Handling of Command and Responses by internal behavior
    internalBehavior.treatMsg(Command.Reset)
    internalBehavior.treatMsg(Command.Run(5))
    internalBehavior.treatMsg(Response.RunFailed("Too much to do"))
    internalBehavior.treatMsg(Response.RunFinished)
    
    externalBehavior.treatMsg(Command.Reset)
    externalBehavior.treatMsg(Command.Run(110))
    // The following doesn't compile
    //externalBehavior.treatMsg(Response.RunFailed("Too much to do"))
  }
}

