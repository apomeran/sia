function out = eliteSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
    [sortedFitness maxIndexes] = sort(totalFitness, 'descend'); %oct
    out = generation(:, :, :, maxIndexes(1:k));
end
