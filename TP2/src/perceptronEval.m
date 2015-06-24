function [layers, resp] = perceptronEval(in, p, beta, func)
    global betas;
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
        if (i == numLayers+1)
	  resp(i, :) = funcLineal(layers(i, :), betas(i,:));
        else 
          resp(i, :) = funcPosta(layers(i, :), betas(i,:));
        end
        nextLayer = [-1 resp(i, :)];
    end

     %Saturated Neurons
           if (false)
             count=0;
             for (i = [2:numLayers])
              for (j = [1:layerSize])
	          if (abs(resp(i,j)) > 0.98)
                     betas(i,j) -= 0.01;
                     
                     %resp(i,j);
                     count++;
                     %printf("Neuron i,j -> %d,%d Saturated \n",i,j);
                  end
                  
	      end
             end
            end
            
end
