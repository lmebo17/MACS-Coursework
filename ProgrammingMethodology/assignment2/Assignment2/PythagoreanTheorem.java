
/*
 * File: PythagoreanTheorem.java
 * Name: 
 * Section Leader: 
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		println("this programm does pythagorian theorem.");
		double x = readDouble(" Enter the first cathetus : ");
		double y = readDouble(" Enter the second cathetus  : ");
		//the next iteration squares x and y , sums them and gets the square root out of their sum 
		double c = Math.sqrt(x * x + y * y);
		println(" The hypotenuse " + c + " . ");
	}
}
