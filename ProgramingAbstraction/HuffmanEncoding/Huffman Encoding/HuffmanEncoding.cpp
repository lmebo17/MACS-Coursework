/**********************************************************
 * File: HuffmanEncoding.cpp
 *
 * Implementation of the functions from HuffmanEncoding.h.
 * Most (if not all) of the code that you write for this
 * assignment will go into this file.
 */

#include "HuffmanEncoding.h"
#include "pqueue.h"
#include <iostream>
#include "console.h"

// Function prototypes
void fillData(Map<ext_char , int>& freqeuencies , PriorityQueue<Node*>& pq);
void compress(PriorityQueue<Node*>& pq);

/* Function: getFrequencyTable
 * Usage: Map<ext_char, int> freq = getFrequencyTable(file);
 * --------------------------------------------------------
 * Given an input stream containing text, calculates the
 * frequencies of each character within that text and stores
 * the result as a Map from ext_chars to the number of times
 * that the character appears.
 *
 * This function will also set the frequency of the PSEUDO_EOF
 * character to be 1, which ensures that any future encoding
 * tree built from these frequencies will have an encoding for
 * the PSEUDO_EOF character.
 */
Map<ext_char, int> getFrequencyTable(istream& file) {
	Map<ext_char,int> result;
	char ch;
	while(file.get(ch)){
		result[ch]++;
	}
	result[PSEUDO_EOF]++;
	return result;	
}

/* Function: buildEncodingTree
 * Usage: Node* tree = buildEncodingTree(frequency);
 * --------------------------------------------------------
 * Given a map from extended characters to frequencies,
 * constructs a Huffman encoding tree from those frequencies
 * and returns a pointer to the root.
 *
 * This function can assume that there is always at least one
 * entry in the map, since the PSEUDO_EOF character will always
 * be present.
 */
Node* buildEncodingTree(Map<ext_char, int>& frequencies) {
	PriorityQueue<Node*> pq;
	fillData(frequencies , pq);
	compress(pq);
	Node* result = pq.peek();
	return result;
}

void fillData(Map<ext_char, int>& frequencies , PriorityQueue<Node*>& pq){
	foreach(ext_char ch in frequencies){
		Node* tmp = new Node;
		tmp->character = ch;
		tmp->weight = frequencies[ch];
		tmp->zero = NULL;
		tmp->one = NULL;
		pq.enqueue(tmp , tmp->weight);
	}
}

void compress(PriorityQueue<Node*>& pq){
	while(pq.size() > 1){
		Node* first = pq.dequeue();
		Node* second = pq.dequeue();
		Node* newNode = new Node;
		newNode->character = NOT_A_CHAR;
		newNode->zero = first;
		newNode->one = second;
		newNode->weight = second->weight + first->weight;
		pq.enqueue(newNode , newNode->weight);
	}
}


/* Function: freeTree
 * Usage: freeTree(encodingTree);
 * --------------------------------------------------------
 * Deallocates all memory allocated for a given encoding
 * tree.
 */
void freeTree(Node* root) {
	if(root == NULL) return;
	freeTree(root->zero);
	freeTree(root->one);
	delete root;
}

/* Function: encodeFile
 * Usage: encodeFile(source, encodingTree, output);
 * --------------------------------------------------------
 * Encodes the given file using the encoding specified by the
 * given encoding tree, then writes the result one bit at a
 * time to the specified output file.
 *
 * This function can assume the following:
 *
 *   - The encoding tree was constructed from the given file,
 *     so every character appears somewhere in the encoding
 *     tree.
 *
 *   - The output file already has the encoding table written
 *     to it, and the file cursor is at the end of the file.
 *     This means that you should just start writing the bits
 *     without seeking the file anywhere.
 */ 


// finds path to the character
bool findPath(Node* root , ext_char ch , string& result){
	if(root == NULL) return false;
	if(root->character == ch) return true;
	result += '0';
	if(findPath(root->zero , ch , result)) return true;
	result = result.substr(0 , result.size() - 1);
	result += '1';
	if(findPath(root->one , ch , result)) return true;
	result = result.substr(0 , result.size() - 1);
	return false;
}

void helper(Node* encodingTree , ext_char ch , string& result , obstream& outfile){
	findPath(encodingTree, ch , result);
	foreach(ext_char ch in result){
		outfile.writeBit(ch == '1');
	}
}

void encodeFile(istream& infile, Node* encodingTree, obstream& outfile) {
	string tmp = "";
	while(!infile.eof()){
		ext_char ch = infile.get();
		helper(encodingTree , ch , tmp , outfile);
		tmp = "";
	}
	helper(encodingTree , PSEUDO_EOF , tmp , outfile);
	
}

/* Function: decodeFile
 * Usage: decodeFile(encodedFile, encodingTree, resultFile);
 * --------------------------------------------------------
 * Decodes a file that has previously been encoded using the
 * encodeFile function.  You can assume the following:
 *
 *   - The encoding table has already been read from the input
 *     file, and the encoding tree parameter was constructed from
 *     this encoding table.
 *
 *   - The output file is open and ready for writing.
 */


// finds leaf using bits
bool findLeaf(Node* encodingTree , string str , char& ch){
	if(encodingTree == NULL) return false;
	if(encodingTree->one == NULL && encodingTree->zero == NULL){
		if(encodingTree->character == NOT_A_CHAR || encodingTree->character == PSEUDO_EOF) return false;
		ch = encodingTree->character;
		return true;
	}
	if(str.size() == 0) return false;
	if(str[0] == '0'){
		if(findLeaf(encodingTree->zero , str.substr(1) , ch)) return true;
	}
	if(str[0] == '1'){
		if(findLeaf(encodingTree->one , str.substr(1) , ch)) return true;
	}
	return false;

}


void decodeFile(ibstream& infile, Node* encodingTree, ostream& file) {
	char ch = ' ';
	string str = "";
	while(true){
		int	bit = infile.readBit();
		if(bit < 0) break;
		str += integerToString(bit);
		if(findLeaf(encodingTree , str , ch)){
			file << ch;
			str = "";
		}
	}
}

/* Function: writeFileHeader
 * Usage: writeFileHeader(output, frequencies);
 * --------------------------------------------------------
 * Writes a table to the front of the specified output file
 * that contains information about the frequencies of all of
 * the letters in the input text.  This information can then
 * be used to decompress input files once they've been
 * compressed.
 *
 * This function is provided for you.  You are free to modify
 * it if you see fit, but if you do you must also update the
 * readFileHeader function defined below this one so that it
 * can properly read the data back.
 */
void writeFileHeader(obstream& outfile, Map<ext_char, int>& frequencies) {
	/* The format we will use is the following:
	 *
	 * First number: Total number of characters whose frequency is being
	 *               encoded.
	 * An appropriate number of pairs of the form [char][frequency][space],
	 * encoding the number of occurrences.
	 *
	 * No information about PSEUDO_EOF is written, since the frequency is
	 * always 1.
	 */
	 
	/* Verify that we have PSEUDO_EOF somewhere in this mapping. */
	if (!frequencies.containsKey(PSEUDO_EOF)) {
		error("No PSEUDO_EOF defined.");
	}
	
	/* Write how many encodings we're going to have.  Note the space after
	 * this number to ensure that we can read it back correctly.
	 */
	outfile << frequencies.size() - 1 << ' ';
	
	/* Now, write the letter/frequency pairs. */
	foreach (ext_char ch in frequencies) {
		/* Skip PSEUDO_EOF if we see it. */
		if (ch == PSEUDO_EOF) continue;
		
		/* Write out the letter and its frequency. */
		outfile << char(ch) << frequencies[ch] << ' ';
	}
}

/* Function: readFileHeader
 * Usage: Map<ext_char, int> freq = writeFileHeader(input);
 * --------------------------------------------------------
 * Reads a table to the front of the specified input file
 * that contains information about the frequencies of all of
 * the letters in the input text.  This information can then
 * be used to reconstruct the encoding tree for that file.
 *
 * This function is provided for you.  You are free to modify
 * it if you see fit, but if you do you must also update the
 * writeFileHeader function defined before this one so that it
 * can properly write the data.
 */
Map<ext_char, int> readFileHeader(ibstream& infile) {
	/* This function inverts the mapping we wrote out in the
	 * writeFileHeader function before.  If you make any
	 * changes to that function, be sure to change this one
	 * too!
	 */
	Map<ext_char, int> result;
	
	/* Read how many values we're going to read in. */
	int numValues;
	infile >> numValues;
	
	/* Skip trailing whitespace. */
	infile.get();
	
	/* Read those values in. */
	for (int i = 0; i < numValues; i++) {
		/* Get the character we're going to read. */
		ext_char ch = infile.get();
		
		/* Get the frequency. */
		int frequency;
		infile >> frequency;
		
		/* Skip the space character. */
		infile.get();
		
		/* Add this to the encoding table. */
		result[ch] = frequency;
	}
	
	/* Add in 1 for PSEUDO_EOF. */
	result[PSEUDO_EOF] = 1;
	return result;
}

/* Function: compress
 * Usage: compress(infile, outfile);
 * --------------------------------------------------------
 * Main entry point for the Huffman compressor.  Compresses
 * the file whose contents are specified by the input
 * ibstream, then writes the result to outfile.  Your final
 * task in this assignment will be to combine all of the
 * previous functions together to implement this function,
 * which should not require much logic of its own and should
 * primarily be glue code.
 */
void compress(ibstream& infile, obstream& outfile) {
	Map<ext_char , int> data = getFrequencyTable(infile);
	infile.rewind();
	Node* root = buildEncodingTree(data);
	writeFileHeader(outfile , data);
	encodeFile(infile , root , outfile);
	freeTree(root);
}

/* Function: decompress
 * Usage: decompress(infile, outfile);
 * --------------------------------------------------------
 * Main entry point for the Huffman decompressor.
 * Decompresses the file whose contents are specified by the
 * input ibstream, then writes the decompressed version of
 * the file to the stream specified by outfile.  Your final
 * task in this assignment will be to combine all of the
 * previous functions together to implement this function,
 * which should not require much logic of its own and should
 * primarily be glue code.
 */
void decompress(ibstream& infile, ostream& outfile) {
	Map<ext_char , int> data = readFileHeader(infile);
	Node* root = buildEncodingTree(data);
	decodeFile(infile , root , outfile);
	freeTree(root);
}

