
/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {
	private int RECT_WIDTH = 200;
	private int RECT_HEIGHT = 50;
	private int OFFSET_BETWEEN_RECTS = 30;
	private int OFFSET_FROM_TOP_RECT = 50;

	public void run() {
		midRect();
		leftMidRect();
		rightMidRect();
		midTopRect();
		attachLines();
	}

	private void midRect() {
		int HEIGHT = getHeight();
		int WIDTH = getWidth();
		int rect1X = (WIDTH - RECT_WIDTH) / 2;
		int rect1Y = HEIGHT / 2;
		GRect rect1 = new GRect(rect1X, rect1Y, RECT_WIDTH, RECT_HEIGHT);
		add(rect1);
		GLabel lab1 = new GLabel("ConsoleProgram");
		double lab1x = lab1.getWidth();
		double lab1y = lab1.getAscent();
		double labX = WIDTH / 2 - lab1x / 2;
		double labY = HEIGHT / 2 + lab1y / 2 + RECT_HEIGHT / 2;
		add(lab1, labX, labY);
	}

	private void leftMidRect() {
		int HEIGHT = getHeight();
		int WIDTH = getWidth();
		int rect2X = (WIDTH / 2 - 3 * RECT_WIDTH / 2 - OFFSET_BETWEEN_RECTS);
		int rect2y = HEIGHT / 2;
		GRect rect2 = new GRect(rect2X, rect2y, RECT_WIDTH, RECT_HEIGHT);
		add(rect2);
		GLabel lab2 = new GLabel("GraphicsProgram");
		double lab2x = lab2.getWidth();
		double lab2y = lab2.getAscent();
		double labX = (WIDTH / 2 - RECT_WIDTH - OFFSET_BETWEEN_RECTS - lab2x / 2);
		double labY = HEIGHT / 2 + lab2y / 2 + RECT_HEIGHT / 2;
		add(lab2, labX, labY);

	}

	private void rightMidRect() {
		int HEIGHT = getHeight();
		int WIDTH = getWidth();
		int rect3X = (WIDTH / 2 + RECT_WIDTH / 2 + OFFSET_BETWEEN_RECTS);
		int rect3Y = HEIGHT / 2;
		GRect rect3 = new GRect(rect3X, rect3Y, RECT_WIDTH, RECT_HEIGHT);
		add(rect3);
		GLabel lab3 = new GLabel("DialogProgram");
		double lab3x = lab3.getWidth();
		double lab3y = lab3.getAscent();
		double labX = WIDTH / 2 + RECT_WIDTH + OFFSET_BETWEEN_RECTS - lab3x / 2;
		double labY = HEIGHT / 2 + lab3y / 2 + RECT_HEIGHT / 2;
		add(lab3, labX, labY);

	}

	private void midTopRect() {
		int HEIGHT = getHeight();
		int WIDTH = getWidth();
		int rect4X = (WIDTH - RECT_WIDTH) / 2;
		int rect4Y = (HEIGHT / 2 - 3 * RECT_HEIGHT + OFFSET_FROM_TOP_RECT);
		GRect rect4 = new GRect(rect4X, rect4Y, RECT_WIDTH, RECT_HEIGHT);
		add(rect4);
		GLabel lab4 = new GLabel("Program");
		double lab4x = lab4.getWidth();
		double lab4y = lab4.getAscent();
		double labX = WIDTH / 2 - lab4x / 2;
		double labY = HEIGHT / 2 + lab4y / 2 - OFFSET_FROM_TOP_RECT - RECT_HEIGHT / 2;
		add(lab4, labX , labY);

	}

	private void attachLines() {
		int HEIGHT = getHeight();
		int WIDTH = getWidth();
		int line1X1 = WIDTH / 2;
		int line1Y1 = HEIGHT / 2 - OFFSET_FROM_TOP_RECT;
		int line1Y2 = HEIGHT / 2;
		GLine line1 = new GLine(line1X1, line1Y1, line1X1, line1Y2);
		add(line1);
		int line2X1 = WIDTH / 2;
		int line2Y1 = HEIGHT / 2 - OFFSET_FROM_TOP_RECT;
		int line2X2 = WIDTH / 2 - RECT_WIDTH - OFFSET_BETWEEN_RECTS;
		int line2Y2 = HEIGHT / 2;
		GLine line2 = new GLine(line2X1, line2Y1, line2X2, line2Y2);
		add(line2);
		int line3X1 = WIDTH / 2;
		int line3Y1 = HEIGHT / 2 - OFFSET_FROM_TOP_RECT;
		int line3X2 = WIDTH / 2 + RECT_WIDTH + OFFSET_BETWEEN_RECTS;
		int line3Y2 = HEIGHT / 2;
		GLine line3 = new GLine(line3X1, line3Y1, line3X2, line3Y2);
		add(line3);
	}
}
