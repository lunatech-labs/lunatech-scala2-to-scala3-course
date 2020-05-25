# Moving Forward from Scala 2 to Scala 3

## Description

The target audience for this course is the Scala _Application_ Developer Community. In other words, the focus is on people who use the language to develop 'end-user' applications and not necessarily those developers who design and write libraries, toolkits or frameworks (although they may benefit from what is in this course if they haven't already worked with Dotty).

The goal of the course is to explore the new features in the Scala language brought by [the Dotty project](https://dotty.epfl.ch). Even though it is still early days and the first release candidate of the Scala 3 language is probably not out for another 6 months, it is our believe that one can never start too early to learn Dotty for the following reasons:

- There is a lot to learn, so if you wait until everything is ready, you risk having to play catchup.
- There are many opportunities to contribute to the Dotty project, which will accelerate its development in the coming months. Using Dotty will provide opportunities to identity issues to be fixed sooner rather than later.

## Approach

We start from an existing Scala 2 application: an [[Akka](https://akka.io)] actor based Sudoku solver. We start from the application as-is, but with an sbt build definition that uses the Dotty compiler.

Through a series of exercises, we look at specific Dotty features and apply these to the Scala 2 code, thereby transforming it step-by-step into a version that exploits a whole series of nice features offered by the new language.

Note that this is work in progress, so keep watching this space!

## Usage

This repository is structured in such a way that it can be transformed into two different artifacts for specific use cases:

- A _studentified_ repository: this is a standalone sbt project which is typically used for teaching or learning purposes. A user of this repository will be working on one exercise at any one point in time and will be able to use a number of custom [sbt] commands:
    - `man`: print a manual page for the global project
    - `man e`: print a manual page for the current exercise. In general, this page will contain some background information on the exercise and a series of specific instructions, hints or tips for the current exercise
    - `listExercises`: will print a list of all available exercises
    - `nextExercise`: move to the next exercise and pull in the tests (if any) for that exercise. This command preserves the current state of the application leaving any code added or changed by the student unmodified.
    - `prevExercise`: the opposite of the `nextExercise` command
    - `pullSolution`: pulls in the reference solution code for the current exercise. This command is handy in a class room setting where at some point in time, the instructor wants to move to the next exercise and the student hasn't completed the current exercise. Note that it will overwrite any code written by the student
    - `gotoExercise <exerciseID>`: jump to an exercise specified by the exerciseID. Note that this command support TAB completion for the exerciseID. Also, remember that this will pull in tests for the exercise and probably you'll want to pull in the solution by running the `pullSolution` command.
    - `saveState`: can be executed prior to executing the `pullSolution` command: it will save the current state of an exercise so that it can be restored later.
    - `savedStates`: show the exercise ids of all exercises for which a state was saved
    - `restoreState <exerciseID>`: restore a previously save state
- A _linearized_ repository: this is a git repository in which every exercise in the master repository is turned into a commit:
  - Used to edit the master repository: In general this is a scratch pad repository that is discarded once it has served its purpose. In this use case, interactive rebasing on the linearized repo is used to transform the code across a range of exercises. Once this rebasing is finished, all applied changes can be applied on the master repo by a process of `delinearization`
  - **There is a second use case for a linearized repo: it can can be used to inspect the differences between consecutive exercises (using a tools such as `SourceTree` or `GitKraken`)**

_Studentification_, _linearization_, and _delinearization_ as well as some administrative tasks (such as renumbering exercises), is done using the so-called [Course Management Tools](https://github.com/lightbend/course-management-tools).

In the following sections, we'll explain `studentify`, `linearize` and `delinearize` commands.

### How to _"studentify"_ a master repository

1. Create a folder in which the _studentified_ version will be
- Clone the [Course Management Tools (CMT)](https://github.com/lightbend/course-management-tools)
- Start an `sbt` session in the cloned CMT repo
- From the `sbt` prompt, run the _studentify_ command with the `-dot` option and provide two arguments:
    - The first one is the _absolute_ path to the root folder of the master repository of the exercises
    - The second one is the _absolute_ path of the folder in which the studentified version will be created
- Once the _studentify_ command is finished, you can _cd_ into the studentified versions root folder and start an _sbt_ session. You will now be able to use all the student commands listed in the previous section.

Assuming that:

- The root folder of the cloned course master repo is located at the following [absolute] path: `/home/userx/src/lunatech-scala-2-to-scala3-course`
- The folder that will hold the _studentified_ version has the following [absolute] path: `/home/userx/stu`

`studentify` should be invoked as follows:

```
studentify -dot /home/userx/src/lunatech-scala-2-to-scala3-course /home/userx/stu
```

### How to _"linearize"_/_"delinearize"_ a master repository

In the context of the _"Moving from Scala 2 to Scala 3"_ course, _linearize_ allows one to view the changes between the different exercises. In other words, this feature allows one to see exactly what needs to be done to _"solve"_ the exercise. So, if you want to actually learn something, don't use this feature — instead, give the exercises a try first...

In a broader context, _"linearize"_/_"delinearize"_ is an important tool to evolve/change the master repository of this course.

The following picture shows the typical process to evolve code in a master repository with the utilisation of a _linearized_ repository:

![Linearization/Delinearization](images/Lin-Delin.png)

Follow the following steps:

1. Choose a folder in which the _linearized_ version will be created
- Clone the [Course Management Tools (CMT)](https://github.com/lightbend/course-management-tools)
- Start an `sbt` session in the cloned CMT repo
- Run the _linearize_ command and provide two arguments:
    - The first one is the _absolute_ path to the root folder of the master repository of the exercises
    - The second one is the absolute path of the folder in which the _linearized_ version will be created (see 1.)
- Once the _linearize_ command is finished, _cd_ into the _linearized_ version's root folder and start refactoring your code using interactive rebasing (e.g. `git rebase -i --root`).
- To apply all changes made in the _linearized_ repo to the master repository, execute the `delinearize` command supplying two arguments:
  - The first one is the _absolute_ path to the root folder of the master repository of the exercises
  - The second one is the absolute path of the `linearized` project folder
- Repeat the _interactive rebasing_ / _delinearization_ sequence as many times as you want

Assuming that:

- The root folder of the cloned course master repo is located at the following [absolute] path: `/home/userx/src/lunatech-scala-2-to-scala3-course`
- The folder that will hold the _linearized_ version has the following [absolute] path: `/home/userx/lin`

`linearize` should be invoked as follows:

```
linearize -dot /home/userx/src/lunatech-scala-2-to-scala3-course /home/userx/stu
```

With the same assumptions, _delinearizing_ the linearized repo is done with the following command:

```
delinearize /home/userx/src/lunatech-scala-2-to-scala3-course /home/userx/stu/lunatech-scala-2-to-scala3-course
```
Note two changes on this command compared to the `linearize` one: the `-dot` option is not present and the second argument is changed.

> NOTES
> 
> - During the process of interactive rebasing, make sure NOT to change the commit message (which in fact is the exercise's project name) or to squash commits as this will make it impossible to _delinearize_ the project.
> - Editing code directly in the _master repo_ and editing code via _linearize_/_delinearize_ are mutually exclusive processes. In practice, this means that when you've been using `linearize`/`delinearize` and you decide to make a change directly to files in the exercises in the master repository, you should discard the `linearized` artifact.
> - Project `common` is aggregated in all exercises. As such, it is the same across _all_ exercises. Also, the _Linearization_/_Delinearization_ process will not transfer changes made in the _linearized_ repository
> - `studentify` and `linearize` mandate that your master repository has no files in your git _index_ or _workspace_, so, commit any work before using these commands. Don't worry about commiting stuff temporarily. Either stash any pending changes or commit them and possibly squash them later.
> Finally: reflect carefully about the best editing approach. In many cases, direct edits in the master repository may be simpler to apply than going through an interactive rebasing session.
