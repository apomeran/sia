function out = quadraticECMFitness(perceptron, inMtx, outMtx)
    out = 1/(calculateECM(perceptron, inMtx, outMtx, 1, @sigmoid)).^2;
end
