function [modifiedPerceptron differ] = perceptronLearner(in, out, p, beta, learningFactor, func, derFunc, alpha, oldDiff)
    
    layerSize = length(p(1, :, 1));
    numberOfLayers = length(p(1, 1, :));
    differ = zeros(length(p(:, 1, 1)), layerSize, numberOfLayers);
    	
    % Create a matrix to store the deltas on each layer
    deltas = zeros(numberOfLayers+1, layerSize+1);
    %Evaluate for selected pattern (Feed FWD)
    [layers, result] = perceptronEval(in, p, beta, func);
    out = [out zeros(1, layerSize-length(out))];

    S = out;
    Vi = result(numberOfLayers+1, :);
    Hi = layers(numberOfLayers+1, :);
    GHi = derFunc(Hi, beta);
    last_deltas_index = numberOfLayers+1;

    % Calculate δ for OUT layer		
    deltas(last_deltas_index, :) = [0 ((GHi + 0.1)).*(S-Hi)];
    
    		

    % Calculate remaining δ for internal layers
    for (i = [numberOfLayers:-1:1])
       
        Hi = layers(i, :);
        GHi = derFunc(Hi, beta);
        next_delta = deltas(i+1, 2:layerSize+1);
        
        deltas(i, :) = ([0 (GHi)] .* ( next_delta * p(:, :, i)' ));
        deltas(i, 1) = 0;

    end

    % Update all the p weights with the backpropagation differentials
    for (i = [numberOfLayers:-1:1])
      Vi = result(i,:);
      VVi = [-1 Vi];
      delta_i = deltas(i+1, :)';

      differ(:, :, i) = (learningFactor * ( delta_i * VVi))' (:,2:layerSize+1) + (alpha * oldDiff(:, :, i));
      p(:,:,i) += differ(:, :, i);

    end

    modifiedPerceptron = p;
end
