
/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

	/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

	/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

	/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;

	public void run() {
		final int x = getWidth();
		final int y = getHeight();
		// the first for loop gives us information about on which row we are at the
		// moment .
		for (int bricksInARow = BRICKS_IN_BASE; bricksInARow > 0; bricksInARow--) {
			// the second for loop puts defined number of bricks in an appropriate row
			for (int brickNumber = 1; brickNumber <= bricksInARow; brickNumber++) {
				// x/2-bricksInARow*BRICK_WIDTH/2-bricksInARow --> this coordinates are for the
				// first rectangle in an appropriate row .
				// and if we add brickNumber*BRICK_WIDTH we get x coordinate of all bricks .
				int rectX = x / 2 - (bricksInARow + 2) * BRICK_WIDTH / 2 + brickNumber * BRICK_WIDTH;
				// rectY means how many times we should go up , if we are on the first row we should go up ones ,
				// if we are on the second row we should go up twice .
				int rectY = y - BRICK_HEIGHT * (BRICKS_IN_BASE - bricksInARow + 1);
				GRect rect = new GRect(rectX, rectY, BRICK_WIDTH, BRICK_HEIGHT);
				add(rect);
			}
			pause(100);
		}
	}
}
