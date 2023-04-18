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

**NAME**<br>
&nbsp;&nbsp;&nbsp;&nbsp;MCU Emulator for 2023-1 System Programming Midterm Assignment<br>
<br>
**DESCRIPTION**<br>
&nbsp;&nbsp;&nbsp;&nbsp;Author       : Junseo Ho<br>
&nbsp;&nbsp;&nbsp;&nbsp;Organization : Myongji University<br>
&nbsp;&nbsp;&nbsp;&nbsp;Modified     : 2023-04-16<br>
<br>
&nbsp;&nbsp;&nbsp;&nbsp;This program emulates the operation of a simple CPU.<br>
&nbsp;&nbsp;&nbsp;&nbsp;The instruction is based on 32 bits, and from the front, 2 bits represent the addressing mode,<br>
&nbsp;&nbsp;&nbsp;&nbsp;4 bits represent the opcode, and 26 bits represent the operand.<br>
<br>
**OPTIONS**<br>
&nbsp;&nbsp;&nbsp;&nbsp;-help<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Print this help document<br>
&nbsp;&nbsp;&nbsp;&nbsp;-p<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Print executed instructions<br>
&nbsp;&nbsp;&nbsp;&nbsp;-d<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Execute one instruction each time input is received from standard input<br>
<br>
**ADDRESSING_MODE**<br>
&nbsp;&nbsp;&nbsp;&nbsp;Bin     Hex     Description<br>
&nbsp;&nbsp;&nbsp;&nbsp;----------------------------<br>
&nbsp;&nbsp;&nbsp;&nbsp;00&nbsp;&nbsp;&nbsp;&nbsp;0x00&nbsp;&nbsp;&nbsp;&nbsp;immediate<br>
&nbsp;&nbsp;&nbsp;&nbsp;01&nbsp;&nbsp;&nbsp;&nbsp;0x01&nbsp;&nbsp;&nbsp;&nbsp;direct<br>
&nbsp;&nbsp;&nbsp;&nbsp;11&nbsp;&nbsp;&nbsp;&nbsp;0x10&nbsp;&nbsp;&nbsp;&nbsp;indirect<br>
<br>
**OPCODE**<br>
&nbsp;&nbsp;&nbsp;&nbsp;Bin     Hex     Mnemonic    Description<br>
&nbsp;&nbsp;&nbsp;&nbsp;--------------------------------------------------------------------------------------------------<br>
&nbsp;&nbsp;&nbsp;&nbsp;0000&nbsp;&nbsp;&nbsp;&nbsp;0x00    hlt         terminate program<br>
&nbsp;&nbsp;&nbsp;&nbsp;0001&nbsp;&nbsp;&nbsp;&nbsp;0x01    prt         print operand to standard output<br>
&nbsp;&nbsp;&nbsp;&nbsp;0010&nbsp;&nbsp;&nbsp;&nbsp;0x02    sto         move value from AC to operand<br>
&nbsp;&nbsp;&nbsp;&nbsp;0011&nbsp;&nbsp;&nbsp;&nbsp;0x03    lda         move value from operand to AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;0100&nbsp;&nbsp;&nbsp;&nbsp;0x04    add         add the operand to AC, store the result value in AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;0101&nbsp;&nbsp;&nbsp;&nbsp;0x05    sub         subtract the operand from AC, store the result value in AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;0110&nbsp;&nbsp;&nbsp;&nbsp;0x06    mul         multiply the operand by AC, store the result value in AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;0111&nbsp;&nbsp;&nbsp;&nbsp;0x07    div         divide AC by the operand, store the quotient value in AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1000&nbsp;&nbsp;&nbsp;&nbsp;0x08    jmp         store operand in PC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1001&nbsp;&nbsp;&nbsp;&nbsp;0x09    jpz         if zero flag is true, store operand in PC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1010&nbsp;&nbsp;&nbsp;&nbsp;0x0A    jpn         if negative flag is true, store operand in PC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1011&nbsp;&nbsp;&nbsp;&nbsp;0x0B    jpp         if zero flag and negative flag are false, store operand in PC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1100&nbsp;&nbsp;&nbsp;&nbsp;0x0C    and         operate bitwise-and with AC and operand, store the result value in AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1101&nbsp;&nbsp;&nbsp;&nbsp;0x0D    or          operate bitwise-or with AC and operand, store the result value in AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1110&nbsp;&nbsp;&nbsp;&nbsp;0x0E    not         invert all bits in AC<br>
&nbsp;&nbsp;&nbsp;&nbsp;1111&nbsp;&nbsp;&nbsp;&nbsp;0x0F    xor         operate bitwise-xor with AC and operand, store the result value in AC<br>

