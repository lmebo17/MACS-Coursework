@R0
D=M

@R1
A=M

@ZERO
M=0

@R2
M=0

(LOOP)
    @R1
    D=M

    @END
    D;JEQ
    
    @R2
    D=M
    
    @R0
    D=D+M
    
    @R2
    M=D
    
    @R1
    M=M-1
@LOOP
0;JMP
(END)
