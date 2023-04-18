# Summary

This project is midterm project of 2023-1 system programming 1 in Myongji University.
<br><br>
This project consists of the following two subjects : Compiler, MCU.
<br><br>
Compiler is a module that can compile simple high-level language into machine code.<br>
and consists of four modules:<br>

* Preprocessor : remove comments and empty lines from source code.
* Lexer : analysis source code, create a symbol Table by dividing the source code into tokens.
* Parser : based on the production rule, create a parse tree with tokens from the symbol table.
* Generator : traverse the parse tree, generate machine code.
  <br><br>
  MCU is a virtual device that emulate the operation of computing hardware.<br>
  and consists of three modules:<br>
* CPU : fetch, decode, execute instructions from memory
* Memory : store instructions and data
* Loader : load program to Memory

# MCU Manual

<b>NAME</b>
MCU Emulator for 2023-1 System Programming Midterm Assignment<br>
<br>
DESCRIPTION<br>
Author       : Junseo Ho<br>
Organization : Myongji University<br>
Modified     : 2023-04-16<br>
<br>
    This program emulates the operation of a simple CPU.<br>
    The instruction is based on 32 bits, and from the front, 2 bits represent the addressing mode,<br>
    4 bits represent the opcode, and 26 bits represent the operand.<br>
<br>
OPTIONS<br>
-help<br>
Print this help document<br>
-p<br>
Print executed instructions<br>
-d<br>
Execute one instruction each time input is received from standard input<br>
<br>
ADDRESSING_MODE<br>
Bin     Hex     Description<br>
----------------------------<br>
00      0x00    immediate<br>
01      0x01    direct<br>
11      0x10    indirect<br>
<br>
OPCODE<br>
Bin     Hex     Mnemonic    Description<br>
--------------------------------------------------------------------------------------------------<br>
0000    0x00    hlt         terminate program<br>
0001    0x01    prt         print operand to standard output<br>
0010    0x02    sto         move value from AC to operand<br>
0011    0x03    lda         move value from operand to AC<br>
0100    0x04    add         add the operand to AC, store the result value in AC<br>
0101    0x05    sub         subtract the operand from AC, store the result value in AC<br>
0110    0x06    mul         multiply the operand by AC, store the result value in AC<br>
0111    0x07    div         divide AC by the operand, store the quotient value in AC<br>
1000    0x08    jmp         store operand in PC<br>
1001    0x09    jpz         if zero flag is true, store operand in PC<br>
1010    0x0A    jpn         if negative flag is true, store operand in PC<br>
1011    0x0B    jpp         if zero flag and negative flag are false, store operand in PC<br>
1100    0x0C    and         operate bitwise-and with AC and operand, store the result value in AC<br>
1101    0x0D    or          operate bitwise-or with AC and operand, store the result value in AC<br>
1110    0x0E    not         invert all bits in AC<br>
1111    0x0F    xor         operate bitwise-xor with AC and operand, store the result value in AC<br>

