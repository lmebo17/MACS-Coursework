/*
 * File: Subsequences.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Subsequences problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Given two strings, returns whether the second string is a
 * subsequence of the first string.
 */
bool isSubsequence(string text, string subsequence);

int main() {
	string text = getLine("Text: ");
	string subsequence = getLine("Subsequnce: ");
	cout << isSubsequence(text , subsequence) << endl;
    return 0;
}

bool isSubsequence(string text, string subsequence){
	if(text == ""){
		return text == subsequence;
	} else if(text[0] == subsequence[0]){
		if(isSubsequence(text.substr(1) , subsequence.substr(1))){
			return true;
		}
	} else if(isSubsequence(text.substr(1) , subsequence)){
		return true;
	}
	return false;
}
