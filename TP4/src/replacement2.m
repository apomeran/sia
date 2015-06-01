function gen = replacement2(generation, inMtx, outMtx, totalFitness, relativeFitness, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc, crossoverProbability, mutationFunc, mutationProbability, replacementSelectionFunc, trainingSeasons, temperature)
    generationLength = length(generation(1, 1, 1, :)) + 1;

    gen = generation;

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
        gen(:, :, :, generated++) = child1;
        if (generated < generationLength && generated <= selectionNumber)
            gen(:, :, :, generated++) = child2;
        end
    end

    if (trainingSeasons > 0)
        gen(:, :, :, 1:selectionNumber) = trainGeneration(gen(:, :, :, 1:selectionNumber), inMtx, outMtx, trainingSeasons,0);
    end

    remaining = generationLength - selectionNumber - 1;
    replacements = selection(generation, remaining, totalFitness, relativeFitness, replacementSelectionFunc, mixSelectionNumber, temperature);

    gen(:, :, :, selectionNumber+1:generationLength-1) = replacements;

end
