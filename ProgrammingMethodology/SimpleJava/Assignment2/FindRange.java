
/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	public void run() {
		println("This program finds the largest and smallest numbers");
		final int SENTINEL = 0;
		int n = readInt();
		int x = n;
		int y = n;
		if (n == SENTINEL) {
			// if person enters the prohibited number on the first iteration , then
			// following words pop up and code ends.
			println("Sorry , can't be computed ");
		} // if the first number is not SENTINEL , then a person keeps entering numbers
			// until he/she enters SENTINEL
		else {
			while (true) {
				n = readInt();
				if (n == SENTINEL)
					break;
				// initially y=n and then when a person enters a new number if y>n y becomes n
				// and this helps us to minimise y.
				if (y > n) {
					y = n;
				}
				// initially x=n and then when a person enters a new number if x<n , x becomes n
				// and this helps us to maximise x.
				if (x < n)
					x = n;
			}
		}
		if (x != SENTINEL && y != SENTINEL) {
			println("smallest:" + y);
			println("largest:" + x);
		}

	}
}
