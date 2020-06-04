# Union Types

## Background

In the previous exercise, we re-encoded the protocol of the different actors
in our application from a `sealed trait` based one to one using Dotty
enumerations. This however, is only a small first step towards a major
simplification of the way encode the exchange of messages between actors.
We will now explore using Dotty's `Union Types` to vastly simplify the Scala 2
based implementation. We will succeed in eliminating the so-called message
adapters and response wrappers from our code!

The general idea is that we have both an external protocol for an actor (which
is exactly the same as in the original implementation) and an [extended] 
internal protocol that also 'understands' the responses the actor can receive.

## Steps

Have close look at the 4 different actors in the application:

  - `SudokuProblemSender`
  - `SudokuSolver`
  - `SudokuProgressTracker`
  - `SudokuDetailProcessor`

Which of these actors receive messages that are responses from other actors?


`Hint:` In its present form, the application utilises message adapters. You
      can easily spot these by doing a search on the factory method to
      create them: `context.messageAdapter`.

`Tip:`  Tackle the `SudokuProblemSender` first. After this, proceed with
      the other actors.

- Create a type alias named `CommandAndResponses` for the Union of the
  actor's external protocol (`Command`) and the `Response` message types.
  This new type will be the type of the internal protocol.

- Adapt the `apply` method that creates the actor's behaviour so that it
  still is the original behaviour as seen from the outside, but which
  has the extended behaviour (corresponding to `CommandAndResponses`).
  You will need to make a few extra modifications to make everything
  type check. Some hints that may put you on the right track:

  - Note that `Behaviors.setup` has a type parameter
  - `Behaviors.setup` returns a `Behavior` of some type. Look at the
    API docs of `Behavior` and specifically at the `narrow` method.
    You will need to apply this method in your code
  - A typical pattern in Akka Typed code is the inclusion of a so-called
    `ActorRef` conventionally called `replyTo`. In the existing code,
    this is the message adaptor. With the latter being eliminated from
    the code, you need to substitute it with another `ActorRef`. Which
    one makes sense? In this context, have a look at the available
    members on an actor's `context`

- Eliminate any unused code such as:
  - The message adapters
  - The `Response` message wrappers

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass

- Verify that the application runs correctly
  
