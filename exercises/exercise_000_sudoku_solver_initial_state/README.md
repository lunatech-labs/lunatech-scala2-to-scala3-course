# An Akka Typed/Scala based Sudoku Solver - Initial State

## Background

This application implements an Akka Typed/Scala based Sudoku Solver that
consists of 29 actors.

The application will also start up an instance of a Sudoku problem generator.

We will first show you how to run the application.


## Steps

- In the project's root folder, start an `sbt` session
- Run the Sodukosolver by executing the `runSolver` command from the `sbt` prompt
- Note that you can stop the application by hitting `Return` in the sbt session
- Observe sudoku solver in action: you should see the following output:

```scala
man [e] > Scala 2 to Scala 3 > sudoku solver initial state > runSolver
[info] running org.lunatechlabs.dotty.SudokuSolverMain -Dcluster-node-configuration.cluster-id=cluster-0 -Dcluster-node-configuration.node-hostname=localhost -Dakka.remote.artery.canonical.port=2550
08:55:17 INFO  [] - Slf4jLogger started
SLF4J: A number (1) of logging calls during the initialization phase have been intercepted and are
SLF4J: now being replayed. These are subject to the filtering rules of the underlying logging system.
SLF4J: See also http://www.slf4j.org/codes.html#replay
Hit RETURN to stop solver
08:55:18 INFO  [akka://sudoku-solver-system/user/sudoku-solver] - Sudoku processing time: 156 milliseconds
08:55:18 INFO  [akka://sudoku-solver-system/user/sudoku-problem-sender] -
+---+---+---+
|712|948|635|
|835|762|941|
|496|531|278|
+---+---+---+
|147|896|352|
|569|213|784|
|283|475|169|
+---+---+---+
|324|659|817|
|951|387|426|
|678|124|593|
+---+---+---+
08:55:18 INFO  [akka://sudoku-solver-system/user/sudoku-solver] - Sudoku processing time: 44 milliseconds
08:55:18 INFO  [akka://sudoku-solver-system/user/sudoku-problem-sender] -
+---+---+---+
|678|124|593|
|951|387|426|
|324|659|817|
+---+---+---+
|283|475|169|
|569|213|784|
|147|896|352|
+---+---+---+
|496|531|278|
|835|762|941|
|712|948|635|
+---+---+---+
.
.
.
```

- You can control the rate at which the Sudoku problem generator sends problems by
  tweaking the setting `sudoku-solver.problem-sender.send-interval` in the
  `sudokusolver.conf` configuration file
