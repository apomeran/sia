function out = trainGeneration(generation, inMtx, outMtx, times, isInitial)
    %this value is N
    generationLength = length(generation(1, 1, 1, :));
    out = generation;

    % 'Best' values for TP2

 
    %5,[1 35 25 1] , 1, 0.25, 0.9, 0.000001,1,50000,[0.01 0.01], 5, 0
    beta = 1;
    
    if (isInitial == 1)
     learningFactor = 0.25;
    else
     learningFactor = 0.02;
    end
    alpha = 0.9;
    epsilon = 0.000001;
    funcIndex = 1;
    iterationCount = times;
    abValues = [0.01 0.01];
    consistency = 5;
    arbitrary = 0;
	
    for i = 1:generationLength
         do
         [p lastDiff] = perceptronTrainer(inMtx, outMtx, out(:, :, :, i), beta, learningFactor, alpha, epsilon, funcIndex, iterationCount, abValues, consistency,arbitrary, inMtx, outMtx);
         until (lastDiff < 0.5)
         out(:, :, :, i) = p;
         1/(lastDiff^2) %FITNESS FOR THIS GENERATION
    end
end
