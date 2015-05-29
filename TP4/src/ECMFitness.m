function out = ECMFitness(perceptron, inMtx, outMtx)
    out = 1/calculateECM(perceptron, inMtx, outMtx, 1, @sigmoid);
end
