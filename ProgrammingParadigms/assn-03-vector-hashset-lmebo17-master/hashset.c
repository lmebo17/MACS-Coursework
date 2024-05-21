#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>

static const int initialAllocation = 8;

vector getVector(const hashset* h , int position){
	return h->start[position]; 
}

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn){
			assert(elemSize > 0 && numBuckets > 0);
			assert(hashfn != NULL && comparefn != NULL);
			h->start = malloc(sizeof(vector)*numBuckets);
			assert(h->start != NULL);
			for(int i = 0; i < numBuckets; i++){
				VectorNew(h->start + i , elemSize , freefn , initialAllocation);
			}
			h->length = 0;
			h->Maxlength = numBuckets;
			h->elemSize = elemSize;
			h->compareFn = comparefn;
			h->freeFn = freefn;
			h->hashFn = hashfn;

}

void HashSetDispose(hashset *h){
	for(int i = 0; i < h->Maxlength; i++){
		VectorDispose(&h->start[i]);
	}
	free(h->start);
}

int HashSetCount(const hashset *h){ 
	return h->length; 
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData){
	assert(mapfn != NULL);
	for(int i = 0; i < h->Maxlength; i++){
		VectorMap(&h->start[i] , mapfn , auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr){
	assert(elemAddr != NULL);
	int index = h->hashFn(elemAddr , h->Maxlength);
	assert(index >= 0 && index < h->Maxlength);
	int start = 0;
	int indexInVector = VectorSearch(&h->start[index] , elemAddr , h->compareFn , start , false);
	if(indexInVector == -1){
		VectorAppend(&h->start[index] , elemAddr);
		h->length++;
	} else{
		VectorReplace(&h->start[index] , elemAddr , indexInVector);
	};
}

void *HashSetLookup(const hashset *h, const void *elemAddr){ 
	assert(elemAddr != NULL);
	int index = h->hashFn(elemAddr , h->Maxlength);
	assert(index >= 0 && index < h->Maxlength);
	int indexInVector = VectorSearch(&h->start[index] , elemAddr , h->compareFn , 0 , false);
	return indexInVector == -1 ? NULL : VectorNth(&h->start[index] , indexInVector); 
	
}
