# Moving Forward from Scala 2 to Scala 3

## Description

The target audience for this course is the Scala _Application_ Developer Community.
In other words, the focus is on people who use the language to develop 'end-user'
applications and not necessarily those developers who design and write libraries,
toolkits or frameworks (although they may benefit from what is in this course if
they haven't already worked with Dotty).

The goal of the course is to explore the new features in the Scala language brought
by [the Dotty project](https://dotty.epfl.ch). Even though it is still early days
and the first release candidate of the Scala 3 language is probably not out for
another 6 months, it is our believe that one can never start too early to learn
Dotty for the following reasons:

- There is a lot to learn, so if you wait until everything is ready, you risk having to play catchup.
- There are many opportunities to contribute to the Dotty project, which will accelerate its development in the coming months. Using Dotty will provide opportunities to identity issues to be fixed sooner rather than later.

## The exercises master repository

This repository, located in the `exercises` subfolder, contains all exercises in a multi-project _sbt_ build. In order to run exercises directly from this repo, _cd_ into this folder and run _sbt_. Alternatively, import the build.sbt file in your favourite development environment (Metals with a client of your choice or IntelliJ).

## The snippets repository

This repository, located in the `code-snippets` folder contains small examples, built mainly for demo purposes (and for inclusion in course slides).

## Course Artifacts

Various artifacts are generated for this course:

- The exercises in _studentified_ format: this is the format that is tuned for learning purposes.
- The exercises in _linearized_ format: useful to study the changes between consecutive exercises.
- The course slide deck in PDF format.

If you want to have a concise overview on how to utilize the _studentified_ and the _linearized_ artifacts, there's a video on this topic what you can find [here](https://youtu.be/2zmXTGG7Nkg).

You can find these artifacts in [the releases](https://github.com/lunatech-labs/lunatech-scala-2-to-scala3-course/releases) of this project.