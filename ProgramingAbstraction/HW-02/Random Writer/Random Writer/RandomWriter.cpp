/*
 * File: RandomWriter.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Random Writer problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
#include "map.h"
#include "vector.h"
#include <fstream>
#include "random.h"
using namespace std;
void getData(ifstream& file);
void calculateProbabilities(Map<string ,Vector<char>>& map , ifstream& file , int num , string& biggest);
const int NUM = 2000;
string generateRandomText(string str , Map<string , Vector<char>>& map);
int getMarkov();

int main() {
	ifstream file;
	getData(file);
	int markovNum = getMarkov();
	Map<string , Vector<char>> map;
	string biggest = "";
	calculateProbabilities(map , file , markovNum , biggest);
	string result = generateRandomText(biggest , map);
	cout << result << endl;
	return 0;
}

// generates the markov input untill , the number is between one and ten.
int getMarkov(){
	int res = getInteger("Enter the Markov order [1-10]: ");
	while(res < 0 || res > 10){
		res = getInteger("Please , enter in range of 1-10: ");
	}
	return res;
}

// tries to open file untill the real file name is entered.
void getData(ifstream& file){
	string filename = getLine("Enter the source text: ");
	file.open(filename.c_str());
	while(!file.is_open()){
		filename = getLine("Unable to open that file. Try again. ");
		file.open(filename.c_str());
	}
}

// calculates probabilities of getting each characters after the specific strings 
// and saves gathered data in map.
void calculateProbabilities(Map<string ,Vector<char>>& map , ifstream& file , int num , string& biggest){
	char ch;
	int count = 0;
	int size = INT_MIN;
	string word = "";
	bool reached = false;
	while(file.get(ch)){
		if(count == num){
			reached = true;
			if(!map.containsKey(word)){
				Vector<char> vc;
				vc.add(ch);
				map[word] = vc;	
			} else {
				map[word].add(ch);
			}
			int tmp = map[word].size();
			if(size < tmp){
				size = tmp;
				biggest = word;
			}
			word = word.substr(1 , word.size() - 1) + ch;
		}
		if(!reached){
			word += ch;
			count++;
		}	
	}
}

// generates new text using the data servered in map.
string generateRandomText(string str ,Map<string , Vector<char>>& map) {
	string result = str;
	for(int i = 0; i < (int)(NUM - str.size()); i++){
		int index = randomInteger(0 , map[str].size() - 1);
		char ch = map[str].get(index);
		result += ch;
		str = str.substr(1 , str.size() - 1) + ch;
	}
	return result;
}





