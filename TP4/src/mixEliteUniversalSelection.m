function out = mixEliteRouletteSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
    % Create the out vector, then replace its content.
    out = generation(:, :, :, 1:k);

    out(:, :, :, 1:n1) = eliteSelection(generation, totalFitness, relativeFitness, n1, n1);
    out(:, :, :, n1+1:k) = universalSelection(generation, totalFitness, relativeFitness, k - n1, n1);
end
