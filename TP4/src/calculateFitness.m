function out = calculateFitness(fitnessFunc, perceptron, inMtx, outMtx)
    %wrapper
    out = fitnessFunc(perceptron, inMtx, outMtx);
end
