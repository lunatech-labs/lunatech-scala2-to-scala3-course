## A note to course authors & contributors

As a course author or maintainer, you will need to have `cmta`, the CMT
administrator CLI, installed on your system.

`cmta` can be installed via Coursier as follows:

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
$ cmta studentify -f -m . -d ~/tmp/stu
Studentifying lunatech-scala2-to-scala3-course to /Users/ericloots/tmp/stu/lunatech-scala2-to-scala3-course
...............
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
  exercise_010_exploring_opaque_type_aliases
  exercise_011_optional_opaque_type_aliases
  exercise_012_multiversal_equality
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

### How to _"linearize"_ a master repository

From a main CMT repository, a so-called "linearized" repository can created. The
latter is a git repo, where each commit corresponds to the exercise code in
the main CMT repository.

A linearized repo can be used for at least to purposes:

1. Exercises can be modified using interactive rebasing. The modifications can
   then be reflected back in the main CMT repository by "delinearization", the
   inverse process of "linearization".
2. Inspect code changes between consecutive exercises.

#### Generate the linearized repo using `cmta`

Run the `cmta` command to _linearized_ the main repo:

```bash
$ cmta linearize -f -m ~/Lunatech/Courses/cmt2.0/lunatech-scala2-to-scala3-course -d ~/tmp/lin
Linearizing lunatech-scala2-to-scala3-course to /Users/ericloots/tmp/lin/lunatech-scala2-to-scala3-course
...............
Successfully linearized /Users/ericloots/Lunatech/Courses/cmt2.0/lunatech-scala2-to-scala3-course
```

Now we can have a look at the linearized repository:

```bash
$ cd /Users/ericloots/tmp/lin/lunatech-scala2-to-scala3-course

$ git log --oneline
71f5989 (HEAD -> main) exercise_021_multiversal_equality
896e6b9 exercise_020_opaque_type_aliases_alt
105be7d exercise_012_multiversal_equality
9e8c117 exercise_011_optional_opaque_type_aliases
5edbcb9 exercise_010_exploring_opaque_type_aliases
913bc7d exercise_009_union_types
45f22bd exercise_008_enum_and_export
2603fc4 exercise_007_givens
cd8306a exercise_006_using_and_summon
d1c6a66 exercise_005_extension_methods
30d471e exercise_004_parameter_untupling
47ca6f2 exercise_003_top_level_definitions
2c8b691 exercise_002_dotty_new_syntax_and_indentation_based_syntax
499cbd8 exercise_001_dotty_deprecated_syntax_rewriting
e0ff2b4 exercise_000_sudoku_solver_initial_state
```
