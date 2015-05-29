function out = randomSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
    randVector = rand(length(totalFitness),1);
    [garbage index] = sort(randVector);
    out = generation(:, :, :, index(1:k));
end
