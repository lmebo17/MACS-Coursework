/*************************************************************
 * File: pqueue-vector.cpp
 *
 * Implementation file for the VectorPriorityQueue
 * class.
 */
 
#include "pqueue-vector.h"
#include "error.h"

VectorPriorityQueue::VectorPriorityQueue() {
	logicalSize = 0;
}

VectorPriorityQueue::~VectorPriorityQueue() {

}

int VectorPriorityQueue::size() {
	return logicalSize;
}

bool VectorPriorityQueue::isEmpty() {
	return size() == 0;
}

void VectorPriorityQueue::enqueue(string value) {
	data.add(value);
	logicalSize++;
}

string VectorPriorityQueue::peek() {
	if(size() == 0){
		error("There are no elements in a queue");
	}
	string result = data[0];
	foreach(string str in data){
		if(result > str){
			result = str;
		}
	}
	return result;
}

string VectorPriorityQueue::dequeueMin() {
	if(size() == 0){
		error("There are no elements in a queue");
	}
	string result = data[0];
	int index = 0;
	for(int i = 0; i < data.size(); i++){
		if(result > data[i]){
			result = data[i];
			index = i;
		}
	}
	data.remove(index);
	logicalSize--;
	return result;
}

