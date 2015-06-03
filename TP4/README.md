Genetic Algorithms

QUICK LAUNCH:

1) Create a generation: 

$> generation = newGeneration(20, [1 35 25 1]);

2) Call genetics with that generation and for our function (No 4): 

$> tp4(generation, 4, @quadraticECMFitness, @eliteSelection, 10, 30, @uniformCrossover, 0.4, @multiBitMutation, 0.15, @eliteSelection, @replacement2, 1000, 1e50, 3,0,0); 

=======================================================================

To train the perceptron using genetic algorithms you should call the octave function:
        - tp4(generation, functionIndex, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc, crossoverProbability, mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod, maxGenerations, targetFitness, trainingSeasons, structurePercentage, unmutableGenerations)
    
    being:
        - N (Int): Generation size.
        - FunctionIndex : functionIndex (OURS is 4)
        - layerSizes (array): sizes of the perceptron layers. e.g.: [1 35 25 1]
        - fitnessFunc (function): fitness funcion, one of: ECMFitness, quadraticECMFitness.
        - selectionFunc (function): selection function, one of: rouletteSelection, boltzmanSelection, universalSelection, tournamentDeterministicSelection, tournamentProbabilisticSelection, eliteSelection.
        - selectionNumber : selection of N individuals
        - mixSelectionNumber : selection of N individuals for mix selection function
        - crossoverFunc (function): crossover function, one of: onePointCrossover, twoPointCrossover, anularCrossover, uniformCrossover.
        - crossoverProbability (double): crossover probability.
        - mutationFunc (function): mutation function, one of: oneBitMutation, multiBitMutation.
        - mutationProbability (double): mutation probability.
        - replacementSelectionFunc (function): function to select the necessary individuals to reach the generation size after a first selection.
        - replacementMethod (function): replacement method, one of: replacement1, replacement2, replacement3.
        - maxGenerations (int): maximum generations before the end of the algorithm.
        - targetFitness (int): fitness to reach in the algorithm.
        - trainingSeasons (int): number of seasons to train each generation.
        - structurePercentage (double): percentage of values that, repeated, would end the algorithm.
        - unmutableGenerations (int): number of generations to wait for the maximum fitness to change.

