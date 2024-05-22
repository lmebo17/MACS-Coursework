/*************************************************************
 * File: pqueue-heap.cpp
 *
 * Implementation file for the HeapPriorityQueue
 * class.
 */
 
#include "pqueue-heap.h"
#include "error.h"

HeapPriorityQueue::HeapPriorityQueue() {
	data = new string[10];
	maxCapacity = 10;
	logicalSize = 0;
}

HeapPriorityQueue::~HeapPriorityQueue() {
	delete[] data;
}

int HeapPriorityQueue::size() {	
	return logicalSize;
}

bool HeapPriorityQueue::isEmpty() {	
	return size() == 0;
}

void HeapPriorityQueue::enqueue(string value) {
	if(size() == maxCapacity){
		resize(data , maxCapacity*2);
	}
	data[logicalSize] = value;
	int tmp = logicalSize;
	while(true){
		if(tmp == 0) break;
		if(value < data[(tmp - 1)/2]){
			string parent = data[(tmp-1)/2];
			data[(tmp-1)/2] = value;
			data[tmp] = parent;
			tmp = (tmp-1)/2;
		} else break;
	}
	logicalSize++;
}

string HeapPriorityQueue::peek() {
	if(size() == 0){
		error("Sorry , I'm empty");
	}
	return data[0];
}

string HeapPriorityQueue::dequeueMin() {
	if(size() == 0){
		error("Sorry , I'm empty");
	}
	string result = data[0];
	string toChange = data[logicalSize-1];
	data[0] = toChange;
	data[logicalSize-1] = result;
	logicalSize--;
	int tmp = 0;
	while(true){
		// bubble down
		if(tmp*2 + 1 < logicalSize && tmp*2 + 2 >= logicalSize){
			if(data[tmp] > data[tmp*2 + 1]){
				string son = data[tmp*2+1];
				data[tmp*2+1] = data[tmp];
				data[tmp] = son;
				continue;
			}
		}
		if(2*tmp + 2 >= logicalSize) break;
		if(data[tmp] < min(data[2*tmp+1] , data[2*tmp+2])) break;
		// bubble up
		if(data[tmp] >= data[2*tmp + 1]){
			if(data[2*tmp + 1] <= data[2*tmp+2]){
				string parent = data[tmp];
				string son = data[2*tmp + 1];
				data[tmp] = son;
				data[2*tmp + 1] = parent;
				tmp = 2*tmp + 1;
				continue;
			}
		}
		if(data[tmp] >= data[2*tmp + 2] && data[2*tmp + 2] <= data[2*tmp + 1]){
			string parent = data[tmp];
			string son = data[2*tmp + 2];
			data[tmp] = son;
			data[2*tmp + 2] = parent;
			tmp = 2*tmp + 2;
			continue;
		}
	}
	return result;
}

void HeapPriorityQueue::resize(string* &data , int size){
	string* newData = new string[size];
	for(int i = 0; i < logicalSize; i++){
		newData[i] = data[i];
	}
	maxCapacity = size;
	string* ptr = data;
	data = newData;
	delete[] ptr;
}

