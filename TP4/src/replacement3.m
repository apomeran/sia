function gen = replacement3(generation, inMtx, outMtx, totalFitness, relativeFitness, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc, crossoverProbability, mutationFunc, mutationProbability, replacementSelectionFunc, trainingSeasons, temperature)
    generationLength = length(generation(1, 1, 1, :)) + 1;

    rows = length(generation(:, 1, 1, 1));
    cols = length(generation(1, :, 1, 1));
    layers = length(generation(1, 1, :, 1));

    totalAux = zeros(rows, cols, layers, generationLength + selectionNumber);

    % Create the vector to put the new created children
    aux = generation(:, :, :, 1:selectionNumber);

    selected = selection(generation, selectionNumber, totalFitness, relativeFitness, selectionFunc, mixSelectionNumber, temperature);

    generated = 1;
    while (generated < selectionNumber)
        r1 = uint16((rand * (selectionNumber - 1)) + 1);
        r2 = uint16((rand * (selectionNumber - 1)) + 1);

        [child1 child2] = crossover(selected(:, :, :, r1), selected(:, :, :, r2), crossoverFunc, crossoverProbability);

        % Mutate the child with a given probability of happening.
        child1 = mutation(child1, mutationFunc, mutationProbability);
        child2 = mutation(child2, mutationFunc, mutationProbability);

        % Assign the child to the next generation and move on to generating another child
        aux(:, :, :, generated++) = child1;
        if (generated < generationLength && generated <= selectionNumber)
            aux(:, :, :, generated++) = child2;
        end
    end

    if (trainingSeasons > 0)
        aux = trainGeneration(aux, inMtx, outMtx, trainingSeasons, 0);
    end

    totalAux(:, :, :, 1:generationLength-1) = generation;
    totalAux(:, :, :, generationLength:generationLength+selectionNumber-1) = aux;

    [totalFitness relativeFitness] = calculatePopulationFitness(fitnessFunc, totalAux, inMtx, outMtx);

    gen = selection(totalAux, generationLength, totalFitness, relativeFitness, replacementSelectionFunc, mixSelectionNumber, temperature);

end
