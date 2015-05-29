Genetic Algorithms

    To train the perceptron using genetic algorithms you should call the octave function:
        - genetics(N, functionIndex, layerSizes, inMtx, outMtx, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc, crossoverProbability, mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod, maxGenerations, targetFitness, trainingSeasons, structurePercentage, unmutableGenerations)
    
    being:
        - N (Int): Generation size.
        - FunctionIndex : functionIndex (OURS is 1 . Test with 5)
        - layerSizes (array): sizes of the perceptron layers. e.g.: [2 5 4 1]
        - inMtx(array): Input values to train the perceptron.
        - outMtx (array): Output values to train the perceptron.
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

QUICK LAUNCH:
tp4(30, 5, [1 25 1], @quadraticECMFitness, @eliteSelection, 3, 3, @onePointCrossover, 0.3, @oneBitMutation, 0.01, @eliteSelection, @replacement1, 150, 10e10, 3, 0, 10)

    
Loading functions into octave workspace:
    Once inside the octave client you should load the functions using the command "load <files>" for the next files:
    - archiveParser.m
    - genetics.m
    - newGeneration.m
    - *Crossover.m
    - *Selection.m
    - replacement*.m
    - *Mutation.m

Being *Crossover.m every archive that ends with Crossover.m


