# Summary

This project is midterm project of 2023-1 system programming 1 in Myongji University.
<br><br>
This project consists of the following two subjects : Compiler, MCU.
<br><br>
Compiler is a module that can compile simple high-level language into machine code.
and consists of four modules:<br>

* Preprocessor : remove comments and empty lines from source code.
* Lexer : analysis source code, create a symbol Table by dividing the source code into tokens.
* Parser : based on the production rule, create a parse tree with tokens from the symbol table.
* Generator : traverse the parse tree, generate machine code.
  <br><br>
  MCU is a virtual device that emulate the operation of computing hardware.
  and consists of three modules:<br>
* CPU : fetch, decode, execute instructions from memory
* Memory : store instructions and data
* Loader : load program to Memory