#!/bin/bash

EXTENSION=".o"

./compiler.sh $1 -p
./mcu.sh $1${EXTENSION} -p