/*
 * File: NumericConversions.cpp
 * ---------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Numeric Conversions problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Function prototypes */

string intToString(int n);
int stringToInt(string str);

/* Main program */

int main() {	
	while(true){
	string str= getLine("enter string: ");
	cout << stringToInt(str) << endl;
	int n = getInteger("enter int: ");
	cout << intToString(n) << endl;
	}
    return 0;
}

string intToString(int n){
	if(n == INT_MIN){
		return "-2147483648";
	}
	bool isNeg = n < 0;
	if(isNeg) n = -n;
	string res = "";
	if(n >= 0 && n <= 9){
		res += (char)(n + '0');
		return res;
	}
	char ch = (char)(n % 10 + '0');
	res = intToString(n / 10) + ch;
	if(isNeg){
		res = '-' + res;
	}
	return res;
}

int stringToInt(string str){	
	bool isNeg = false;
	if(str[0] == '-'){
		str = str.substr(1);
		isNeg = true;
	}
	if(str.length() == 1) return str[0] - '0';
	int res = 10 * stringToInt(str.substr(0 , str.length() - 1)) + (str[str.length() - 1] - '0');
	if(isNeg) res = -res;
	return res;
}