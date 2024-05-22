/*
 * File: Combinations.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Combinations problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
using namespace std;
int combination(int n , int k);

int main() {
	int n = getInteger();
	int k = getInteger();
	cout << combination(n , k) << endl;
    return 0;
}

int combination(int n , int k){
	if(k == 0 || k == n) return 1;
	return combination(n - 1 , k) + combination(n - 1 , k - 1);
}
