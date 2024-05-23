
/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Hangman extends ConsoleProgram {

	private HangmanLexicon lex;
	private HangmanCanvas canvas;
	private String str;
	private char letter = ' ';
	private boolean changed = false;
	private String decodedWord = "";
	private String word = "";
	private int TURNS = 8;
	private boolean gameOver = false;
	private static final int MAX_TURNS = 8;

	// this method splits the program to two sides and adds canvas on the right side of the program. 
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
		this.setSize(800, 650);
		
	}

	public void run() {
		while(true) {
			startWord();
			gameImplementation();
			pause(2000);
		}
	}

	// this method is a body of the game .
	// decoded word is the non end version of the deliberately defined word.	
	private void gameImplementation() {
		println("Welcome to Hangman!");
		println("The word now looks like this" + cypheredWord());
		println("You have 8 guesses left.");
		decodedWord = cypheredWord();
		TURNS = MAX_TURNS;
		gameOver = false;
		canvas.reset();
		while (!gameOver) {
			changed = false;
			print("Your guess : ");
			letter = readChar();
			checkCharEquality();
			if (!changed) {
				canvas.noteIncorrectGuess(letter);
				canvas.constructionCases(MAX_TURNS - TURNS);
			}	
			casedPrints();			
		}
	}

	//this method compares two chars and returns true if this chars are equal 
	//or if one of them can be got from the second's changing to lower or to it's upper case.
	private boolean isEqual(char a, char b) {
		return a == b || b == a - 'a' + 'A';
	}

	//this method generates a random word from the hangman lexicon.
	private String startWord() {
		HangmanLexicon lex = new HangmanLexicon();
		int index = lex.getWordCount();
		word = lex.getWord(index);
		return word;
	}
	//this method is the same as readLine method for strings , but readChar method is used for chars.
	private char readChar() {
		str = readLine();
		while (str.length() != 1 || !checkChar(str.charAt(0))) {
			print("Please , enter a letter : ");
			str = readLine();
		}
		char letter = str.charAt(0);
		return letter;
	}

	//this method turns the initial word into dashes.
	private String cypheredWord() {
		String cypher = "";
		for (int i = 0; i < word.length(); i++) {
			cypher = cypher + '-';
		}
		return cypher;
	}

	//this method checks if the entered character is a letter.
	private boolean checkChar(char ch) {
		return Character.isLetter(ch);
	}

	//this method compares an entered letter and chars of the word .
	private void checkCharEquality() {
		changed = false;
		canvas.displayWord(decodedWord);
		for (int i = 0; i < word.length(); i++) {
			if (isEqual(letter, word.charAt(i))) {
				letter = word.charAt(i);
				decodedWord = decodedWord.substring(0, i) + word.charAt(i) + decodedWord.substring(i + 1);
				canvas.displayWord(decodedWord);
				changed = true;
			}
		}
	}

	//the following methods prints specially designated texts for each appropriate cases.
	private void printFirstCase() {
		println("There are no " + letter + "'S in the word.");
		println("The word now looks like this: " + decodedWord);
		println("You have " + TURNS + " guesses left.");
	}

	private void printSecondCase() {
		println("There are no " + letter + "'S in the word.");
		println("The word now looks like this: " + decodedWord);
		println("You have only one guess left.");
	}

	private void printThirdCase() {
		println("That guess is correct.");
		println("You guessed the word: " + word);
		println("You win.");
	}

	private void printFourthCase() {
		if(TURNS > 1) {
			println("Your guess is correct.");
			println("The word now looks like this: " + decodedWord);
			println("You have " + TURNS + " guesses left.");
		} else {
			println("Your guess is correct.");
			println("The word now looks like this: " + decodedWord);
			println("You have only one guess left.");
		}
	}

	private void printFifthCase() {
		println("There are no " + letter + "'S in the word.");
		println("You're completely hung");
		println("The word was: " + word);
		println("You lose.");
	}
	
	private void casedPrints() {
		letter = Character.toUpperCase(letter);
		if (TURNS > 2 && !changed) {
			TURNS--;
			printFirstCase();
		} else if (TURNS == 2 && !changed) {
			TURNS--;
			printSecondCase();
		} else if (TURNS > 0 && decodedWord.equals(word)) {
			printThirdCase();
			gameOver = true;
		} else if (TURNS > 0 && changed) {
			printFourthCase();
		} else if (TURNS == 1 && !(decodedWord.equals(word))) {
			printFifthCase();
			gameOver = true;
		}
		
	}
}
