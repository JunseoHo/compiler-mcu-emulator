#!/bin/bash
cd compiler/src/
javac main/Main.java
java main/Main $@
rm */*.class
