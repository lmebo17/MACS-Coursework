
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

public class BreakoutExtension extends GraphicsProgram {

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

	private static final int OFFSET_BETWEEN_HEARTS = 5;
	
	private static final int HEART_TOP_OFFSET = 5;
	
	RandomGenerator rgen = RandomGenerator.getInstance();

	private static int NUMBER_OF_BRICKS = NBRICKS_PER_ROW * NBRICK_ROWS;

	private double vx = rgen.nextDouble(1.0, 3.0);

	private static double vy = 3;

	private GOval ball;

	private GRect rect;

	private GRect paddle;

	private GImage img;

	private int counter = NTURNS;

	private GLabel startLab;

	private GLabel tryAgain;

	private GImage heart1;

	private GImage heart2;

	private GImage heart3;

	private GLabel points;

	private int HighScore = 0;
	
	private Color randomColor = rgen.nextColor();

	/* Method: run() */
	/** Runs the Breakout program. */

	public void run() {
		constructInterface();
		startGame();

	}

	// the constructInterface method sets the interface of game
	private void constructInterface() {
		canvasTheme();
		startMessage();
		constructHearts();
		points();
		constructBricks();
		constructPaddle();
		addMouseListeners();
	}

	// this method sets the background theme of the game
	private void canvasTheme() {
		img = new GImage("theme1.gif");
		img.sendToBack();
		img.setSize(WIDTH, HEIGHT);
		add(img, 0, 0);

	}

	// this method gives a message to the player - what he/she has to do in order to start the game 
	private void startMessage() {
		startLab = new GLabel(" Press on the ball to start ");
		startLab.setFont("Arial Black-20");
		startLab.setColor(Color.RED);
		double labX = startLab.getWidth();
		add(startLab, getWidth() / 2 - labX / 2, 2 * getHeight() / 3);
	}
	// after loosing a game try again label pops up on the interface 
	private GLabel tryAgain() {
		tryAgain = new GLabel(" Try again ");
		tryAgain.setFont("Arial Black-50");
		tryAgain.setColor(Color.RED);
		add(tryAgain, (getWidth() / 2 - tryAgain.getWidth() / 2), 2 * getHeight() / 3);
		return tryAgain;
	}

	// this method sets a heart image on the bottom of the interface , this heart is designated for the player's game life
	public void heart1() {
		heart1 = new GImage("heart.png ");
		heart1.setSize(30, 30);
		double heartX = heart1.getWidth();
		heart1.sendBackward();
		add(heart1, getWidth() - (heartX + OFFSET_BETWEEN_HEARTS) , HEART_TOP_OFFSET);

	}

	// this method sets a heart image on the bottom of the interface , this heart is designated for the player's game life
	public void heart2() {
		heart2 = new GImage("heart.png ");
		heart2.setSize(30, 30);
		double heartX = heart2.getWidth();	
		heart2.sendBackward();
		add(heart2, getWidth() - 2 * (heartX + OFFSET_BETWEEN_HEARTS), HEART_TOP_OFFSET);
	}
	
	// this method sets a heart image on the bottom of the interface , this heart is designated for the player's game life
	public void heart3() {
		heart3 = new GImage("heart.png ");
		heart3.setSize(30, 30);
		double heartX = heart3.getWidth();	
		heart3.sendBackward();
		add(heart3, getWidth() - 3 * (heartX + OFFSET_BETWEEN_HEARTS), HEART_TOP_OFFSET);
	}
	
	private void constructHearts() {
		heart1();
		heart2();
		heart3();
	}
	
	// this method constructs bricks
	private void constructBricks() {
		// START_OFFSET is the distance between the start of the canvas and the first brick , 
		// simultaneously it's the distance between the canvas end and the last brick .
		double START_OFFSET = (WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2;
		for (int i = 1; i <= NBRICK_ROWS; i++) {
			for (int j = 1; j <= NBRICKS_PER_ROW; j++) {
				double rectX = START_OFFSET + (j - 1) * BRICK_SEP + (j - 1) * BRICK_WIDTH;
				double rectY = BRICK_Y_OFFSET + (i - 1) * (BRICK_HEIGHT + BRICK_SEP);
				rect = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				rect.setFilled(true);
				// i have generated RGB colours on my own so those numbers in RGB are not attached to any logical operation
				Color rgb = new Color(255 , 0 , 201-2*i*j);
				rect.setFillColor(rgb);
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
		paddle.setFillColor(Color.CYAN);
		add(paddle, paddleX, paddleY);
	}
	//this method creates a ball and sets it in the centre of the game interface
	private void constructBall() {
		int ballX = WIDTH / 2 - BALL_RADIUS;
		int ballY = HEIGHT / 2 - BALL_RADIUS;
		ball = new GOval(2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(randomColor);
		add(ball, ballX, ballY);
	}
	// this method is an actual game , if there still presents at least one brick ( game is not over ) the game starts over and over . 
	// the game starts only for the above given number of attempts
	private void startGame() { 
		for (int i = 1; i <= NTURNS; i++) {
			if (NUMBER_OF_BRICKS > 0) {
				constructBall();
				waitForClick();
				if (i > 1) {
					remove(tryAgain);
				}
				remove(startLab);
				ballMovement(ball);
				counter--;
				// if a player has an attempt and there are bricks on the screen that means 
				//that code could come on this line only if the ball has touched the bottom of canvas
				if (counter != 0 && NUMBER_OF_BRICKS != 0) {
					tryAgain();
					if (i == 1)
						remove(heart3);
					if (i == 2)
						remove(heart2);
				}
				endGame();
			}
		}
	}


	// this method pops up on the screen if a player did  not win in his three attempts 
	private void endGame() {
		java.applet.AudioClip Clip = MediaTools.loadAudioClip("Death-sound-in-Minecraft.au");
		if (counter == 0) {
			Clip.play();
			remove(heart1);
			GLabel lab = new GLabel(" You lost - Your score is " + HighScore);
			lab.setFont("Arial Black-20");
			lab.setColor(Color.RED);
			double a = lab.getWidth();
			double b = lab.getHeight();
			add(lab, (getWidth() - a) / 2, (getHeight() - b) / 2);
		}
	}
	// this method makes the ball move
	private void ballMovement(GOval ball) {
		//the starting speed of the ball is randomly generated 
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
			// the following four line of the code makes the program run smoother and locks frame per second
			long time = 8;
			long end = ZonedDateTime.now().toInstant().toEpochMilli();
			long left = time - (end - start);
			if (left > 0)
				pause(left);
		}
	}
	//this method checks the ball position and if the ball touches the walls the ball changes its direction
	private void checkSideColisions() {
		int x = getWidth();
		if (ball.getX() >= x - 2 * BALL_RADIUS)
			vx = -vx;
		
		if (ball.getX() <= 0)
			vx = -vx;
		if (ball.getY() <= 0)
			vy = -vy;
	}
	// this method returns the appropriate object which is on the same position where the ball is
	private GObject getCollider() {
		//this GObject returns the object on  the bottom right point of the circumscribed square over the circle
		GObject object1 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		//this GObject returns the object on  the bottom left point of the circumscribed square over the circle
		GObject object2 = getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		//this GObject returns the object on  the top  right point of the circumscribed square over the circle
		GObject object3 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		//this GObject returns the object on  the top  left point of the circumscribed square over the circle
		GObject object4 = getElementAt(ball.getX(), ball.getY());
		// this method should return a paddle or the bricks , so in order not to return the score label , or the background of the game ,
		// or the game life images , i wrote the following restrictions . 
		if (object1 != img && object1 != null && object1 != heart1 && object1 != heart2 && object1 != heart3
				&& object1 != points) {
			return (object1);
		} else if (object2 != img && object2 != null && object2 != heart1 && object2 != heart2 && object2 != heart3
				&& object2 != points) {
			return (object2);
		} else if (object3 != img && object3 != null && object3 != heart1 && object3 != heart2 && object3 != heart3
				&& object3 != points) {
			return (object3);
		} else if (object4 != img && object4 != null && object4 != heart1 && object4 != heart2 && object4 != heart3
				&& object4 != points) {
			return (object4);
		}
		return img;

	}

	private void checkColision() {
		java.applet.AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
		randomColor = rgen.nextColor();
		// this method is used only if the ball touches the up side of the paddle
		if (getCollider() == paddle && vy > 0) {
			bounceClip.play();
			vy = -vy;
			ball.setFillColor(randomColor);
			paddle.setFillColor(randomColor);
		}
		// this method is used only if the ball touches the sides of the paddle
		if (getCollider() == paddle && ball.getY() + 2 * BALL_RADIUS > getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT) {
			vx = -vx;
			vy = -vy;
			ball.setFillColor(randomColor);
			paddle.setFillColor(randomColor);
			bounceClip.play();
		}
		
		// this method is used only if the ball touches a brick
		if (getCollider() != img && getCollider() != paddle && getCollider() != heart1 && getCollider() != heart2
				&& getCollider() != heart3 && getCollider() != points ) {
			ball.setFillColor(randomColor);
			bounceClip.play();
			score();
			remove(getCollider());	
			vy = -vy;
			NUMBER_OF_BRICKS--;
			winningGame();
		}
	}

	// if person wins this method makes you won label pop up on the screen
	private void winningGame() {
		java.applet.AudioClip winningClip = MediaTools.loadAudioClip("you-win-tekken.au");
		if (NUMBER_OF_BRICKS == 0) {
			pause(100);
			winningClip.play();
			remove(ball);
			GLabel lab = new GLabel(" You won , your score is " + HighScore);
			lab.setFont("Arial Black-15");
			lab.setColor(Color.RED);
			double x = lab.getWidth();
			double y = lab.getHeight();
			add(lab, (getWidth() - x) / 2, (getHeight() - y) / 2);
		}
	}
	// this method creates a label on which the player's score is shown
	private void points() {
		points = new GLabel(" Score : 0 ");
		points.setFont("-30");
		points.setColor(Color.RED);
		int offsetFromStart = 2;
		points.setLocation(offsetFromStart, 30);
		add(points);

	}
	
	// this method calculates the score of a player 
	private void score() {
		// for the first two rows
		// 186 = BRICK_Y_OFFSET + 10 * BRICK_HEIGHT + 9 * BRICK_SEP
		// 166 = BRICK_Y_OFFSET + 8 * BRICK_HEIGHT + 8 * BRICK_SEP
		if (getCollider().getY() <= 186 && getCollider().getY() >= 166) {
			HighScore += 10;
		}
		// for the second two rows
		// 162 = BRICK_Y_OFFSET + 8 * BRICK_HEIGHT + 7 * BRICK_SEP
		// 142 = BRICK_Y_OFFSET + 6 * BRICK_HEIGHT + 6 * BRICK_SEP
		if (getCollider().getY() <= 162 && getCollider().getY() >= 142) {
			HighScore += 20;
		}
		// for the third two rows
		// 138 = BRICK_Y_OFFSET + 6 * BRICK_HEIGHT + 5 * BRICK_SEP
		// 118 = BRICK_Y_OFFSET + 4 * BRICK_HEIGHT + 4 * BRICK_SEP
		if (getCollider().getY() <= 138 && getCollider().getY() >= 118) {
			HighScore += 30;
		}
		// for the fourth two rows
		// 114 = BRICK_Y_OFFSET + 4 * BRICK_HEIGHT + 3 * BRICK_SEP
		// 94 = BRICK_Y_OFFSET + 2 * BRICK_HEIGHT + 2 * BRICK_SEP
		if (getCollider().getY() <= 114 && getCollider().getY() >= 94) {
			HighScore += 40;
		}
		// for the fifth two rows
		// 90 = BRICK_Y_OFFSET + 2 * BRICK_HEIGHT + 1 * BRICK_SEP
		// 70 = BRICK_Y_OFFSET
		if (getCollider().getY() <= 90 && getCollider().getY() >= 70) {
			HighScore += 50;
		}
		points.setLabel("Score : " + HighScore);
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
}
