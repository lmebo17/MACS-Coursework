# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

from util import Stack

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem):
    stack = util.Stack()
    visited = set()
    stack.push((problem.getStartState(), []))
    while not stack.isEmpty():
        currState, path = stack.pop()
        if currState in visited: continue
        visited.add(currState)
        if problem.isGoalState(currState):
            return path
        successors = problem.getSuccessors(currState)
        for nextState, step, _ in successors: 
            if nextState not in visited:
                stack.push((nextState, path + [step]))
    return []


def breadthFirstSearch(problem):
    queue = util.Queue()
    visited = []
    queue.push((problem.getStartState(), [], 0))
    while not queue.isEmpty():
        currState, path, cost = queue.pop()
        if currState in visited: continue
        visited.append(currState)
        if problem.isGoalState(currState):
            return path
        successors = problem.getSuccessors(currState)
        for nextState, step, stepCost in successors:
            if nextState not in visited:
                queue.push((nextState, path + [step], cost + stepCost))
    return []

import util

def uniformCostSearch(problem):
    queue = util.PriorityQueue()
    visited = set()
    priorities = {}
    queue.push((problem.getStartState(), [], 0), 0)
    priorities[problem.getStartState()] = 0
    while not queue.isEmpty():
        currState, path, cost = queue.pop()
        if currState in visited:
            continue
        visited.add(currState)
        if problem.isGoalState(currState):
            return path
        successors = problem.getSuccessors(currState)
        for nextState, step, stepCost in successors:
            totalCost = cost + stepCost
            if nextState not in priorities:
                priorities[nextState] = totalCost
                queue.push((nextState, path + [step], totalCost), totalCost)
            elif totalCost < priorities[nextState]:
                priorities[nextState] = totalCost
                queue.update((nextState, path + [step], totalCost), totalCost)
    return []


def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem, heuristic=nullHeuristic):
    queue = util.PriorityQueue()
    queue.push((problem.getStartState(), [], 0), heuristic(problem.getStartState(), problem))  
    visited = []
    # priorities = {}     
    # priorities[problem.getStartState()] = 0
    while not queue.isEmpty():
        currState, path, cost = queue.pop()
        if currState in visited: continue
        if problem.isGoalState(currState): return path
        visited.append(currState)
        successors = problem.getSuccessors(currState)
        for nextState, action, stepCost in successors:
            totalCost = cost + stepCost
            heuristicEstimate = totalCost + heuristic(nextState, problem)
            if nextState not in visited:
                # priorities[nextState] = heuristicEstimate    
                queue.push((nextState, path + [action], totalCost), heuristicEstimate)
            else: queue.update((nextState, path + [action], totalCost), heuristicEstimate)
            # elif heuristicEstimate < priorities[nextState]:
            #     priorities[nextState] = heuristicEstimate
    return []


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
