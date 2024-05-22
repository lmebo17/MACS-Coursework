/*
 * File: InverseGenetics.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Inverse Genetics problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include <fstream>
#include "set.h"
#include "map.h"
#include "console.h"
using namespace std;

/* Function: listAllRNAStrandsFor(string protein,
 *                                Map<char, Set<string> >& codons);
 * Usage: listAllRNAStrandsFor("PARTY", codons);
 * ==================================================================
 * Given a protein and a map from amino acid codes to the codons for
 * that code, lists all possible RNA strands that could generate
 * that protein
 */
void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons);

/* Function: loadCodonMap();
 * Usage: Map<char, Lexicon> codonMap = loadCodonMap();
 * ==================================================================
 * Loads the codon mapping table from a file.
 */
Map<char, Set<string> > loadCodonMap();

int main() {
    Map<char, Set<string> > codons = loadCodonMap();
	listAllRNAStrandsFor("KWS" , codons);
	cout << " " << endl;
    
    return 0;
}

/* You do not need to change this function. */
Map<char, Set<string> > loadCodonMap() {
    ifstream input("codons.txt");
    Map<char, Set<string> > result;

    /* The current codon / protein combination. */
    string codon;
    char protein;

    /* Continuously pull data from the file until all data has been
     * read.
     */
    while (input >> codon >> protein) {
        result[protein] += codon;
    }

    return result;
}

// prints generated codons recursively
void helper(string protein , Map<char , Set<string> >& codons , int index , string soFar , int size){
	if(index == size){
		cout << soFar << endl;
	} else{
		Set<string> st = codons[protein[index]];
		foreach(string tmp in st){
			helper(protein , codons , index + 1 , soFar + tmp , size);
		}	
	}
}


void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons){
	int index = 0;
	int size = protein.size();
	helper(protein , codons , index , "" , size);
}
