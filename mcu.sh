#!/bin/bash
cd mcu/src/
javac main/Main.java
java main/Main $@
rm */*.class
