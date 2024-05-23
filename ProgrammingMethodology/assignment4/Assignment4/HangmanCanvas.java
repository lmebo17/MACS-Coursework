
/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import java.util.ArrayList;

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	private GLabel lab = new GLabel("");
	private String chars = "";
	private GLabel wrongChars = new GLabel("");
	private GLine scaffold;
	private GLine rightFoot;
	private GLine leftFoot;
	private GOval head;
	private GLine body;
	private GLine beam;
	private GLine rope;
	private GLine leftUpArm;
	private GLine leftLowArm;
	private GLine rightUpArm;
	private GLine rightLowArm;
	private GLine leftHip;
	private GLine rightHip;
	private GLine leftLeg;
	private GLine rightLeg;
	private ArrayList<GObject> arr = new ArrayList<GObject>();
	

	/** Resets the display so that only the scaffold appears */
	public void reset() {
		constructScaffold();
		constructBeam();
		constructRope();
		removeObjects();		
	}
	//this method removes objects from the canvas and from the array list too.
	private void removeObjects() {
		for(int i = 0; i < arr.size(); i++) {
			remove(arr.get(i));
		}
		for(int i = 0; i < arr.size(); i++) {
			arr.remove(i);
		}
		chars = "";
	}
	//this method draws objects on the canvas.
	public void constructionCases(int x) {
		switch(x) {
		  case 0: {
			  constructHead();
			  arr.add(head);
			  break;
		  }
		  case 1: {
			  constructBody();
			  arr.add(body);
			  break;
		  }
		  case 2: {
			  constructLeftHand();
			  arr.add(leftUpArm);
			  arr.add(leftLowArm);
			  break;
		  }
		  case 3: {
			  constructRightHand();
			  arr.add(rightUpArm);
			  arr.add(rightLowArm);
			  break;
		  }
		  case 4: {
			  constructLeftLeg();
			  constructLeftHip();
			  arr.add(leftLeg);
			  arr.add(leftHip);
			  break;
		  } 
		  case 5: {
			  constructRightLeg();
			  constructRightHip();
			  arr.add(rightLeg);
			  arr.add(rightHip);
			  break;
		  }
		  case 6: {
			  constructLeftFoot();
			  arr.add(leftFoot);
			  break;
		  }
		  case 7: {
			  constructRightFoot();
			  arr.add(rightFoot);
			  break;
		  }
		}
	}
	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		lab.setLabel(word);
		int labX = getWidth()/2-BEAM_LENGTH;
		int labY = OFFSET_FROM_TOP+SCAFFOLD_HEIGHT+OFFSET_FROM_LABEL;
		add(lab, labX , labY);
		arr.add(lab);
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user. Calling
	 * this method causes the next body part to appear on the scaffold and adds the
	 * letter to the list of incorrect guesses that appears at the bottom of the
	 * window.
	 */
	public void noteIncorrectGuess(char letter) {
		letter = Character.toUpperCase(letter);
		if (!(chars.contains(letter + ""))) {
			chars = chars + letter;
			wrongChars.setLabel(chars);
		}
		int wrongCharsX = getWidth()/2-BEAM_LENGTH;
		int wrongCharsY = OFFSET_FROM_TOP+SCAFFOLD_HEIGHT+OFFSET_FROM_LABEL+OFFSET_BETWEEN_LABELS;
		add(wrongChars, wrongCharsX , wrongCharsY);
		arr.add(wrongChars);
	}

	private void constructScaffold() {
		int scaffoldX1 = getWidth() / 2 - BEAM_LENGTH;
		int scaffoldX2 = scaffoldX1;
		int scaffoldY1 = OFFSET_FROM_TOP;
		int scaffoldY2 = scaffoldY1 + SCAFFOLD_HEIGHT;
		scaffold = new GLine(scaffoldX1, scaffoldY1, scaffoldX2, scaffoldY2);
		add(scaffold);
	}

	private void constructBeam() {
		int beamX1 = getWidth() / 2;
		int beamX2 = getWidth() / 2 - BEAM_LENGTH;
		int beamY1 = OFFSET_FROM_TOP;
		int beamY2 = beamY1;
		beam = new GLine(beamX1, beamY1, beamX2, beamY2);
		add(beam);
	}

	private void constructRope() {
		int ropeX1 = getWidth() / 2;
		int ropeX2 = ropeX1;
		int ropeY1 = OFFSET_FROM_TOP;
		int ropeY2 = ropeY1 + ROPE_LENGTH;
		rope = new GLine(ropeX1, ropeY1, ropeX2, ropeY2);
		add(rope);
	}

	private void constructHead() {
		int headX = getWidth() / 2 - HEAD_RADIUS;
		int headY = OFFSET_FROM_TOP + ROPE_LENGTH;
		head = new GOval(headX, headY, 2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
		add(head);
	}
	private void constructBody() {
		int bodyX = getWidth()/2;
		int bodyY1 = OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS;
		int bodyY2 = bodyY1+BODY_LENGTH;
		body = new GLine(bodyX , bodyY1 , bodyX , bodyY2);
		add(body);
	}
	private void constructLeftHand() {
		int upLeftArmX1 = getWidth()/2;
		int upLeftArmX2 = getWidth()/2-UPPER_ARM_LENGTH;
		int upLeftArmY = OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD;
		leftUpArm = new GLine(upLeftArmX1 , upLeftArmY , upLeftArmX2 , upLeftArmY);
		add(leftUpArm);
		int leftLowArmX = upLeftArmX2;
		int leftLowArmY1 = upLeftArmY;
		int leftLowArmY2  = leftLowArmY1 + LOWER_ARM_LENGTH;
		leftLowArm  = new GLine(leftLowArmX , leftLowArmY1 , leftLowArmX , leftLowArmY2);
		add(leftLowArm);
	}
	private void constructRightHand() {
		int upRightArmX1 = getWidth()/2;
		int upRightArmX2 = getWidth()/2+UPPER_ARM_LENGTH;
		int upRightArmY = OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD;
		rightUpArm = new GLine(upRightArmX1 , upRightArmY , upRightArmX2 , upRightArmY);
		add(rightUpArm);
		int rightLowArmX = upRightArmX2;
		int rightLowArmY1 = upRightArmY;
		int rightLowArmY2  = rightLowArmY1 + LOWER_ARM_LENGTH;
		rightLowArm  = new GLine(rightLowArmX , rightLowArmY1 , rightLowArmX , rightLowArmY2);
		add(rightLowArm);
	}
	private void constructLeftHip() {
		int HipX1 = getWidth()/2;
		int HipX2 = HipX1-HIP_WIDTH;
		int HipY= OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH;
		leftHip = new GLine(HipX1 , HipY , HipX2 , HipY);
		add(leftHip);
	}
	private void constructRightHip() {
		int HipX1 = getWidth()/2;
		int HipX2 = HipX1+HIP_WIDTH;
		int HipY= OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH;
		rightHip = new GLine(HipX1 , HipY , HipX2 , HipY);
		add(rightHip);
	}
	private void constructLeftLeg() {
		int legX = getWidth()/2-HIP_WIDTH;
		int legY1 = OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH;
		int legY2 = legY1 + LEG_LENGTH;
		leftLeg = new GLine(legX , legY1 , legX , legY2);
		add(leftLeg);
	}
	private void constructRightLeg() {
		int legX = getWidth()/2+HIP_WIDTH;
		int legY1 = OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH;
		int legY2 = legY1 + LEG_LENGTH;
		rightLeg = new GLine(legX , legY1 , legX , legY2);
		add(rightLeg);
	}
	private void constructLeftFoot() {
		int footX1 = getWidth()/2-HIP_WIDTH;
		int footX2 = footX1-FOOT_LENGTH;
		int footY = OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH;
		leftFoot = new GLine(footX1 , footY , footX2 , footY);
		add(leftFoot);
	}
	private void constructRightFoot() {
		int footX1 = getWidth()/2+HIP_WIDTH;
		int footX2 = footX1+FOOT_LENGTH;
		int footY = OFFSET_FROM_TOP+ROPE_LENGTH+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH;
		rightFoot = new GLine(footX1 , footY , footX2 , footY);
		add(rightFoot);
	}

	/* Constants for the simple version of the picture (in pixels) */
	private static final int OFFSET_FROM_LABEL =30;
	private static final int OFFSET_BETWEEN_LABELS = 20;
	private static final int OFFSET_FROM_TOP = 50;
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
}
