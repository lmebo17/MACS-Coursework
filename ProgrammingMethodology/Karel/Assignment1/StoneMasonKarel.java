
/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {
	public void run() {
		while (frontIsClear()) {
			fillRow();
			goFourStepsForward();
		}
		fillRow();

	}

	private void fillRow() {
		// karel is on the first horizontal line and puts the beepers on a an
		// appropriate vertical line
		// if there does not exist one
		turnLeft();
		while (frontIsClear()) {
			if (noBeepersPresent()) {
				putBeeper();
			} else {
				move();
			}
		}
		if (noBeepersPresent()) {
			putBeeper();
		}
		goBack();
	}

	private void goBack() {
		// karel goes from the top of the column to the first horizontal line and then
		// faces east
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnLeft();
	}

	private void goFourStepsForward() {
		// karel moves to the next appropriate column if there exists one
		move();
		move();
		move();
		move();
	}
}
