function out = oneBitMutation(individual, mutationProbability)
    out = individual;
    if (rand < mutationProbability)
        epsilon = 0.0001;

        rows = length(individual(:, 1, 1));
        cols = length(individual(1, :, 1));
        layers = length(individual(1, 1, :));
        
        out = individual;

        row = floor(rand * rows) + 1; 
        col = floor(rand * cols) + 1; 
        layer = floor(rand * layers) + 1; 

        while abs(individual(row, col, layer) - 0) < epsilon;
            row = floor(rand * rows) + 1; 
            col = floor(rand * cols) + 1; 
            layer = floor(rand * layers) + 1; 
        end

        out(row, col, layer) = rand - 0.5;
    end
end
