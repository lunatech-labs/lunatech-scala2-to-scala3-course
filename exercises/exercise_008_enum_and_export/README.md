# Enumerations and Export

## Background

The Sudoku solver is an `Akka Typed` based application. It consists of 29 actors
that interact by exchanging messages and adhering to a well defined protocol,
i.e. each actor has a number of message types it 'understands', the so-called
`Command`s and a number of messages that it can send to other actors, usually in
`Response` to messages it received earlier.

In Scala 2, a typed actor uses ADTs to encode both `Command`s and `Responses`.
Following is a typical protocol definition:

```scala
  sealed trait Command
  final case class CommandA(n: Int)     extends Command
  case      object CommandB             extends Command
  final case class CommandC(n1: Double) extends Command
  
  sealed trait Response
  final case class ResponseA(sum: Double) extends Response
  case      object ResponseB              extends Response
```

In Dotty, we can use enumerations to encode the protocol in a more succinct way.
The example protocol listed above can be encoded in the following fashion:

```scala
  enum Command:
    case CommandA(n: Int)
    case CommandB
    case CommandC(n1: Double)

  enum Response:
    case ResponseA(sum: Double)
    case ResponseB
```

The compiler will desugar this encoding in `case class`es and `case object`s that
are equivalent to the original encoding.

There's one slight twist that comes with the new encoding: in the original
encoding, the different case classes and objects reside at the package level,
while in the enumeration based encoding, they are 'nested' one level deeper.
For example, `CommandA`, qualified at the package level is at `Command.CommandA`.
It would be inconvenient that, because of the switch to the elegant enum
based encoding of the protocol, we would have to change all occurrences
of `Command` or `Response` references to qualified ones. There's an easy way
to avoid this by using Dotty's `export` feature and we will apply this feature
in this exercise.


## Steps

- Have a look at the protocol definitions of the different actors used in
  the application:

  - `SudokuProblemSender`
  - `SudokuSolver`
  - `SudokuProgressTracker`
  - `SudokuDetailProcessor`

- Change the current encoding to a Dotty enum based one
  - Note that some of the actors have so-called `ResponseWrappers`. Leave
    these unmodified: include them in the protocol for the time being: we
    will remove these in the next exercise. You may have to change the
    `ResponseWrapper` from `private` to a public members
    
- Use Dotty's `export` feature to avoid having to refactor the code to
  utilise qualified references to the messages

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass
  
- Verify that the application runs correctly
