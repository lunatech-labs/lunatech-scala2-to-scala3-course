# Moving Forward from Scala 2 to Scala 3

## Description

The target audience for this course is the Scala _Application_ Developer Community. In other words, the focus is on people who use the language to develop 'end-user' applications and not necessarily those developers who design and write libraries, toolkits or frameworks (although they may benefit from what is in this course if they haven't already worked with Scala 3).

The goal of the course is to explore the new features in the Scala language brought by [the Scala 3 project](https://dotty.epfl.ch).

## Approach

We start from an existing Scala 2 application: an [[Akka](https://akka.io)] actor based Sudoku solver. We start from the application as-is, but with an sbt build definition that uses the Scala 3 compiler.

Through a series of exercises, we look at specific Scala 3 features and apply these to the Scala 2 code, thereby transforming it step-by-step into a version that exploits a whole series of nice features offered by the new language.

Note that this course gets updated on regular basis, so keep watching this space!


## Course installation

Some prerequisites need to be met in order to install the course. The following components
need to be installed:

* Coursier
* A Java JDK (JDK 11 or 17). Note that Coursier allows you to install a JDK
* The `cmtc` Course Management Tools Client CLI
* An IDE

The following sections detail how these components.

### Install Coursier

The course can be installed using [Coursier](https://github.com/coursier/coursier/).

Coursier will, as part of its base installation, install a number of useful applications among which:

* `sbt` - the Scala Build Tool
* `scala-cli` - the Scala interactive toolkit
* `scala` - the Scala REPL
* `scalac` - the Scala compiler
* `java` - JDK 8 by default (we will need to install a more recent version of the JDK)

Coursier, once installed, will make it extremely easy to switch between different versions of the
installed tools (such as the Scala compiler and REPL).

**If you haven't installed Coursier, install it first by following the [Install Scala on your computer](https://docs.scala-lang.org/getting-started/index.html#install-scala-on-your-computer) instructions.**

### Install a JDK

If you haven't installed a JDK yet (version 11 or 17), you can do this in the following manner.
In a terminal on your system, issue the following command to install JDK 11:

```bash
$ cs java --jvm temurin:1.11 --setup
Checking if ~/.profile, ~/.zprofile, ~/.bash_profile need(s) updating.
Some shell configuration files were updated. It is recommended to close this terminal once the setup command is done, and open a new one for the changes to be taken into account
```

Close the current terminal and start a new one. Verify that the JDK is installed correctly:

```bash
$ java -version
openjdk version "11.0.20" 2023-07-18
OpenJDK Runtime Environment Temurin-11.0.20+8 (build 11.0.20+8)
OpenJDK 64-Bit Server VM Temurin-11.0.20+8 (build 11.0.20+8, mixed mode)
```

### Install `cmtc`

During the course, you will use the `cmtc` command, the **C**ourse **M**anagement **T**ools **C**lient CLI.

Install it using Coursier from the command line (all Linux, Mac, or Windows platforms) as follows:

```
$ cs install --contrib cmtc
Wrote cmtc
```

Verify that the installation was successful by running the `cmtc` command. You should see the following output:

```bash
$ cmtc
Usage: cmtc <COMMAND>

Commands:
  goto-exercise        Move to a given exercise. Pull in tests and readme files for that exercise
  goto-first-exercise  Move to the first exercise. Pull in tests and readme files for that exercise
  install              Install a course - from either a local directory, a zip file on the local file system or a Github project
  list-exercises       List all exercises and their IDs in the repo. Mark the active exercise with a star
  list-saved-states    List all saved exercise states, if any.
  next-exercise        Move to the next exercise. Pull in tests and readme files for that exercise
  previous-exercise    Move to the previous exercise. Pull in tests and readme files for that exercise
  pull-solution        Pull in all code for the active exercise. All local changes are discarded
  pull-template        Selectively pull in a given file or folder for the active exercise
  restore-state        Restore a previously saved exercise state
  save-state           Save the state of the active exercise
  set-current-course   Sets the current course to point to a directory
  version              Print version info
```
  
### Install the course

Installing this course can be done via the `cmtc install` command as follows:

```bash
$ cmtc install -f -s lunatech-labs/lunatech-scala2-to-scala3-course
Downloading studentified course from 'https://github.com/lunatech-labs/lunatech-scala2-to-scala3-course/releases/download/3.0.2/lunatech-scala2-to-scala3-course-student.zip' to courses directory

Project lunatech-scala2-to-scala3-course (3.0.2) successfully installed to:
  /Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course

Current course set to '/Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course'

Exercises in repository:
  1.  *   exercise_000_sudoku_solver_initial_state
  2.      exercise_001_dotty_deprecated_syntax_rewriting
  3.      exercise_002_dotty_new_syntax_and_indentation_based_syntax
  4.      exercise_003_top_level_definitions
  5.      exercise_004_parameter_untupling
  6.      exercise_005_extension_methods
  7.      exercise_006_using_and_summon
  8.      exercise_007_givens
  9.      exercise_008_enum_and_export
 10.      exercise_009_union_types
 11.      exercise_010_opaque_type_aliases
 12.      exercise_011_multiversal_equality
 13.      exercise_020_opaque_type_aliases_alt
 14.      exercise_021_multiversal_equality
```

As can be seen in the above output, `cmtc` installed the course in the `/Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course` folder. Note that this location is OS specific, so, take a note of the location.

#### Verifying the course installation

Verify that the the course was installed correctly by launching `sbt test` in the root folder of the
course exercises. You should see the following output:

```bash
$ sbt test
[info] welcome to sbt 1.9.3 (Eclipse Adoptium Java 11.0.20)
[info] loading settings for project global-plugins from idea.sbt ...
[info] loading global plugins from /Users/ericloots/.sbt/1.0/plugins
[info] loading settings for project lunatech-scala2-to-scala3-course-build from plugins.sbt ...
[info] loading project definition from /Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course/project
[info] compiling 1 Scala source to /Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course/project/target/scala-2.12/sbt-1.0/classes ...
[info] loading settings for project moving-from-scala-2-to-scala-3 from build.sbt ...
[info] set current project to moving-from-scala-2-to-scala-3 (in build file:/Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course/)
[info] compiling 11 Scala sources to /Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course/target/scala-2.13/classes ...
[warn] /Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course/src/main/scala/org/lunatechlabs/dotty/sudoku/SudokuProblemSender.scala:81:44: Auto-application to `()` is deprecated. Supply the empty argument list `()` explicitly to invoke method next,
[warn] or remove the empty argument list from its definition (Java-defined methods are exempt).
[warn] In Scala 3, an unapplied method like this will be eta-expanded into a function.
[warn]         val nextRowUpdates = rowUpdatesSeq.next
[warn]                                            ^
[warn] one warning found
[info] compiling 4 Scala sources to /Users/ericloots/Library/Caches/com.lunatech.cmt/Courses/lunatech-scala2-to-scala3-course/target/scala-2.13/test-classes ...
org.lunatechlabs.dotty.sudoku.CellMappingSuite:
org.lunatechlabs.dotty.sudoku.ReductionRuleSuite:
  + Mapping row coordinates should result in correct column & block coordinates 0.02s
  + Mapping column coordinates should result in correct row & block coordinates 0.001s
  + Mapping block coordinates should result in correct row & column coordinates 0.001s
  + Applying reduction rules should eliminate values in isolated complete sets from occurrences in other cells (First reduction rule) 0.045s
  + Applying reduction rules should eliminate values in isolated complete sets of 5 values from occurrences in other cells (First reduction rule) 0.001s
  + Applying reduction rules should eliminate values in 2 isolated complete sets of 3 values from occurrences in other cells (First reduction rule) 0.001s
  + Applying reduction rules should eliminate values in shadowed complete sets from occurrences in same cells (Second reduction rule) 0.001s
  + Applying reduction rules should eliminate values in shadowed complete (6 value) sets from occurrences in same cells (Second reduction rule) 0.001s
SLF4J: A number (1) of logging calls during the initialization phase have been intercepted and are
SLF4J: now being replayed. These are subject to the filtering rules of the underlying logging system.
SLF4J: See also http://www.slf4j.org/codes.html#replay
org.lunatechlabs.dotty.sudoku.SudokuDetailProcessorSuite:
  + Sending no updates to a sudoku detail processor should result in sending a SudokuDetailUnchanged messsage 0.007s
  + Sending an update to a fresh instance of the SudokuDetailProcessor that sets one cell to a single value should result in sending an update that reflects this update 0.002s
  + Sending a series of subsequent Updates to a SudokuDetailProcessor should result in sending updates and ultimately return no changes 0.003s
[info] Passed: Total 11, Failed 0, Errors 0, Passed 11
[success] Total time: 5 s, completed 24 Aug 2023, 08:30:54
```

Observe that all (11) tests passed successfully.

### Installing an IDE

Instructions are provided in the `IDE-Setup.md` file in the root folder of the installed
course.

## A note to course authors & contributors

For starters, install the `cmta` command as follows:

```bash
$ cs install --contrib cmta
Wrote cmta
```

Using the `cmta` command, two artefacts can be generated from this repo:

- a so-called `studentified` repo. This artefact is distributed to course students.
- a `linearized` repo. This is git repository in which every exercise in the
master repository is turned into a commit. It can be used to:
  - edit the master repository: In general this is a scratch pad repository that
    is discarded once it has served its purpose. In this use case, interactive rebasing
    on the linearized repo is used to transform the code across a range of exercises.
    Once this rebasing is finished, all applied changes can be applied on the master
    repo by a process of `delinearization`
  - to inspect the differences between consecutive exercises (using a tools such as `SourceTree`)

### How to _"studentify"_ a master repository

#### Clone the main repo

Clone this repo (aka the main repo for this course) to your computer:

```bash
$ mkdir ~/Courses

$ cd ~/Courses

$ git clone git@github.com:lunatech-labs/lunatech-scala2-to-scala3-course.git

$ ls -l
drwxr-xr-x@ 17 ericloots  staff  544 11 Mar 08:55 lunatech-scala2-to-scala3-course

```

#### Create a folder that will hold the studentified repo

Create a folder in which the _studentified_ version will be created. For example:

```bash
$ mkdir ~/tmp/stu
```

#### Generate the studentified repo using `cmta`

Run the `cmta` command to _studentify_ the main repo:

```bash
$ cmta studentify -f -g -m ~/Courses/lunatech-scala2-to-scala3-course -d ~/tmp/stu
Studentifying lunatech-scala2-to-scala3-course to /Users/ericloots/tmp/stu/lunatech-scala2-to-scala3-course
..............
Processed exercises:
  exercise_000_sudoku_solver_initial_state
  exercise_001_dotty_deprecated_syntax_rewriting
  exercise_002_dotty_new_syntax_and_indentation_based_syntax
  exercise_003_top_level_definitions
  exercise_004_parameter_untupling
  exercise_005_extension_methods
  exercise_006_using_and_summon
  exercise_007_givens
  exercise_008_enum_and_export
  exercise_009_union_types
  exercise_010_opaque_type_aliases
  exercise_011_multiversal_equality
  exercise_020_opaque_type_aliases_alt
  exercise_021_multiversal_equality

$ cd ~/tmp/stu/lunatech-scala2-to-scala3-course

$ ls -l ~/tmp/stu/lunatech-scala2-to-scala3-course/
total 32
-rw-r--r--@ 1 ericloots  staff  1305 21 Aug 16:54 IDE_setup.md
-rw-r--r--@ 1 ericloots  staff  7254 21 Aug 16:54 README.md
-rw-r--r--@ 1 ericloots  staff   429 21 Aug 16:54 build.sbt
drwxr-xr-x@ 3 ericloots  staff    96 21 Aug 16:54 images
drwxr-xr-x@ 5 ericloots  staff   160 21 Aug 16:54 project
drwxr-xr-x@ 4 ericloots  staff   128 21 Aug 16:54 src
drwxr-xr-x@ 3 ericloots  staff    96 21 Aug 16:54 sudokus
```

