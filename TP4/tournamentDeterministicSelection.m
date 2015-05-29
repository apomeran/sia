function out = tournamentDeterministicSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
    % Create the out vector, then replace its content.
    out = generation(:, :, :, 1:k);

    for i = 1:k
        elem1 = floor(rand * length(generation(1, 1, 1, :)) + 1);
        elem2 = floor(rand * length(generation(1, 1, 1, :)) + 1);

        if totalFitness(elem1) > totalFitness(elem2)
            out(:, :, :, i) = generation(:, :, :, elem1);
        else
            out(:, :, :, i) = generation(:, :, :, elem2);
        end
    end
end
