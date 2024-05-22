/*
 * File: Sierpinski.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Sierpinski problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "gwindow.h"
#include "simpio.h"
#include <cmath>
using namespace std;

// draws the sierpenski zero level triangle
void drawTriangle(int triangleSize , GWindow& gw){
	int height = gw.getHeight()/2;
	int width = gw.getWidth()/2;
	double triangleHeight = triangleSize * (sqrt(3.0)/2);
	gw.drawPolarLine(width - triangleSize / 2 , height + triangleHeight/2 , triangleSize , 60);
	gw.drawPolarLine(width + triangleSize /2 , height + triangleHeight/2 , triangleSize , 120);
	gw.drawPolarLine(width - triangleSize /2 , height + triangleHeight /2 , triangleSize , 0);
}


// draws the sierpenski levels recursively
void wrapper(int triangleSize , GWindow& gw ,int NUM , int turn, double height , double width , double triangleHeight){
	if(turn == NUM) {
		return;
	} else {
		gw.drawPolarLine(width - triangleSize / 4, height , triangleSize/2, 0);
		gw.drawPolarLine(width, height + triangleHeight/2 , triangleSize/2 , 120);
		gw.drawPolarLine(width, height + triangleHeight/2 , triangleSize/2 , 60);
		wrapper(triangleSize/2 , gw , NUM , turn + 1, height - triangleHeight/4, width , triangleHeight/2);
		wrapper(triangleSize/2 , gw , NUM , turn + 1, height + triangleHeight/4, width - triangleSize /4 , triangleHeight/2);
		wrapper(triangleSize/2 , gw , NUM , turn + 1, height + triangleHeight/4 , width + triangleSize/4 , triangleHeight /2);
	}
}

void drawSierpinski(int triangleSize , GWindow& gw , int NUM){
	double height = gw.getHeight()/2;
	double width = gw.getWidth()/2;
	double triangleHeight = triangleSize * (sqrt(3.0)/2);
	wrapper(triangleSize , gw , NUM ,0 ,height , width ,triangleHeight);
}

int main() {
	GWindow gw;
	int triangleSize = getInteger("Enter the size of a triangle: ");
	while(triangleSize < 0){
		triangleSize = getInteger("Enter the size of a triangle: ");
	}
	int NUM = getInteger("Enter the level of fractal: ");
	while(NUM < 0){
		NUM = getInteger("Enter the level of fractal: ");
	}
	drawTriangle(triangleSize , gw);
	drawSierpinski(triangleSize , gw , NUM);

	return 0;
}
