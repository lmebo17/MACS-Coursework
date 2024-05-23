
/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {
	public void run() {
		firstCircle();
		secondCircle();
		thirdCircle();
	}

	private int CIRCLE_DIAMETER = 144;
	private double k=144/2.54;
	private int CIRCLE_DIAMETER1 = (int) (k * 1.65 );
	private int CIRCLE_DIAMETER2 = (int) (k * 0.76 );

	private void firstCircle() {
		int WIDTH = getWidth();
		int HEIGHT = getHeight();
		//ovalX = the half of canvas x coordinate - the radius of the circle
		int ovalX=(WIDTH - CIRCLE_DIAMETER) / 2;
		//ovalY = the half of canvas y coordinate - the radius of the circle
		int ovalY=(HEIGHT - CIRCLE_DIAMETER) / 2;
		GOval oval = new GOval(ovalX , ovalY , CIRCLE_DIAMETER, CIRCLE_DIAMETER);
		oval.setFilled(true);
		oval.setColor(Color.RED);
		add(oval);
	}

	private void secondCircle() {
		int WIDTH = getWidth();
		int HEIGHT = getHeight();
		//oval1X = the half of canvas x coordinate - the radius of the circle
		int oval1X = (WIDTH - CIRCLE_DIAMETER1) / 2;
		//oval1Y = the half of canvas y coordinate - the radius of the circle
		int oval1Y = (HEIGHT - CIRCLE_DIAMETER1) / 2;			
		GOval oval1 = new GOval( oval1X , oval1Y , CIRCLE_DIAMETER1 , CIRCLE_DIAMETER1);
		add(oval1);
		oval1.setFilled(true);
		oval1.setColor(Color.WHITE);
	}

	private void thirdCircle() {
		int WIDTH = getWidth();
		int HEIGHT = getHeight();
		//oval2X = the half of canvas x coordinate - the radius of the circle
		int oval2X = (WIDTH - CIRCLE_DIAMETER2) / 2;
		//oval2Y = the half of canvas y coordinate - the radius of the circle
		int oval2Y = (HEIGHT - CIRCLE_DIAMETER2) / 2;
		GOval oval2 = new GOval( oval2X , oval2Y , CIRCLE_DIAMETER2, CIRCLE_DIAMETER2);
		oval2.setFilled(true);
		oval2.setFillColor(Color.RED);
		add(oval2);
	}

}
