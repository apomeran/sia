function out = multiBitMutation(individual, mutationProbability)
    rows = length(individual(:, 1, 1));
    cols = length(individual(1, :, 1));
    layers = length(individual(1, 1, :));

    mutationMatrix = rand(rows, cols, layers);

    out = individual;

    for row = 1:rows
        for col = 1:cols
            for layer = 1:layers
                if (out(row, col, layer) != 0)
                    if (mutationMatrix(row, col, layer) < mutationProbability)
                        out(row, col, layer) = rand - 0.5;
                    end
                end
            end
        end
    end
end
