function gen = replacement1(generation, inMtx, outMtx, totalFitness, relativeFitness, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc, crossoverProbability, mutationFunc, mutationProbability, replacementSelectionFunc, trainingSeasons, temperature)

    % For this method, select 2 parents and combine until N children are generated.
    selectionNumber = 2;

    generationLength = length(generation(1, 1, 1, :)) + 1;

    gen = generation;

    generated = 1;
    while (generated < generationLength)
        % Compute the selection to cross for the next generation.
        selected = selection(generation, selectionNumber, totalFitness, relativeFitness, selectionFunc, mixSelectionNumber, temperature);

        % Cross the 2 parents to generate a child.
        [child1 child2] = crossover(selected(:, :, :, 1), selected(:, : ,: ,2), crossoverFunc, crossoverProbability);

        % Mutate the child with a given probability of happening.
        child1 = mutation(child1, mutationFunc, mutationProbability);
        child2 = mutation(child2, mutationFunc, mutationProbability);

        % Assign the child to the next generation and move on to generating another child
        gen(:, :, :, generated++) = child1;
        if (generated < generationLength)
            gen(:, :, :, generated++) = child2;
        end
    end
    if (trainingSeasons > 0)
        trainGeneration(gen, inMtx, outMtx, trainingSeasons);
    end

end
