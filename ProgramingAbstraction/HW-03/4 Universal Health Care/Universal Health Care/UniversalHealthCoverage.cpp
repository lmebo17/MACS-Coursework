/*
 * File: UniversalHealthCoverage.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the UniversalHealthCoverage problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */
#include <iostream>
#include <string>
#include "set.h"
#include "vector.h"
#include "console.h"
using namespace std;

/* Function: canOfferUniversalCoverage(Set<string>& cities,
 *                                     Vector< Set<string> >& locations,
 *                                     int numHospitals,
 *                                     Vector< Set<string> >& result);
 * Usage: if (canOfferUniversalCoverage(cities, locations, 4, result)
 * ==================================================================
 * Given a set of cities, a list of what cities various hospitals can
 * cover, and a number of hospitals, returns whether or not it's
 * possible to provide coverage to all cities with the given number of
 * hospitals.  If so, one specific way to do this is handed back in the
 * result parameter.
 */
bool canOfferUniversalCoverage(Set<string>& cities,
                               Vector< Set<string> >& locations,
                               int numHospitals,
                               Vector< Set<string> >& result);


// generates all the subsets of the locations with length less then numHospitals.
Vector<Vector<Set<string>>> subsets(Vector<Set<string>> locations , int numHospitals){
	Vector<Vector<Set<string>>> vc;
	if(locations.size() == 0){
		Vector<Set<string>> tmp;
		vc.add(tmp);
		return vc;
	}
	Set<string> st = locations[0];
	locations.remove(0);
	Vector<Vector<Set<string>>> temporaryVc = subsets(locations, numHospitals);
	vc = temporaryVc;
	for(int i = 0; i < temporaryVc.size(); i++){
		Vector<Set<string>> copy = temporaryVc[i]; 
		copy.add(st);
		if(copy.size() <= numHospitals){
			vc.add(copy);
		}
	}
	return vc;
}


// iterates through the generated subsets and checks whether the concatenation of the subsets is equall to city
bool canOfferUniversalCoverage(Set<string>& cities , Vector< Set<string> >& locations, int numHospitals, Vector< Set<string> >& result){
	Vector<Vector<Set<string>>> subset = subsets(locations , numHospitals);
	foreach(Vector<Set<string>> collection in subset){
		Set<string> res;
		foreach(Set<string> st in collection){
			res += st;
			result.add(st);
		}
		if(res == cities){
			return true;
		} else result.clear();
	}
	return false;
}


int main() {
	Set<string> cities;
	Vector<Set<string>> locations;
	cities.add("A");
	cities.add("B");
	cities.add("C");
	cities.add("D");
	Set<string> s1; s1.add("A");
	Set<string> s2; s2.add("A"); s2.add("D"); s2.add("C");
	Set<string> s3; s3.add("D"); s3.add("B"); s3.add("C");
	locations.add(s1); locations.add(s2); locations.add(s3);
	int numHospitals = 2;
	Vector<Set<string>> result;
	if(canOfferUniversalCoverage(cities , locations , numHospitals , result)){
		cout << result.toString() << endl;
	} 
	cout << "" << endl;
    return 0;
}
