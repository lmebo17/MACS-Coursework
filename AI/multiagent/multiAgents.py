# multiAgents.py
# --------------
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


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        return betterEvaluationFunction(successorGameState)

def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    def getAction(self, gameState):
        _, best_action = self.pacmanAction(gameState, 0)  
        return best_action


    def pacmanAction(self, state, depth):
        if state.isWin() or state.isLose() or depth == self.depth:
            return self.evaluationFunction(state), None
        
        legal_actions = state.getLegalActions(0)
        max_utility = float('-inf')
        best_action = None
        
        for action in legal_actions:
            successor = state.generateSuccessor(0, action)
            score, _ = self.ghostAction(successor, depth, 1)
            if score >= max_utility:
                max_utility = score
                best_action = action
        
        return max_utility, best_action


    def ghostAction(self, state, depth, agentIndex):
        if state.isWin() or state.isLose() or depth == self.depth:
            return self.evaluationFunction(state), None
        
        legal_actions = state.getLegalActions(agentIndex)
        min_utility = float('inf')
        next_agent_index = (agentIndex + 1) % state.getNumAgents()
        
        for action in legal_actions:
            successor = state.generateSuccessor(agentIndex, action)
            score = 0
            if next_agent_index:
                score, _ = self.ghostAction(successor, depth, next_agent_index)
            else:
                score, _ = self.pacmanAction(successor, depth + 1)
            min_utility = min(min_utility, score)
        
        return min_utility, None


from multiAgents import MultiAgentSearchAgent
import util

class AlphaBetaAgent(MultiAgentSearchAgent):

    def getAction(self, gameState):
        _, best_action = self.alphaBetaPacman(gameState, 0, float('-inf'), float('inf'))
        return best_action

    ## alpha betas vamateb ubralod
    def alphaBetaPacman(self, state, depth, alpha, beta):
        if state.isWin() or state.isLose() or depth == self.depth:
            return self.evaluationFunction(state), None
        
        legal_actions = state.getLegalActions(0)
        max_utility = float('-inf')
        best_action = None
        
        for action in legal_actions:
            successor = state.generateSuccessor(0, action)
            score, _ = self.alphaBetaGhost(successor, depth, 1, alpha, beta)
            if score >= max_utility:
                max_utility = score
                best_action = action
            alpha = max(alpha, max_utility)
            if max_utility > beta: 
                return max_utility, best_action
        
        return max_utility, best_action

    def alphaBetaGhost(self, state, depth, agentIndex, alpha, beta):
        if state.isWin() or state.isLose() or depth == self.depth:
            return self.evaluationFunction(state), None
        
        legal_actions = state.getLegalActions(agentIndex)
        min_utility = float('inf')
        next_agent_index = (agentIndex + 1) % state.getNumAgents()
        
        for action in legal_actions:
            successor = state.generateSuccessor(agentIndex, action)
            if next_agent_index:
                score, _ = self.alphaBetaGhost(successor, depth, next_agent_index, alpha, beta)
            else:
                score, _ = self.alphaBetaPacman(successor, depth + 1, alpha, beta)
            min_utility = min(min_utility, score)
            beta = min(beta, min_utility)
            if min_utility < alpha:
                return min_utility, None
        
        return min_utility, None

class ExpectimaxAgent(MultiAgentSearchAgent):

    def getAction(self, gameState):
        _, best_action = self.expectiMaxPacman(gameState, 0)  
        return best_action
        util.raiseNotDefined()


    def expectiMaxPacman(self, state, depth):
        if state.isWin() or state.isLose() or depth == self.depth:
            return self.evaluationFunction(state), None
    
        legal_actions = state.getLegalActions(0)
        max_utility = float('-inf')
        best_action = None
        
        for action in legal_actions:
            successor = state.generateSuccessor(0, action)
            score, _ = self.expectiMaxGhost(successor, depth, 1)
            if score >= max_utility:
                max_utility = score
                best_action = action
        
        return max_utility, best_action


    def expectiMaxGhost(self, state, depth, agentIndex):
        if state.isWin() or state.isLose() or depth == self.depth:
            return self.evaluationFunction(state), None
        
        legal_actions = state.getLegalActions(agentIndex)
        next_agent_index = (agentIndex + 1) % state.getNumAgents()
        
        sum_utility = 0
        for action in legal_actions:
            successor = state.generateSuccessor(agentIndex, action)
            score = 0
            if next_agent_index:
                score, _ = self.expectiMaxGhost(successor, depth, next_agent_index)
            else:
                score, _ = self.expectiMaxPacman(successor, depth + 1)
            sum_utility += score
            
        avg_utility = sum_utility / len(legal_actions)
        
        return avg_utility, None


def betterEvaluationFunction(state):
    if state.isLose():
        return float('-inf')
    
    pacmanPosition = state.getPacmanPosition()
    foods = state.getFood()
    ghostStates = state.getGhostStates()

    scaredTimes = []
    for ghostState in ghostStates:
        scaredTimes.append(ghostState.scaredTimer)

    score = 0
    food_weight = 1
    ghost_weight = -10.0
    scared_ghost_weight = 15 ## meti unda iyos ghost_weightze, radgan prioritetulia.
    food_distances = []

    for food in foods.asList():
        food_distances.append(manhattanDistance(pacmanPosition, food))

    if food_distances:
        closest_food_distance = min(food_distances)
        if closest_food_distance:
            score += food_weight / closest_food_distance
        else:
            score += food_weight / (closest_food_distance + 1)  

    # darchenili sachmlebi gamovaklot, rac upro 
    # cota darcha mit upro cota gamoakldeba,rac kargia.
    number_of_food = len(foods.asList())
    score -= 2*number_of_food  
    ghost_distances = []

    for ghost in ghostStates:
        ghost_distances.append(manhattanDistance(pacmanPosition, ghost.getPosition()))

    if ghost_distances:
        closest_ghost_distance = min(ghost_distances)
        if scaredTimes[0] > 0: ## tu ghosti mkvdaria
            if scared_ghost_weight:
                score += scared_ghost_weight / (closest_ghost_distance)
            else:
                score += scared_ghost_weight / (closest_ghost_distance + 1) ##foodze tu vart ukve da 0-ze rom ar gaiyos.
        else: ## ghosti tu cocxalia, ghost_weight uaryopitia da daakldeba
            if closest_ghost_distance:
                score += ghost_weight / (closest_ghost_distance)
            else: 
                score += ghost_weight / (closest_ghost_distance + 1)
    return score

def manhattanDistance(pos1, pos2):
    return abs(pos1[0] - pos2[0]) + abs(pos1[1] - pos2[1])




# Abbreviation
better = betterEvaluationFunction
