/*
 * File: ConsecutiveHeads.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Consecutive Heads problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "random.h"
using namespace std;
int simulation();
const double p = 0.5;

int main() {	
	cout << "It took " << simulation() << " flips to get 3 consecutive heads " <<endl;
    return 0;
}

int simulation(){
	int count = 0;
	int consecutiveCount = 0;	
	while(consecutiveCount != 3){
		if(randomChance(p)){
			cout << "heads" << endl;
			consecutiveCount++;
		} else {
			cout << "tails" << endl;
			consecutiveCount = 0;
		} 
		count++;
	}	
	return count;
}
