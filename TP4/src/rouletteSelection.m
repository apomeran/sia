function out = rouletteSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
    % Generate the vector of accumulated fitness.
    accumulatedFitness = cumsum(relativeFitness);

    % Create the out vector, then replace its content.
    out = generation(:, :, :, 1:k);

    % Start the roulette!
    for i = 1:k
        out(:, :, :, i) = generation(:, :, :,  min(find(accumulatedFitness > rand)));
    end
end
