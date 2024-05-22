/*
 * File: FleschKincaid.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Flesch-Kincaid problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "tokenscanner.h"
#include "simpio.h"
#include "filelib.h"
using namespace std;
const double C0 = -15.59;
const double C1 = 0.39;
const double C2 = 11.8;

void readFile(ifstream& file);
void scanFile(TokenScanner& tk);
void count(int& words , int& sentences , int& syllables , TokenScanner& tk);
int countSyllables(string& token);
double countGrade(int words , int sentences , int syllables);
bool isVowel(char ch);
bool isEndOfSentence(string& str);


int main() {
	ifstream file;
	readFile(file);	
	TokenScanner tk(file);
	scanFile(tk);
	int words = 0;
	int sentences = 0;
	int syllables = 0;
	count(words , sentences , syllables , tk);
	double grade = countGrade(words , sentences , syllables);
	cout << "words: " << words << endl;
	cout << "sentences: " << sentences << endl;
	cout << "syllables: " << syllables << endl;
	cout << "grade: " << grade << endl;
    return 0;
}

void readFile(ifstream& file){
	string fileName = getLine("Please , enter a file name: ");
	file.open(fileName.c_str());
	while(file.fail()){
		cout << "Please , enter a valid name of a file: ";
		fileName = getLine();
		file.open(fileName.c_str());
	}
}

void scanFile(TokenScanner& tk){
	tk.ignoreWhitespace();
	tk.addWordCharacters("'");
	
}


// counts words , syllables and sentences using tokenscanner
void count(int& words , int& sentences , int& syllables , TokenScanner& tk){
	while(tk.hasMoreTokens()){
		string token = tk.nextToken();
		if(isalpha(token[0])){
			words++;
			syllables += countSyllables(token);
		}
		if(isEndOfSentence(token)){
			sentences++;
		}
	}
	if(sentences == 0 && words == 0){
		sentences++;
		words++;
	}
}

bool isEndOfSentence(string& str){
	return (str == "." || str == "!" || str == "?");
}



int countSyllables(string& token){
	int syllables = 0;
	bool lastWasVowel = false;
	for(int i = 0; i <(int)token.size() - 1; i++){
		if(isVowel(token[i]) && !lastWasVowel){  
			syllables++;
			lastWasVowel = true;
		} else if(!isVowel(token[i])){
			lastWasVowel = false;
		}
	}
	if(token.length() > 1){
		char lastLetter = tolower(token[token.length() - 1]);
		char beforeLast = tolower(token[token.length() - 2]);
		if(!isVowel(beforeLast) && isVowel(lastLetter) && lastLetter != 'e'){
			syllables++;
		}
	}
	if(syllables == 0){
		syllables++;
	}
	return syllables;
}




bool isVowel(char ch){
	ch = tolower(ch);
	return (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'y');
}

double countGrade(int words , int sentences , int syllables){
	double grade = C0 + C1 * words/sentences + C2 * syllables / words;
	return grade;
}

