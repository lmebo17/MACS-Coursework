
/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {
	public void run() {
		fillRow();
		goToTheLastSquare();
		findMidPoint();

	}

	private void fillRow() {
		// fills the first horizontal line with beepers
		putBeeper();
		while (frontIsClear()) {
			move();
			putBeeper();
		}

	}

	private void goToTheLastSquare() {
		while (frontIsClear()) {
			move();
		}
		turnAround();
	}

	private void findMidPoint() {
		// karel subtracts one beeper from the beginning and from the end until the only
		// beeper is left on the one of the mid vertical line
		while (beepersPresent()) {
			if (frontIsClear()) {
				move();
				if (beepersPresent()) {
					turnAround();
					move();
					turnAround();
					pickBeeper();
				} else {
					turnAround();
					move();
					//karel is on one of the mid points of the first horizontal line
					turnLeft();

				}
				goToTheLastSquare();	
				goForward();
			}
		}
	}
	private void goForward() {
		while (noBeepersPresent()) {
			move();
		}
	}
}