.data
key:      .asciiz "PgEfTYaWGHjDAmxQqFLRpCJBownyUKZXkbvzIdshurMilNSVOtec#@_!=.+-*/"
original: .asciiz "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
str:      .space 1000001    
input_prompt: .asciiz "p: "
string_prompt: .asciiz "string: "

.text
.globl main
main:
    li $v0, 4
    la $a0, input_prompt
    syscall

    li $v0, 5
    syscall
    move $t0, $v0

    li $v0, 4
    la $a0, string_prompt
    syscall

    li $v0, 8
    la $a0, str
    li $a1, 100
    syscall

    li $t1, 0
loop:
    lb $t2, str($t1)
    beqz $t2, end_loop
    beq $t0, 1, encrypt
    bne $t0, 1, decrypt
    j next_char

encrypt:
    la $t3, original
    li $t5, 0
find_index_loop:
    lb $t4, 0($t3)
    beqz $t4, found_index
    beq $t2, $t4, found_index
    addi $t3, $t3, 1
    addi $t5, $t5, 1
    j find_index_loop

found_index:
    la $t6, key
    add $t6, $t6, $t5
    lb $t7, 0($t6)
    sb $t7, str($t1)
    addi $t1, $t1, 1
    j loop

decrypt:
    la $t3, key
    li $t5, 0
find_index_loop_decrypt:
    lb $t4, 0($t3)
    beqz $t4, found_index_decrypt
    beq $t2, $t4, found_index_decrypt
    addi $t3, $t3, 1
    addi $t5, $t5, 1
    j find_index_loop_decrypt

found_index_decrypt:
    la $t6, original
    add $t6, $t6, $t5
    lb $t7, 0($t6)
    sb $t7, str($t1)
    addi $t1, $t1, 1
    j loop

next_char:
    j loop

end_loop:
    li $v0, 4
    la $a0, str
    syscall

    li $v0, 10
    syscall
