
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;
import java.time.ZonedDateTime;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	RandomGenerator rgen = RandomGenerator.getInstance();

	private double vx = rgen.nextDouble(-3, 3);

	private static double vy = 3;

	private static int NUMBER_OF_BRICKS = NBRICKS_PER_ROW * NBRICK_ROWS;

	private int counter = NTURNS;

	private GOval ball;

	private GRect rect;

	private GRect paddle;

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		constructInterface();
		startGame();

	}

	// the constructInterface method sets the interface of game
	private void constructInterface() {
		constructBricks();
		constructPaddle();
		addMouseListeners();
	}

	// this method constructs bricks
	private void constructBricks() {
		// START_OFFSET is the distance between the start of the canvas and the first
		// brick ,
		// simultaneously it's the distance between the canvas end and the last brick .
		double START_OFFSET = (WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2;
		for (int i = 1; i <= NBRICK_ROWS; i++) {
			for (int j = 1; j <= NBRICKS_PER_ROW; j++) {
				double rectX = START_OFFSET + (j - 1) * BRICK_SEP + (j - 1) * BRICK_WIDTH;
				double rectY = BRICK_Y_OFFSET + (i - 1) * (BRICK_HEIGHT + BRICK_SEP);
				rect = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				rect.setFilled(true);
				setColors(i, rect);
				add(rect, rectX, rectY);
			}
		}

	}

	// this method constructs paddle
	private void constructPaddle() {
		int y = getHeight();
		int paddleX = (WIDTH - PADDLE_WIDTH) / 2;
		int paddleY = y - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		add(paddle, paddleX, paddleY);
	}

	// this method creates a ball and sets it in the centre of the game interface
	private void constructBall() {
		int ballX = WIDTH / 2 - BALL_RADIUS;
		int ballY = HEIGHT / 2 - BALL_RADIUS;
		ball = new GOval(2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		add(ball, ballX, ballY);
	}

	// this method is an actual game , if there still presents at least one brick (
	// game is not over ) the game starts over and over .
	// the game starts only for the above given number of attempts
	private void startGame() {
		for (int i = 0; i < NTURNS; i++) {
			if (NUMBER_OF_BRICKS > 0) {
				constructBall();
				waitForClick();
				ballMovement(ball);
				counter--;
				endGame();
			}
		}
	}

	// this method pops up on the screen if a player did not win in his three
	// attempts
	private void endGame() {
		if (counter == 0) {
			GLabel lab = new GLabel(" You lost ");
			lab.setFont("Arial Black-20");
			lab.setColor(Color.RED);
			double a = lab.getWidth();
			double b = lab.getHeight();
			add(lab, (getWidth() - a) / 2, (getHeight() - b) / 2);
		}
	}

	// this method sets the colours of bricks
	private void setColors(int i, GRect rect) {
		if (i % 10 == 1 || i % 10 == 2)
			rect.setFillColor(Color.RED);
		if (i % 10 == 3 || i % 10 == 4)
			rect.setFillColor(Color.ORANGE);
		if (i % 10 == 5 || i % 10 == 6)
			rect.setFillColor(Color.YELLOW);
		if (i % 10 == 7 || i % 10 == 8)
			rect.setFillColor(Color.GREEN);
		if (i % 10 == 9 || i % 10 == 0)
			rect.setFillColor(Color.CYAN);
	}

	public void mouseMoved(MouseEvent e) {
		if (NUMBER_OF_BRICKS != 0 && counter != 0) {
			int y = getHeight();
			int mouseX = e.getX();
			if (mouseX >= PADDLE_WIDTH / 2 && mouseX <= WIDTH - PADDLE_WIDTH / 2) {
				paddle.setLocation(mouseX - PADDLE_WIDTH / 2, y - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
			}
		}
	}

	// this method makes the ball move
	private void ballMovement(GOval ball) {
		// the starting speed of the ball is randomly generated
		if (rgen.nextBoolean(0.5))
			vx = -vx;
		while (true) {
			long start = ZonedDateTime.now().toInstant().toEpochMilli();
			checkSideColisions();
			checkColision();
			ball.move(vx, vy);
			if (ball.getY() >= getHeight() - 2 * BALL_RADIUS || NUMBER_OF_BRICKS == 0) {
				remove(ball);
				break;
			}
			// the following five line of the code makes the program run smoother and locks
			// frame per second
			long time = 8;
			long end = ZonedDateTime.now().toInstant().toEpochMilli();
			long left = time - (end - start);
			if (left > 0)
				pause(left);
		}
	}

	// this method checks the ball position and if the ball touches the walls the
	// ball changes its direction
	private void checkSideColisions() {
		int x = getWidth();
		if (ball.getX() >= x - 2 * BALL_RADIUS)
			vx = -vx;
		if (ball.getX() <= 0)
			vx = -vx;
		if (ball.getY() <= 0)
			vy = -vy;
	}

	// this method returns the appropriate object which is on the same position
	// where the ball is
	private GObject getCollider() {
		// this GObject returns the object on the bottom right point of the
		// circumscribed square over the circle
		GObject object1 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		// this GObject returns the object on the bottom left point of the circumscribed
		// square over the circle
		GObject object2 = getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		// this GObject returns the object on the top right point of the circumscribed
		// square over the circle
		GObject object3 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		// this GObject returns the object on the top left point of the circumscribed
		// square over the circle
		GObject object4 = getElementAt(ball.getX(), ball.getY());
		if (object1 != null) {
			return (object1);
		} else if (object2 != null) {
			return (object2);
		} else if (object3 != null) {
			return (object3);
		} else if (object4 != null) {
			return (object4);
		}
		return null;

	}

	private void checkColision() {
		// this method is used only if the ball touches the up side of the paddle
		if (getCollider() == paddle && vy > 0) {
			vy = -vy;
		}
		// this method is used only if the ball touches the sides of the paddle
		if (getCollider() == paddle && ball.getY() + 2 * BALL_RADIUS > getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT) {
			vx = -vx;
			vy = -vy;
		}
		// this method is used only if the ball touches a brick
		if (getCollider() != null && getCollider() != paddle) {
			remove(getCollider());
			vy = -vy;
			NUMBER_OF_BRICKS--;
			winningGame();
		}

	}
	// if person wins this method makes you won label pop up on the screen
		private void winningGame() {
			if (NUMBER_OF_BRICKS == 0) {
				remove(ball);
				GLabel lab = new GLabel(" You won " );
				lab.setFont("Arial Black-15");
				lab.setColor(Color.RED);
				double x = lab.getWidth();
				double y = lab.getHeight();
				add(lab, (getWidth() - x) / 2, (getHeight() - y) / 2);
			}
		}
}
