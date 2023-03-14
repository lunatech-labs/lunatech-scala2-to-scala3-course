# Moving Forward from Scala 2 to Scala 3

## Description

The target audience for this course is the Scala _Application_ Developer Community. In other words, the focus is on people who use the language to develop 'end-user' applications and not necessarily those developers who design and write libraries, toolkits or frameworks (although they may benefit from what is in this course if they haven't already worked with Scala 3).

The goal of the course is to explore the new features in the Scala language brought by [the Scala 3 project](https://dotty.epfl.ch). Even though it is still early days and the first release candidate of the Scala 3 language is probably not out for another 6 months, it is our believe that one can never start too early to learn Scala 3 for the following reasons:

- There is a lot to learn, so if you wait until everything is ready, you risk having to play catchup.
- There are many opportunities to contribute to the Scala 3 project, which will accelerate its development in the coming months. Using Scala 3 will provide opportunities to identity issues to be fixed sooner rather than later.

## Approach

We start from an existing Scala 2 application: an [[Akka](https://akka.io)] actor based Sudoku solver. We start from the application as-is, but with an sbt build definition that uses the Scala 3 compiler.

Through a series of exercises, we look at specific Scala 3 features and apply these to the Scala 2 code, thereby transforming it step-by-step into a version that exploits a whole series of nice features offered by the new language.

Note that this is work in progress, so keep watching this space!

## Usage

This repository is structured in such a way that it can be transformed into two different artifacts for specific use cases:

- A _studentified_ repository: this is a standalone sbt project which is typically used for teaching or learning purposes. A user of this repository will be working on one exercise at any one point in time and will be able to use a number of commands via the `cmtc` cli:

    - `cmtc set-course-root`: Sets the location of the current course as the default location for subsequent cmtc command invocations.
    - `cmtc list-exercises`: will print a list of all available exercises. The current exercise is marked with a **_*_**.
    - `cmtc next-exercise`: move to the next exercise and pull in the tests (if any) and exercise instructions for that exercise. This command preserves the current state of the application leaving any code added or changed by the student unmodified.
    - `cmtc previous-exercise`: the opposite of the `next-exercise` command.
    - `cmtc pull-solution`: pulls in the reference solution code for the current exercise. This command is handy in a class room setting where at some point in time, the instructor wants to move to the next exercise and the student hasn't completed the current exercise. Note that it will overwrite any code written by the student. Before pulling the exercise
      solution, the student can save the current state of their work with the `cmtc save-state` command.
    - `cmtc goto-exercise <exerciseID>`: jump to an exercise specified by the exerciseID.
      Remember that this will pull in tests for the exercise and probably you'll want to
      pull in the solution by running the `pullSolution` command.
    - `cmtc save-state`: can be executed prior to executing the `cmtc pull-solution`
      command: it will save the current state of an exercise so that it can be restored
      later.
    - `cmtc list-saved-states`: show the exercise ids of all exercises for which a
      state was saved.
    - `cmtc restore-state <exerciseID>`: restore a previously save exercise state.

- A _linearized_ repository: this is a git repository in which every exercise in the
  master repository is turned into a commit:
  - Used to edit the master repository: In general this is a scratch pad repository that
    is discarded once it has served its purpose. In this use case, interactive rebasing
    on the linearized repo is used to transform the code across a range of exercises.
    Once this rebasing is finished, all applied changes can be applied on the master
    repo by a process of `delinearization`
  - _There is a second use case for a linearized repo: it can can be used to inspect the differences between consecutive exercises (using a tools such as `SourceTree` or `GitKraken`)_

_Studentification_, _linearization_, and _delinearization_ as well as some administrative tasks (such as renumbering exercises), is done using the so-called [Course Management Tools](https://github.com/lunatech-labs/course-management-tools).

In the following sections, we'll explain how to `studentify` this course.

### How to _"studentify"_ a master repository

#### Clone the main repo

Clone this repo (aka the main repo for this course) to your computer:

```bash
$ mkdir ~/Courses

$ cd ~/Courses

$ git clone git@github.com:lunatech-labs/lunatech-scala-2-to-scala3-course.git

$ ls -l
drwxr-xr-x@ 17 ericloots  staff  544 11 Mar 08:55 lunatech-scala-2-to-scala3-course

```

#### Create a folder that will hold the studentified repo

Create a folder in which the _studentified_ version will be created. For example:

```bash
$ mkdir ~/tmp/stu
```

#### Generate the studentified repo using `cmta`

Run the `cmta` command to _studentify_ the main repo:

```bash
$ cmta studentify -f -g -m ~/Courses/lunatech-scala-2-to-scala3-course -d ~/tmp/stu

$ cd ~/tmp/stu/lunatech-scala-2-to-scala3-course

$ ls -l
drwxr-xr-x@ 10 ericloots  staff  320 10 Mar 13:23 code
```

The `code` folder holds the exercise source code as an sbt project.

## Where can I find the `cmta` and `cmtc` tools?

These are located in the `CMT-tools` folder in the root of this repo.

Copy them to a suitable folder on your system and make sure that this folder is on
your PATH (for windows users, copy both the `.bat` files too.

### `cmtc` and `cmta` prerequisites

- Your system should have a JDK (>= Java 11) installed.

For `cmta`, you should have a recent version of `git` installed.

