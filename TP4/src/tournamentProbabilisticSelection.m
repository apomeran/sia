function out = tournamentProbabilisticSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
    % Create the out vector, then replace its content.
    out = generation(:, :, :, 1:k);

    probability = 0.75;

    for i = 1:k
        elem1 = floor(rand * length(generation(1, 1, 1, :)) + 1);
        elem2 = floor(rand * length(generation(1, 1, 1, :)) + 1);

        if totalFitness(elem1) > totalFitness(elem2)
            moreApt = generation(:, :, :, elem1);
            lessApt = generation(:, :, :, elem2);
        else
            moreApt = generation(:, :, :, elem2);
            lessApt = generation(:, :, :, elem1);
        end

        if rand < probability
            out(:, :, :, i) = moreApt;
        else
            out(:, :, :, i) = lessApt;
        end
    end
end
