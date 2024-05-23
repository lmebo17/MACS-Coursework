(START)
    @SCREEN
    D=A
    @ADDRESS
    M=D

    @8192
    D=A
    @SIZE
    M=D

    @0
    D=A
    @INDEX
    M=D

(PIXEL_LOOP)
    @INDEX
    D=M
    @SIZE
    D=D-M
    @END_LOOP
    D;JEQ

    @KBD
    D=M
    @KEY
    D;JEQ

    @ADDRESS
    A=M
    M=-1
    @CONTINUE
    0;JMP

(KEY)
    @ADDRESS
    A=M
    M=0

(CONTINUE)
    @ADDRESS
    M=M+1

    @INDEX
    M=M+1

    @PIXEL_LOOP
    0;JMP

(END_LOOP)
    @START
    0;JMP

