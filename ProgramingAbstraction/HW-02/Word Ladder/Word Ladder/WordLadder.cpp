/*
 * File: WordLadder.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Word Ladder problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "set.h"
#include "vector.h"
#include "queue.h"
#include "lexicon.h"
#include "simpio.h"
using namespace std;
Vector<string> generatePath(string start , string end);
void generateNewWords(Lexicon& lexicon , string lastWord , Vector<string>& generatedWords);
void visualizePath(string start , string end);

int main() {
	while(true){
		string start = getLine("Enter start word (Return to quit): ");
		if(start == "") break;
		string end = getLine("Enter destination word: ");
		visualizePath(start , end);
	}
    cout << endl;
	return 0;
}


// visualizes generated path.
void visualizePath(string start ,string end){
	Vector<string> vc = generatePath(start , end);
	if(vc.size() == 1){
		cout << "No ladder found" << endl;
		return;
	}
	cout << "Searching..." << endl;
	cout << "Found ladder: ";
	foreach(string word in vc){
		cout << word << " ";
	}
	cout << endl;
}

// tries to find path between two strings using BFS algorithm
Vector<string> generatePath(string start , string end){
	Lexicon lexicon("EnglishWords.dat"); 
	Set<string> usedWords;  
	Queue<Vector<string>> wordLadders;
	Vector<string> firstLadder;
	firstLadder.add(start);
	wordLadders.enqueue(firstLadder);
	while(!wordLadders.isEmpty()){
		Vector<string> path = wordLadders.dequeue();
		string word = path.get(path.size() - 1);
		if(word == end){
			return path;
		}
		Vector<string> generatedWords;
		generateNewWords(lexicon , word , generatedWords);
		foreach(string newWord in generatedWords){
			if(!usedWords.contains(newWord)){
				Vector<string> newPath = path;
				newPath.add(newWord);
				wordLadders.enqueue(newPath);
				usedWords.add(newWord);
			}
		}
	}

	return firstLadder;
}

// generates and saves new words existing in english lexicon.
void generateNewWords(Lexicon& lexicon , string lastWord , Vector<string>& generatedWords){
	for(int i = 0; i < lastWord.size(); i++){
		string copyOfWord = lastWord;
		for(char ch = 'a'; ch <='z'; ch++){
			copyOfWord[i] = ch;
			if(lexicon.contains(copyOfWord)){
				generatedWords.add(copyOfWord);
			}	
		}
	}

}

