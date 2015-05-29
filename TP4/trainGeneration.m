function out = trainGeneration(generation, inMtx, outMtx, times)
    %this value is N
    generationLength = length(generation(1, 1, 1, :));
    out = generation;

    % 'Best' values for TP2

    beta = 1;
    learningFactor = 0.01;
    alpha = 0.3;
    epsilon = 0.0001;
    funcIndex = 1;
    iterationCount = times;
    abValues = [0.01 0.1];
    consistency = 5;
    amount = 400;
    arbitrary = 0.01;
    minn = 1;
    maxx = amount;
	
    for i = 1:generationLength
        out(:, :, :, i) = perceptronTrainer(inMtx, outMtx, out(:, :, :, i), beta, learningFactor, alpha, epsilon, funcIndex, iterationCount, abValues, consistency,arbitrary, minn, maxx);
    end
end
