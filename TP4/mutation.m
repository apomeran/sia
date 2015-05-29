function out = mutation(individual, mutationFunc, mutationProbability)
    out = mutationFunc(individual, mutationProbability);
end
