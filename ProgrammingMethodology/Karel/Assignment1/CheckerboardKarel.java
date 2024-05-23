
/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {
	public void run() {
		if (frontIsBlocked()) {
			// checks if the vertical lines are one or more
			turnLeft();
			if (noBeepersPresent()) {
				putBeeper();
			}

		}
		while (frontIsClear()) {
			fillRow();
			if (facingEast()) {
				// if in the end Karel is standing on odd number of horizontal line , turns left
				turnLeft();
			}
			if (facingWest()) {
				// if in the end Karel is standing on even number of horizontal line , turns
				// right
				turnRight();
			}
		}

	}

	private void fillRow() {
		while (frontIsClear()) {
			if (noBeepersPresent()) {
				// checks if there is a beeper either on the first or on the last square of a
				// horizontal line
				putBeeper();
			}

			move();

			if (frontIsClear()) {
				move();
				putBeeper();
			} else {
				if (frontIsBlocked()) {
					if (facingEast()) {
						goLeftUp();
					}
				}
				if (frontIsBlocked()) {
					if (facingWest()) {
						goRightUp();
					}

				}

			}
			checkWhatsAbove();

		}
	}

	private void goLeftUp() {
		if (leftIsClear()) {
			turnLeft();
			move();
			turnLeft();
		}

	}

	private void goRightUp() {
		if (rightIsClear()) {
			turnRight();
			move();
			turnRight();
		}

	}

	private void checkWhatsAbove() {
		// if on the last square of the vertical line beeper does not present , karel
		// does the
		// following two checks
		if (frontIsBlocked()) {
			if (facingEast()) {
				if (leftIsClear()) {
					goLeftUp();
					move();
				}
			} else {
				if (rightIsClear()) {
					goRightUp();
					move();

				}

			}

		}

	}
}
