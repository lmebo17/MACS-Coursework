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
void findNeighbours(Loc& current , Set<pair<double,Edge>>& data , Set<Edge>& used , int rows , int cols);
Loc findParent(Vector<Vector<Loc>>& dsu , Loc& current);
bool inBounds(int currentX , int currentY , int rows , int cols);

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

// finds a path between given locations
void route(Map<Loc , Loc> parents , Loc& start , Loc& end , Vector<Loc>& result){
	Loc tmp = end;
	while(parents[tmp] != start){
		result.insert(0 , parents[tmp]);
		tmp = parents[tmp];
	}
	result.insert(0 , start);
	result.push_back(end);
}


Set<Edge> createMaze(int numRows, int numCols) {
	Set<Edge> result;
    Vector<Vector<Loc>> dsu(numRows);
	Set<Edge> used;
	Set<pair<double ,Edge>> data;
	for(int i = 0; i < numRows; i++){
		for(int j = 0; j < numCols; j++){
			Loc current = makeLoc(i , j);
			dsu[i].add(current);
			findNeighbours(current , data , used  , numRows , numCols);
		}
	}
	while(!data.isEmpty()){
		pair<double , Edge> currentEdge = data.first();
		data.remove(data.first());
		Edge edge = currentEdge.second;
		if(findParent(dsu , edge.start) != findParent(dsu , edge.end)){
			result.add(edge);
			dsu[findParent(dsu, edge.start).row][findParent(dsu, edge.start).col] = findParent(dsu, edge.end);
		}

	}
	return result;
}

// finds unused neighbours of the current location
void findNeighbours(Loc& current , Set<pair<double,Edge>>& data , Set<Edge>& used , int rows , int cols){
	for(int m = -1; m <= 1; m++){
		for(int n = -1; n <= 1; n++){
			if(abs(m) == abs(n)) continue;
			Loc& neighbour = makeLoc(current.row + m , current.col + n);
			Edge newEdge1 = makeEdge(current , neighbour);
			Edge newEdge2 = makeEdge(neighbour , current);
			if(!inBounds(neighbour.row , neighbour.col , rows , cols) || used.contains(newEdge1) || used.contains(newEdge2)) continue;
			used.add(newEdge1);
			double cost = randomReal(0 , 1);
			pair<double , Edge> tmp;
			tmp.first = cost;
			tmp.second = newEdge1;
			data.add(tmp);
		}
	}
}

// checks whether the coordinates ar in bounds
bool inBounds(int currentX , int currentY , int rows , int cols){
	return min(currentX , currentY) >= 0 && currentY < cols && currentX < rows;
}

// finds a parent of give location
Loc findParent(Vector<Vector<Loc>>& dsu , Loc& current){
	if(dsu[current.row][current.col] == current) return current;
	return dsu[current.row][current.col] = findParent(dsu , dsu[current.row][current.col]);
}