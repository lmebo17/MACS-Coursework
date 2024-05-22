/*************************************************************
 * File: pqueue-doublylinkedlist.cpp
 *
 * Implementation file for the DoublyLinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-doublylinkedlist.h"
#include "error.h"

DoublyLinkedListPriorityQueue::DoublyLinkedListPriorityQueue() {
	logicalSize = 0;
	head = new Node;
	tail = new Node;
	head->next = tail;
	head->prev = NULL;
	tail->prev = head;
	tail->next = NULL;
}

DoublyLinkedListPriorityQueue::~DoublyLinkedListPriorityQueue() {
	Node* ptr = head;
	while(ptr){
		Node* tmp = ptr;
		ptr = ptr->next;
		delete tmp;
	}
}

int DoublyLinkedListPriorityQueue::size() {
	return logicalSize;
}

bool DoublyLinkedListPriorityQueue::isEmpty() {
	return size() == 0;
}

void DoublyLinkedListPriorityQueue::enqueue(string value) {
	Node* newNode = new Node;
	newNode->value = value;
	newNode->prev = head;
	head->next->prev = newNode;
	newNode->next = head->next;
	head->next = newNode;
	logicalSize++;
}

string DoublyLinkedListPriorityQueue::peek() {
	if(size() == 0){
		error("Sorry , I'm empty");
	}
	string result = head->next->value;
	for(Node* ptr = head->next; ptr != NULL; ptr = ptr->next){
		if(ptr == tail) continue;
		if(result > ptr->value){
			result = ptr->value;
		}
	}
	return result;
}

string DoublyLinkedListPriorityQueue::dequeueMin() {
	if(size() == 0){
		error("Sorry , I'm empty");
	}
	string result = head->next->value;
	Node* toRemove = head->next;
	for(Node* ptr = head->next; ptr != NULL; ptr = ptr->next){
		if(ptr == tail) continue;
		if(result > ptr->value){
			result = ptr->value;
			toRemove = ptr;
		}
	}
	toRemove->next->prev = toRemove->prev;
	toRemove->prev->next = toRemove->next;
	delete toRemove;
	logicalSize--;
	return result;
}

