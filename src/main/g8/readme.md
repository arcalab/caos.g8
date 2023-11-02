# Animator of $name$

Simple example of an animator of a DSL, meant to bootstrap animators and analysers of other DSLs using the Caos framework.

An extended version of this language, based on CCS, can be found in http://lmf.di.uminho.pt/ccs-caos.

# Caos

This project uses and the Caos's framework, placed at `lib/caos`. More information on it can be found online:

 - Caos' GitHub page: https://github.com/arcalab/CAOS
 - Caos' tutorial: https://arxiv.org/abs/2304.14901
 - Caos' demo video: https://youtu.be/Xcfn3zqpubw 

The project can also be included as a submodule, as explained in the documentation of Caos.

## Requirements

- JVM (>=1.8)
- sbt

## Compilation

You need to compile this project using the ScalaJS plug-in, following the steps below.
The result will be a JavaScript file that is already being imported by an existing HTML file. 

1. `sbt fastLinkJS`
2. open the file `lib/tool/index.html`
