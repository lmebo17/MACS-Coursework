
/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		while (!(nPlayers >= 1 && nPlayers <= MAX_PLAYERS)) {
			nPlayers = dialog.readInt("Please , enter a number between one and four");
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		lowerScore = new int[nPlayers];
		upperScore = new int[nPlayers];
		totalSum = new int[nPlayers];
		hasUpperBonus = new boolean[nPlayers];
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		scores = new int[nPlayers + 1][N_CATEGORIES + 1];
		playGame();
	}

	private void playGame() {
		fillArr(scores);
		for (int t = 1; t <= N_SCORING_CATEGORIES; t++) {
			for (int player = 1; player <= nPlayers; player++) {
				display.printMessage(
						playerNames[player - 1] + "'s turn! Click  \"Roll Dice" + " button to roll the dice");
				diceInitialization(dice, player);
				simulateTurns();
				display.printMessage("Select a category for this roll.");
				int category = display.waitForPlayerToSelectCategory();
				checkRepetition(player, category);
				int score = checkCategory(dice, category);
				scores[player][category] = score;
				scoreBoard(category, player, score);
			}
			evaluateWinner();
		}
	}

	// this method randomizes the selected dices .
	private void simulateTurns() {
		for (int i = 0; i < NUMBER_OF_TURNS - 1; i++) {
			display.waitForPlayerToSelectDice();
			diceRandomizer(dice);
			display.displayDice(dice);
		}
	}

	// after the game is over, this method evaluates the player with max score and
	// prints the special message on the scren.
	private void evaluateWinner() {
		int max = 0;
		for (int i = 0; i < nPlayers; i++) {
			if (totalSum[max] < totalSum[i]) {
				max = i;
			}
		}
		display.printMessage("Congratulations , " + playerNames[max] + " , You are the winner with a total score of "
				+ totalSum[max]);
	}

	// this method checks whether the player has already got bonus points.
	private void checkRepetition(int player, int category) {
		while (scores[player][category] != -1) {
			display.printMessage("Please , choose an unchosen category.");
			category = display.waitForPlayerToSelectCategory();
		}
	}

	// this method updates score board in appropriate score fields .
	private void scoreBoard(int category, int player, int score) {
		lowerSum(score, category, player);
		upperSum(score, category, player);
		display.updateScorecard(category, player, score);
		display.updateScorecard(UPPER_SCORE, player, upperScore[player - 1]);
		display.updateScorecard(LOWER_SCORE, player, lowerScore[player - 1]);
		if (upperScore[player - 1] >= 63 && !hasUpperBonus[player - 1]) {
			hasUpperBonus[player - 1] = true;
			display.updateScorecard(UPPER_BONUS, player, 35);
			totalSum[player - 1] = totalSum[player - 1] + 35;
			display.updateScorecard(TOTAL, player, totalScore(score, category, player));
		} else
			display.updateScorecard(TOTAL, player, totalScore(score, category, player));

	}

	// this method counts the sum of the first six point bars .
	private void upperSum(int score, int category, int player) {
		if (category >= ONES && category <= SIXES) {
			upperScore[player - 1] += score;
		}
	}

	// this method counts the sum of the second side of point bars .
	private void lowerSum(int score, int category, int player) {
		if (category >= THREE_OF_A_KIND && category <= CHANCE) {
			lowerScore[player - 1] += score;
		}
	}

	// this method counts the sum of all points.
	private int totalScore(int score, int category, int player) {
		totalSum[player - 1] += score;
		return totalSum[player - 1];
	}

	// this method initializes the dice configurations in the beginning of the game.
	private void diceInitialization(int[] arr, int player) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = rgen.nextInt(1, 6);
		}
		display.waitForPlayerToClickRoll(player);
		display.displayDice(dice);
	}

	// this method randomis the selected dices.
	private void diceRandomizer(int[] arr) {
		for (int j = 0; j < dice.length; j++) {
			if (display.isDieSelected(j)) {
				dice[j] = rgen.nextInt(1, 6);
			}
		}
	}

	// this method counts the sum of the same  dices .
	private int nums(int[] dice, int num) {
		int score = 0;
		for (int i = 0; i < dice.length; i++) {
			if (dice[i] == num)
				score = score + num;
		}
		return score;
	}

	// this method checks whether the dices have three or four same dices init.
	private int sameKinds(int[] dice, int num) {
		int temp = 0;
		if (num == THREE_OF_A_KIND) {
			temp = 3;
		} else if (num == FOUR_OF_A_KIND) {
			temp = 4;
		}
		int score = 0;
		for (int i = 0; i < dice.length - temp + 1; i++) {
			int count = 0;
			for (int j = 0; j < dice.length; j++) {
				if (dice[i] == dice[j]) {
					count++;
				}
				if (count == temp) {
					score = sum(dice);
					break;
				}
			}
		}
		return score;
	}
	// this method checks whether two dices are the same and the other three dices are the same.
	private int fullHouse(int[] dice) {
		int score = 0;
		arr.clear();
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < dice.length; j++) {
				if (dice[i] == dice[j]) {
					count++;
					arr.add(j);
				}
			}
			if (count == 3 && checkOthers(dice)) {
				score = 25;
				return score;
			} else {
				count = 0;
				arr.removeAll(arr);
			}
		}
		return 0;
	}

	// this method checks if the remaining two dices are same but they are different from the starting three dices .
	private boolean checkOthers(int[] dice) {
		for (int i = 0; i < dice.length; i++) {
			for (int j = 0; j < dice.length; j++) {
				if (dice[i] == dice[j] && i != j && !arr.contains(i) && !arr.contains(j)) {
					return true;
				}
			}
		}
		return false;
	}

	// this method checks if the dices satisfy streets.
	private int streets(int[] dice, int num) {
		Arrays.sort(dice);
		int score = 0;
		for (int i = 0; i < dice.length; i++) {
			int count = 1;
			int x = dice[i];
			for (int j = 0; j < dice.length; j++) {
				if (x == dice[j] - 1) {
					x = dice[j];
					j = 0;
					count++;
				}
			}
			if (count == num / 3 + num % 2) {
				score = 30 + (num % 4) * 10;
				return score;
			}
		}
		return 0;
	}

	// this method checks if the dices satisfy yeahtzee
	private int yahtzee(int[] dice) {
		int score = 0;
		for (int i = 0; i < 1; i++) {
			int count = 0;
			for (int j = 0; j < dice.length; j++) {
				if (dice[i] == dice[j]) {
					count++;
				}
				if (count == 5) {
					score = 50;
					break;
				}
			}
		}
		return score;
	}

	// this method sums all the dices .
	private int chance(int[] dice) {
		int score = sum(dice);
		return score;
	}
	// this method sums all the dices .
	private int sum(int[] dice) {
		int sum = 0;
		for (int i = 0; i < dice.length; i++) {
			sum = sum + dice[i];
		}
		return sum;
	}

	// this method fills the score board array with -1 . I do this in order to check whether the player has already chosen 
	// a category .
	private void fillArr(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				arr[i][j] = -1;
			}
		}
	}

	// this method checks whether the dices satisfy the chosen category and returns an appropriate score .
	private int checkCategory(int[] dice, int x) {
		int score = 0;
		if (x >= ONES && x <= SIXES) {
			score = nums(dice, x);
		} else if (x >= THREE_OF_A_KIND && x <= FOUR_OF_A_KIND) {
			score = sameKinds(dice, x);
		} else if (x == FULL_HOUSE) {
			score = fullHouse(dice);
		} else if (x >= SMALL_STRAIGHT && x <= LARGE_STRAIGHT) {
			score = streets(dice, x);
		} else if (x == YAHTZEE) {
			score = yahtzee(dice);
		} else if (x == CHANCE) {
			score = chance(dice);
		}
		return score;
	}

	/* Private instance variables */
	private int[] dice = new int[N_DICE];
	private int NUMBER_OF_TURNS = 3;
	private int nPlayers;
	private boolean[] hasUpperBonus;
	private int[] upperScore;
	private int[] lowerScore;
	private int[] totalSum;
	private String[] playerNames;
	private int[][] scores;
	private List<Integer> arr = new ArrayList<>();
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
