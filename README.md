caos.g8: a Giter8 template for using the Caos framework
=================

A Giter8 template for a minimal example that uses the [Caos framework](https://github.com/arcalab/CAOS).

Note that an older Giter8 exists, based on a more complex example and targetting an older version of Caos, which can be found in https://github.com/arcalab/caos-choreo.g8


Requirements
------------

* Scala building tools ([sbt](https://www.scala-sbt.org))
* Java Runtime Environment ([JRE](https://www.java.com/en/download/)) (>1.8)

Template usage
--------------
Using `sbt`, do:
```
sbt new arcalab/caos.g8
```
in the folder where you want to create the template. The result will be a project ready to be compiled with `sbt fastLinkJS`, with a snapshot of the Caos libraries. We recommend later replacing the folder `lib/caos` with a clone of the Caos library, possibly as a submodule.

This template uses as an example a simplified CCS language (Calculus of Communicating Systems), mainly based on the project https://github.com/arcalab/ccs-caos.