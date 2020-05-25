package org.lunatech.dotty.enumeration

object SomeActor {

  enum Command {
    case Reset
    case Execute(times: Int)
    case Shutdown
    case Restart(jobId: Int)
  }
  export Command._

  //def apply(): Behavior[Command] = ???
}

// someActor ! SomeActor.Command.Execute(5)