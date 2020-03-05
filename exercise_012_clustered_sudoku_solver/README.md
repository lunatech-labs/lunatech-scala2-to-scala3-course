# Clustered Sudoku Solver

## Description

In this step, we add an actor based Sudoku solver. A solver is started on each
node and an Akka Group Router is added to distribute the processing of problem
across all available solvers. The Router is configured using a round-robin
routing strategy (we override the default one, which is random routing strategy
 - feel free to experiment with that one).

An Akka Group Router is cluster-aware: the router will automatically start
routing messages to new routees (Sudoku solvers in this case), or stop routing
to routees when these can no longer be accessed (because, for example, of
network issues) or are stopped.

For demonstration purposes, a Sudoku problem generator is added to the set-up,
and this generator is running as an Akka Cluster Singleton. In practical use
cases, the problems will most probably be coming from external sources (like a
Sudoku Solver Client). Running the problem generator allows us to show how the
system behaves in certain scenarios (like performing a coordinated shutdown of
the node on which the singleton is running, or when it is stop by a Split Brain
Resolver in case of a partitioned cluster).

## Steps

- Check out the new code in the file `SudokuSolverMain`

You'll see that two actors are created: a `SudokuSolver` and a
`SudokuProblemGenerator`. Also, a group router is created.

1. Run `sbt universal:packageBin` to create the packaged binaries.
2. Run the solver by executing the `run` command with the
   appropriate exercise number.
3. Run additional instances of the Sudoku solver by executing the
   `run` command in a new terminal and by passing an instance
   sequence number as the second argument to the `run` command 
4. The problem sender send problems at a fixed rate which can be changed
   with the `sudoku-solver.problem-sender.send-interval` settings in the
   `sudokusolver.conf` configuration file.
5. Observe sudoku solver in action.
