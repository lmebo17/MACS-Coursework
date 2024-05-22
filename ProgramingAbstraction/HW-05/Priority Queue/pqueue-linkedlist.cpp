/*************************************************************
 * File: pqueue-linkedlist.cpp
 *
 * Implementation file for the LinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-linkedlist.h"
#include "error.h"

LinkedListPriorityQueue::LinkedListPriorityQueue() {
	logicalSize = 0;
	head = new Node;
	head->next = NULL;
}

LinkedListPriorityQueue::~LinkedListPriorityQueue() {
	Node* ptr = head;
	while(ptr){
		Node* tmp = ptr;
		ptr = ptr->next;
		delete tmp;
	}
}

int LinkedListPriorityQueue::size() {
	return logicalSize;
}

bool LinkedListPriorityQueue::isEmpty() {
	return size() == 0;
}

void LinkedListPriorityQueue::enqueue(string value) {
	Node* newNode = new Node;
	newNode->value = value;
	logicalSize++;
	if(size() == 0){
		head->next = newNode;
		newNode->next = NULL;
		return;
	}
	Node* prev = head;
	for(Node* ptr = head->next; ptr != NULL; ptr = ptr->next){
		if(value < ptr->value){
			prev->next = newNode;
			newNode->next = ptr;
			return;
		}
		prev = ptr;
	}
	prev->next = newNode;
	newNode->next = NULL;
}

string LinkedListPriorityQueue::peek() {
	if(size() == 0) error("empty");
	return head->next->value;
}

string LinkedListPriorityQueue::dequeueMin() {
	string result = peek();
	Node* ptr = head->next;
	head->next = head->next->next;
	delete ptr;
	logicalSize--;
	return result;
}

