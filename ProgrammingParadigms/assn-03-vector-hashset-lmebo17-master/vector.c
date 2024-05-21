#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <search.h>


void grow(vector* v){
    v->Maxlength += v->toAdd;
    v->start = realloc(v->start , v->Maxlength*v->elemSize);
    assert(v != NULL);
}

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation){
     assert(elemSize > 0);
     assert(initialAllocation >= 0);
     if(initialAllocation == 0) initialAllocation = 8;
     v->Maxlength = initialAllocation;
     v->toAdd = initialAllocation;
     v->length = 0;
     v->elemSize = elemSize;
     v->freeFn = freeFn;
     v->start = malloc(initialAllocation * elemSize);
     assert(v->start != NULL);
}

void VectorDispose(vector *v){
    if(v->freeFn != NULL){
        for(int i = 0; i < v->length; i++){
            v->freeFn(VectorNth(v , i));    
        }
    }
    free(v->start);
}

int VectorLength(const vector *v){ 
    return v->length; 
}

void *VectorNth(const vector *v, int position){ 
    assert(position >= 0 && position < v->length);
    return (void*)((char*)v->start + position*v->elemSize); 
}

void VectorReplace(vector *v, const void *elemAddr, int position){
    if(v->length <= position && position < 0) return;
    if(v->freeFn != NULL) v->freeFn(VectorNth(v , position));
    memcpy(VectorNth(v , position) , elemAddr , v->elemSize);
}


void VectorInsert(vector *v, const void *elemAddr, int position){
    assert(position >= 0 && position <= v->length);
    if(v->length == v->Maxlength) grow(v);
    if(position == v->length){
        VectorAppend(v , elemAddr);
        return;
    }
    void* curr = VectorNth(v , position);
    void* newPos = (void*)((char*)curr + v->elemSize);
    memmove(newPos , curr , (v->length - position)*v->elemSize);
    memcpy(curr , elemAddr , v->elemSize);
    v->length++;
}


void VectorAppend(vector *v, const void *elemAddr){
    if(v->length == v->Maxlength) grow(v);
    void* curr = (void*)((char*)v->start + v->length*v->elemSize);
    memcpy(curr, elemAddr , v->elemSize);
    v->length++;
}


void VectorDelete(vector *v, int position){
    assert(position >= 0 && position < v->length);
    void* curr = VectorNth(v , position);
    if(v->freeFn != NULL) v->freeFn(curr);
    memmove(curr ,(void*)((char*)curr + v->elemSize) , (v->length - position - 1) * v->elemSize);
    v->length--;
    
}

void VectorSort(vector *v, VectorCompareFunction compare){
    assert(compare != NULL);
    qsort(v->start , v->length , v->elemSize , compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData){
    assert(mapFn != NULL);
    for(int i = 0; i < v->length; i++){
        mapFn(VectorNth(v , i) , auxData);
    }
}

static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted){ 
    assert(startIndex >= 0 && startIndex <= v->length);
    assert(key != NULL && searchFn != NULL);
    void* res = NULL;
    int num = v->length - startIndex;
    if(startIndex != v->length){
        if(isSorted){
            res = bsearch(key , VectorNth(v , startIndex), num , v->elemSize , searchFn);
        } else {
            res = lfind(key , VectorNth(v , startIndex) , &num , v->elemSize , searchFn);
        }
    }
    return res == NULL ? kNotFound : ((char*)res - (char*)v->start)/v->elemSize;
} 
