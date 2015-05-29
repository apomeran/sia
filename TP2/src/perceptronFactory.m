function out = perceptronFactory(layerSize)
    maxSize = max(layerSize);
    out = rand(maxSize + 1, maxSize, length(layerSize) - 1) - 0.5;
    for(i = [1:length(layerSize)-1])
       thisLayerSize = layerSize(i);
       nextLayerSize = layerSize(i+1);
       out(:, nextLayerSize+1:maxSize, i) = zeros(maxSize + 1, maxSize - nextLayerSize);
       out(thisLayerSize+2:maxSize+1, :, i) = zeros(maxSize - thisLayerSize, maxSize);
    endfor
endfunction
