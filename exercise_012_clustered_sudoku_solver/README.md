# Clustered Sudoku Solver

## Description

This application implements a clustered Sudoku Solver. The solver is
an Akka based application and each solver consists of 29 actors.

The application will also start up a `single` instance of a Sudoku
problem generator. This is done by running it as a so-called Akka
Cluster Singleton.

As the application is clustered, cluster formation needs to take place
and this process is bootstrapped by starting the so-called first seed
node (the node with id `0`)


## Steps

1. Run `sbt universal:packageBin` to create the packaged binaries.
2. Run the solver by executing the `run` command with the
   appropriate exercise number and node number.
   
   For example, if this is exercise number 12, run node-0 by
   executing the following command in the project's root folder:
   
```scala
./run 12 0
```
   
3. Run additional instances of the Sudoku solver by executing the
   `run` command in a new terminal and by passing an instance
   sequence number as the second argument to the `run` command 
4. The problem sender send problems at a fixed rate which can be changed
   with the `sudoku-solver.problem-sender.send-interval` settings in the
   `sudokusolver.conf` configuration file.
5. Observe sudoku solver in action.
