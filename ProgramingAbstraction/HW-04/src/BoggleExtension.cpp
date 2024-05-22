/*
 * File: Boggle.cpp
 * ----------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the main starter file for Assignment #4, Boggle.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <vector>
#include "gboggle.h"
#include "grid.h"
#include "gwindow.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
using namespace std;

/* Constants */

const int BOGGLE_WINDOW_WIDTH = 650;
const int BOGGLE_WINDOW_HEIGHT = 350;

const string STANDARD_CUBES[16]  = {
	"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
 
const string BIG_BOGGLE_CUBES[25]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

/* created handly */
const string SUPER_BIG_BOGGLE_CUBES[36]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM","EEGHNW",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW","ELRTTY",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT","HIMNQU",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU","EEINSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU","EHRTVW",
	"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW","HLNNRZ"
};

/* Function prototypes */

void welcome();
void giveInstructions();
int size();
string stringToLower(string s);
vector<string> arrayToVector(string str);
void fillGrid(Grid<char>& board);
void initializeBoard(Grid<char>& board);
void fillBoard(Grid<char>& board , vector<char>& chars);
void shuffleVector(vector<char>& vc);
bool helper(Grid<char>& board , string word ,int index , int row , int col , Lexicon& data , Grid<bool>& visited , vector<pair<int,int>>& directions);
void computerWords(Grid<char>& board , int row , int col , Set<string>& usedWords , string soFar , Lexicon& data , Grid<bool>& usedIndexes);
bool canGet(Grid<char>& board , string word , Lexicon& data);
void enteringWords(Grid<char>& board , string& answer);
void computerHelper(Grid<char>& board , Set<string>& usedWords , Lexicon& data);
void prints();
void game(string& answer);
/* Main program */

int main() {
    GWindow gw(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    initGBoggle(gw);
    welcome();
    giveInstructions();
	string answer = "yes";
	while(answer == "yes"){
		game(answer);
	}
    return 0;
}

/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */

void welcome() {
    cout << "Welcome!  You're about to play an intense game ";
    cout << "of mind-numbing Boggle.  The good news is that ";
    cout << "you might improve your vocabulary a bit.  The ";
    cout << "bad news is that you're probably going to lose ";
    cout << "miserably to this little dictionary-toting hunk ";
    cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}

/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */

void giveInstructions() {
    cout << endl;
    cout << "The boggle board is a grid onto which I ";
    cout << "I will randomly distribute cubes. These ";
    cout << "6-sided cubes have letters rather than ";
    cout << "numbers on the faces, creating a grid of ";
    cout << "letters on which you try to form words. ";
    cout << "You go first, entering all the words you can ";
    cout << "find that are formed by tracing adjoining ";
    cout << "letters. Two letters adjoin if they are next ";
    cout << "to each other horizontally, vertically, or ";
    cout << "diagonally. A letter can only be used once ";
    cout << "in each word. Words must be at least four ";
    cout << "letters long and can be counted only once. ";
    cout << "You score points based on word length: a ";
    cout << "4-letter word is worth 1 point, 5-letters ";
    cout << "earn 2 points, and so on. After your puny ";
    cout << "brain is exhausted, I, the supercomputer, ";
    cout << "will find all the remaining words and double ";
    cout << "or triple your paltry score." << endl << endl;
    cout << "Hit return when you're ready...";
    getLine();
}

// [TODO: Fill in the rest of the code]

// simulation of a game
void game(string& answer){
	int boardSize = size();
	Grid<char> board(boardSize , boardSize);
	drawBoard(board.numRows() , board.numCols());
	initializeBoard(board);
	fillGrid(board);
	enteringWords(board , answer);
}

// player enters the words
void enteringWords(Grid<char>& board , string& continuing){
	cout << "Ok, take all the time you want and find all the words you can! Signal that you're finished by entering an empty line. " << endl;
	Lexicon data("EnglishWords.dat");
	Set<string> usedWords;
	while(true){
		cout << "Enter the word: ";
		string word = getLine();
		word = stringToLower(word);
		if(word == ""){
			computerHelper(board , usedWords , data);
			cout << "Would you like to play again? ";
			continuing = getLine();
			return;
		}
		bool isUsed = usedWords.contains(word);
		bool getWord = canGet(board , word , data);
		if(isUsed){
			cout << "You have already guessed that word. " << endl;
		}
		if(!getWord){
			cout << "You can't get that word. " << endl;
		}
		if(!isUsed && getWord){
			usedWords.add(word);
			recordWordForPlayer(word , HUMAN);
			cout << "You guessed a word. " << endl;
		}
		
	}
}

// is a helper method for computing all the words existing in english lexicon
void computerHelper(Grid<char>& board , Set<string>& usedWords , Lexicon& data){
	string soFar = "";
	Grid<bool> usedIndexes(board.numRows() , board.numCols());
	for(int i = 0; i < board.numRows(); i++){
		for(int j = 0; j < board.numCols(); j++){
			computerWords(board , i , j , usedWords , soFar , data , usedIndexes);
		}
	}
}

// computes all the words that can be got from the grid
void computerWords(Grid<char>& board , int row , int col , Set<string>& usedWords , string soFar , Lexicon& data , Grid<bool>& usedIndexes){
	if(!data.containsPrefix(soFar)) return;
	if(soFar.size() >= 4 && !usedWords.contains(soFar) && data.contains(soFar)){
		usedWords.add(soFar);
		recordWordForPlayer(soFar , COMPUTER);
	}
	if(row < 0 || col < 0 || row >= board.numRows() || col >= board.numCols()){
		return;
	}
	if(usedIndexes[row][col]) return;
	usedIndexes[row][col] = true;
	computerWords(board , row + 1, col , usedWords , soFar + board[row][col] , data , usedIndexes);
	computerWords(board , row + 1, col + 1 , usedWords , soFar + board[row][col] , data , usedIndexes);
	computerWords(board , row + 1, col - 1 , usedWords , soFar + board[row][col] , data , usedIndexes);
	computerWords(board , row , col + 1, usedWords , soFar + board[row][col] , data , usedIndexes);
	computerWords(board , row , col - 1 , usedWords , soFar + board[row][col] , data , usedIndexes);
	computerWords(board , row - 1, col - 1, usedWords , soFar + board[row][col] , data , usedIndexes);
	computerWords(board , row - 1, col , usedWords , soFar + board[row][col] , data , usedIndexes);
	computerWords(board , row - 1, col + 1 , usedWords , soFar + board[row][col] , data , usedIndexes);
	usedIndexes[row][col] = false;
}

// asks player to decide the size of the boggle
int size(){
	cout << "Would you like Big Boggle? ";
	string answer = getLine();
	answer = stringToLower(answer);
	if(answer == "no") return 4;
	int gamesize = 0;
	if(answer == "yes"){
		cout << "What size do you want ? 5 or 6 ?: ";
		while(gamesize < 5 || gamesize > 6){
			gamesize = getInteger();
		}
	}
	return gamesize;
}
	
// converts string to lowercase
string stringToLower(string s){
	string result = "";
	for(int i = 0; i < s.size(); i++){
		result += tolower(s[i]);
	}
	return result;
}

// converts an array to vector
vector<string> arrayToVector(string str){
	vector<string> result;
	if(str == "BIG"){
		for(int i = 0; i < 25; i++){
			result.push_back(BIG_BOGGLE_CUBES[i]);
		}
	}
	if(str == "STANDART"){
		for(int i = 0; i < 16; i++){
			result.push_back(STANDARD_CUBES[i]);
		}
	}
	if(str == "SUPERBIG"){
		for(int i = 0; i < 36; i++){
			result.push_back(SUPER_BIG_BOGGLE_CUBES[i]);
		}
	}
	return result;
}

void prints(int n){
	cout << "Enter a " << integerToString(n*n) << "-character string to identify which letters you want on the cubes." << endl;
	cout << "The first 4 letters are the cubes on the top row from left to right, the next 4 letters are the second row, and so on. " << endl;
	cout << "Enter the string: ";
}

// initialization of the board with the specific configurations decided by the user
void initializeBoard(Grid<char>& board){
	vector<string> words;
	cout << "I'll give you a chance to set up the board to your specification, which makes it easier to confirm your boggle program is working. " << endl;
	cout << "Do you want to force the board configuration? " ;
	string answer = getLine();
	vector<char> chars;
	if(stringToLower(answer) == "yes") {
		if(board.numCols() == 4){
			prints(4);
			string characters = getLine();
			while(characters.size() != 16){
				cout << "Enter the string with the size of 16: ";
				characters = getLine();
			}
			for(int i = 0; i < characters.size(); i++){
				chars.push_back(characters[i]);
			}
		}
		if(board.numCols() == 5){
			prints(5);
			string characters = getLine();
			while(characters.size() != 25){
				cout << "Enter the string with the size of 25: ";
				characters = getLine();
			}
			for(int i = 0; i < characters.size(); i++){
				chars.push_back(characters[i]);
			}
		}
		if(board.numCols() == 6){
			prints(6);
			string characters = getLine();
			while(characters.size() != 36){
				cout << "Enter the string with the size of 36: ";
				characters = getLine();
			}
			for(int i = 0; i < characters.size(); i++){
				chars.push_back(characters[i]);
			}
		}
	}
	if(stringToLower(answer) == "no"){
		if(board.numCols() == 5) words = arrayToVector("BIG");
		if(board.numCols() == 4) words = arrayToVector("STANDART");
		if(board.numCols() == 6) words = arrayToVector("SUPERBIG");
		for(int i = 0; i < words.size(); i++){
			int index = randomInteger(0 , words[i].size()-1);
			chars.push_back(words[i][index]);
		}
		shuffleVector(chars);
	}
	fillBoard(board , chars);
}

// fills the board with the generated characters
void fillBoard(Grid<char>& board , vector<char>& chars){
	for(int i = 0; i < board.numRows(); i++){
		for(int j = 0; j < board.numCols(); j++){
			if(chars.size() > 0){
				board[i][j] = chars.front();
				chars.erase(chars.begin());
			}
		}
	}
}

// shuffles the charachters randomly in a vector
void shuffleVector(vector<char>& vc){
	for(int i = 0; i < vc.size(); i++){
		int index = randomInteger(0 , vc.size() - 1);
		char current = vc[i];
		char swapper = vc[index];
		vc[i] = swapper;
		vc[index] = current;
	}
}

// fills the board with generated characters
void fillGrid(Grid<char>& board){
	for(int i = 0; i < board.numRows(); i++){
		for(int j = 0; j < board.numCols(); j++){
			labelCube(i , j , board[i][j]);
		}
	}

}

// graphs the path used for creating the specific word
void graphPath(vector<pair<int , int> >& directions){
	for(int i = 0; i < directions.size(); i++){
		pair<int , int> current = directions[i];
		highlightCube(current.first , current.second , true);
		pause(150);
		highlightCube(current.first , current.second , false);
	}


}

// returns true if its possible to create a word using the board
bool canGet(Grid<char>& board , string word , Lexicon& data){
	if(word.size() < 4) return false;
	int index = 0;
	Grid<bool> visited(board.numRows() , board.numCols());
	vector<pair<int,int>> directions;
	for(int i = 0; i < board.numRows(); i++){
		for(int j = 0; j < board.numCols(); j++){
			if(helper(board , word , index , i , j , data , visited , directions)){
				graphPath(directions);
				return true;
			}
		}
	}
	return false;
}

// it's a helper method for finding out the possibility of creating a word with grid using backtraking
bool helper(Grid<char>& board , string word ,int index , int row , int col , Lexicon& data , Grid<bool>& visited , vector<pair<int,int> >& directions){
	if(index == word.size() && data.contains(word)) return true;
	if(row < 0 || col < 0 || row >= board.numRows() || col >= board.numCols()) return false;
	if(visited[row][col]) return false;
	if(board[row][col] != word[index]){
		return false; 
	} else{
		visited[row][col] = true;
		pair<int , int> current;
		current.first = row;
		current.second = col;
		directions.push_back(current);
		if(helper(board , word , index + 1, row + 1, col , data , visited , directions)) return true;
		if(helper(board , word , index + 1, row + 1, col + 1 , data , visited , directions)) return true;
		if(helper(board , word , index + 1, row + 1, col - 1 , data , visited , directions)) return true;
		if(helper(board , word , index + 1, row , col + 1 , data , visited , directions)) return true;
		if(helper(board , word , index + 1, row , col - 1 , data , visited , directions)) return true;
		if(helper(board , word, index + 1, row - 1, col , data , visited , directions)) return true;
		if(helper(board , word , index + 1, row - 1, col + 1 , data , visited , directions)) return true;
		if(helper(board, word ,index + 1, row - 1, col - 1 , data , visited , directions)) return true;
		visited[row][col] = false;
		directions.pop_back();
	}
	return false;
}





