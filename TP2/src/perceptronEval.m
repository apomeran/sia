function [layers, resp] = perceptronEval(in, p, beta, func)
    layerSize = length(p(1, :, 1));

    numLayers = length(p(1, 1, :));
    layers = zeros(numLayers+1, layerSize);
    resp = zeros(numLayers+1, layerSize);

    extraLayer = [in zeros(1, layerSize-length(in))];
    nextLayer = [-1 extraLayer];
    layers(1, :) = extraLayer;

    funcPosta = func;
    funcLineal = @identity;
    resp(1, :) = layers(1, :);
    for (i = [2:numLayers+1])
        layers(i, :) = [nextLayer*p(:, :, i-1)];
        resp(i, :) = funcPosta(layers(i, :), beta);
        nextLayer = [-1 resp(i, :)];
    end
end
