/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import acm.util.*;
import acmx.export.java.io.FileReader;

public class HangmanLexicon {
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	public ArrayList<String> arr = new ArrayList<String>();

/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		int count = rgen.nextInt(0 , arr.size());
		return count;
	} 
	//this method reads words from lexicon and then adds them to the array list.
	public HangmanLexicon(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("HangmanLexicon.txt"));		
			while(true) {
				String saveWord = reader.readLine();
				if(saveWord == null) break;	
				arr.add(saveWord);
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		String word = arr.get(index);
		return word;
	} 
}
