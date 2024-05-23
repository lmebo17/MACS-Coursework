
/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {
	public void run() {
		reachNewspaper();
		pickNewspaper();
		goBack();
	}

//method reachNewspaper makes karel go where the newspaper is
	public void reachNewspaper() {
		move();
		move();
		turnRight();
		move();
		turnLeft();
		move();
	}

	// karel picks newspaper and turns around
	public void pickNewspaper() {
		pickBeeper();
		turnAround();
	}

	// karel goes to the starting position
	public void goBack() {
		move();
		turnRight();
		move();
		turnLeft();
		move();
		move();
		turnAround();

	}
}
