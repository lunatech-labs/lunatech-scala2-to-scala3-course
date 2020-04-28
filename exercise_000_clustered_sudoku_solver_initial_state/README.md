# Clustered Sudoku Solver - Initial State

## Background

This application implements a clustered Sudoku Solver. The solver is
an Akka based application and each solver consists of 29 actors.

The application will also start up a `single` instance of a Sudoku
problem generator. This is done by running it as a so-called Akka
Cluster Singleton.

As the application is clustered, cluster formation needs to take place
and this process is bootstrapped by starting the so-called first seed
node (the node with id `0`)

We will first show you how to run the application.


## Steps

- Run the Sodukosolver by executing the `solver0` command from the sbt prompt
- Observe sudoku solver in action.
