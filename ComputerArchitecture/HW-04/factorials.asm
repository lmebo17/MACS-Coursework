.data
endl:    .asciiz "\n"
ln: .float 2.30258509299
zero: .float 0.0
one:  .float 1.0
two:  .float 2.0
prompt0: .asciiz "Enter N: "
prompt1: .asciiz "Number of digits of "
prompt2: .asciiz "! is "
newline: .asciiz "\n"

.text
.globl main

main:
    li $v0, 4                 
    la $a0, prompt0     
    syscall		

    li $v0, 5
    syscall
    move $t0, $v0
    
    li $v0, 4                 
    la $a0, prompt1         
    syscall
    
    move $a0, $t0        
    li $v0, 1                  
    syscall 
    
    li $v0, 4                 
    la $a0, prompt2         
    syscall
    
    l.s $f0, zero  	
    li $t1, 2   
        	
    loop: 		
  	bgt $t1, $t0, end_loop 
  	
    mtc1 $t1, $f1	
    cvt.s.w $f1, $f1
    l.s $f2, one
    sub.s $f3, $f1, $f2
    add.s $f4, $f1, $f2
    div.s $f1, $f3, $f4
    mul.s $f2, $f1, $f1
    l.s $f3, zero
    l.s $f4, one
    
    li $t2, 0
    li $t3, 100
    
    inner_loop: 	
    	bge $t2, $t3, end_inner_loop
    		
	div.s $f5, $f1, $f4
	add.s $f3, $f3, $f5     
	mul.s $f1, $f1, $f2
	l.s $f5, two
	add.s $f4, $f4, $f5

	addi $t2, $t2, 1
	j inner_loop
	
    end_inner_loop:
    
    l.s $f1, two
    mul.s $f3, $f3, $f1
    l.s $f2, ln
    div.s $f1, $f3, $f2

    add.s $f0, $f0, $f1

    addi $t1, $t1, 1
    j loop
    
end_loop:
    
    cvt.w.s $f0, $f0    
    mfc1 $t0, $f0 
    addi $t0, $t0, 1
    
    li $v0, 1
    move $a0, $t0
    syscall		
    
    li $v0, 4
    la $a0, endl
    syscall

    li $v0, 10
    syscall

