/******************************************************************************
 * File: Trailblazer.cpp
 *
 * Implementation of the graph algorithms that comprise the Trailblazer
 * assignment.
 */

#include "Trailblazer.h"
#include "TrailblazerGraphics.h"
#include "TrailblazerTypes.h"
#include "TrailblazerPQueue.h"
#include "random.h"
#include "vector.h"
#include <limits>
using namespace std;


/* Function Prototypes */
void route(Map<Loc , Loc> parents , Loc& start , Loc& end , Vector<Loc>& result);
void findNeighbours(Loc& current , TrailblazerPQueue<Edge>& pq , Set<Edge>& used , int rows , int cols);
bool inBounds(int currentX , int currentY , int rows , int cols);
bool sameClusters(Map<Loc , Set<Loc>>& tmp , Loc& start , Loc& end);

/* Function: shortestPath
 * 
 * Finds the shortest path between the locations given by start and end in the
 * specified world.	 The cost of moving from one edge to the next is specified
 * by the given cost function.	The resulting path is then returned as a
 * Vector<Loc> containing the locations to visit in the order in which they
 * would be visited.	If no path is found, this function should report an
 * error.
 *
 * In Part Two of this assignment, you will need to add an additional parameter
 * to this function that represents the heuristic to use while performing the
 * search.  Make sure to update both this implementation prototype and the
 * function prototype in Trailblazer.h.
 */

Vector<Loc>
shortestPath(Loc start,
             Loc end,
             Grid<double>& world,
			 double costFn(Loc from, Loc to, Grid<double>& world), double heuristic(Loc start , Loc end , Grid<double>& world)) {
	Vector<Loc> result;
	Map<Loc , double> minimalCosts;
	Map<Loc , Loc> parents;
	TrailblazerPQueue<Loc> data;
	Set<Loc> used;
	minimalCosts[start] = 0;
	data.enqueue(start , heuristic(start , end , world));
	while(!data.isEmpty()){
		Loc current = data.dequeueMin();
		colorCell(world , current , GREEN); 
		used.add(current);
		if(current == end) break;
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				if((i!=0 || j!=0) && world.inBounds(current.row + i , current.col + j)){
					Loc neighbour;
					neighbour.row = current.row + i;
					neighbour.col = current.col + j;
					if(used.contains(neighbour)) continue;		
					if(minimalCosts.containsKey(neighbour)){
						if(minimalCosts[current] + costFn(current , neighbour , world) < minimalCosts[neighbour]){
							parents[neighbour] = current;
							minimalCosts[neighbour] = minimalCosts[current] + costFn(current , neighbour , world);
							data.decreaseKey(neighbour , minimalCosts[neighbour] + heuristic(neighbour , end , world));
						}
					} else {
						parents[neighbour] = current;
						minimalCosts[neighbour] = minimalCosts[current] + costFn(current , neighbour , world);
						data.enqueue(neighbour , minimalCosts[neighbour] + heuristic(neighbour , end , world));
						if(minimalCosts[neighbour] != numeric_limits<double>::infinity()) colorCell(world , neighbour , YELLOW);
					}
				}
			}
		}
	}
	route(parents , start , end , result);
    return result;
}
// finds the route from start to the end
void route(Map<Loc , Loc> parents , Loc& start , Loc& end , Vector<Loc>& result){
	Loc tmp = end;
	while(parents[tmp] != start){
		result.insert(0 , parents[tmp]);
		tmp = parents[tmp];
	}
	result.insert(0 , start);
	result.push_back(end);
}

// creates maze with the given dimensions
Set<Edge> createMaze(int numRows, int numCols) {
	Set<Edge> result;
	Map<Loc , Set<Loc>> data;
	int clusters = numRows*numCols;
	for(int i  = 0 ; i < numRows; i++){
		for(int j = 0; j < numCols; j++){
			Loc tmp = makeLoc(i , j);
			data[tmp].add(tmp);
		}
	}
	Set<Edge> used;
	TrailblazerPQueue<Edge> pq;
	for(int i = 0; i < numRows; i++){
		for(int j = 0; j < numCols; j++){
			Loc current = makeLoc(i , j);
			findNeighbours(current , pq , used  , numRows , numCols);
		}
	}
	while(clusters >= 2 && pq.size() > 0){
		Edge current = pq.dequeueMin();
		if(!sameClusters(data , current.start , current.end)){
			Set<Loc> newCluster = data[current.start] + data[current.end];
			foreach(Loc tmp in newCluster){
				data[tmp] = newCluster;
			}
			result.add(current);
			clusters--;
		}
		
	}
	return result;
}

// checks whether two nodes are connected in any way
bool sameClusters(Map<Loc , Set<Loc>>& tmp , Loc& start , Loc& end){
	foreach(Loc current in tmp){
		if(tmp[current].contains(start) && tmp[current].contains(end)) return true;
	}
	return false;
}

// makes an edge between unconnected neighbourhood
void findNeighbours(Loc& current , TrailblazerPQueue<Edge>& pq , Set<Edge>& used , int rows , int cols){
	for(int m = -1; m <= 1; m++){
		for(int n = -1; n <= 1; n++){
			if(abs(m) == abs(n)) continue;
			Loc& neighbour = makeLoc(current.row + m , current.col + n);
			Edge newEdge1 = makeEdge(current , neighbour);
			Edge newEdge2 = makeEdge(neighbour , current);
			if(!inBounds(neighbour.row , neighbour.col , rows , cols) || used.contains(newEdge1) || used.contains(newEdge2)) continue;
			used.add(newEdge1);
			double cost = randomReal(0 , 1);
			pq.enqueue(newEdge1 , cost);
				
		}
	}
}

// checks if the coordinates are in bound
bool inBounds(int currentX , int currentY , int rows , int cols){
	return min(currentX , currentY) >= 0 && currentY < cols && currentX < rows;
}

