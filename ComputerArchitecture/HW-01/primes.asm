.data
N:       .asciiz "N: "
M:       .asciiz "M: "
space:   .asciiz " "
newline: .asciiz "\n"

.text
.globl main

main:
    li $v0, 4
    la $a0, N
    syscall

    li $v0, 5
    syscall
    move $t0, $v0

    li $v0, 4
    la $a0, M
    syscall

    li $v0, 5
    syscall
    move $t1, $v0

    loop:
        move $t2, $t0

        ble $t2, 1, not_prime

        li $t3, 2
        li $t4, 0

        div_loop:
            beq $t3, $t2, print_prime
            div $t2, $t3
            mfhi $t5
            beq $t5, $zero, not_prime

            addi $t3, $t3, 1
            j div_loop

        print_prime:
            li $v0, 1
            move $a0, $t0
            syscall
            li $v0, 4
            la $a0, space
            syscall

        not_prime:

        addi $t0, $t0, 1
        ble $t0, $t1, loop
        j end_loop

    end_loop:
        li $v0, 10
        syscall