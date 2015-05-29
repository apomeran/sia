function gen = newGeneration(N, layerSizes) 
    maxSize = max(layerSizes);
    gen = zeros(maxSize + 1, maxSize, length(layerSizes) - 1, N);
    for i=1:N
        gen(:, :, :, i) = perceptronFactory(layerSizes);
    end
end
