function out = universalSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
    %Generate the vector of accumulated fitness.
    acumFitness = cumsum(relativeFitness);
    r = rand;
    %Create the out vector, then replace its content.
    out = generation(:, :, :, 1:k);

    for j = 1:k;
        r = (r + j - 1) / k;
        out(:, :, :, j) = generation(:, :, :, min(find(acumFitness > r)));
    end
end
