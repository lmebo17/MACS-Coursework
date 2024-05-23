
/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {
		int countProcess = 0;
		int n = readInt("Enter a number: ");
		while (n != 1) {
			// if n can be divided by two , the program divides the number by two and prints the result
			if (n % 2 == 0) {
				println(n + " is even , so I take half : " + (n / 2));
				n = n / 2;
			} //if n can't be divided by two , the program multiplies the number by 3 and then adds one
			else {
				println(n + " is odd , so I make 3n+1 : " + (3 * n + 1));
				n = 3 * n + 1;
			}
			//countProcess counts how many steps it took to get to one
			countProcess+=1;
		}
		println("The process took " + countProcess + " to reach 1 ");
	}
}
