    .data
arr: .space  8008   
space: .asciiz " "
    .text
    .globl main

main:
    li $v0, 5
    syscall
    move $t0, $v0       

    li $v0, 5
    syscall
    move $t1, $v0       

    li $t2, 0           
    la $t3, arr         
    addi $t4, $t1, 1    
    li $t5, 0           

init_loop:
    bge $t2, $t0, loop_end 
    li $v0, 5           
    syscall
    move $t6, $v0       

    sll $t7, $t6, 2     
    add $t7, $t3, $t7   
    lw $t8, 0($t7)      
    addi $t8, $t8, 1    
    sw $t8, 0($t7)      

    addi $t2, $t2, 1    
    j init_loop         

loop_end:
    li $t2, 1           
    la $t3, arr         

print_loop:
    bgt $t2, $t1, end 

    sll $t4, $t2, 2     
    add $t4, $t3, $t4   
    lw $a0, 0($t4)      
    li $v0, 1           
    syscall

    li $v0, 4           
    la $a0, space       
    syscall

    addi $t2, $t2, 1    
    j print_loop        

end:
