function out = calculateECM(perceptron, inMtx, outMtx)
    out = 0;
    for i = 1:length(inMtx)
        [layers, result] = perceptronEval(inMtx(i, :), perceptron, 1, @sigmoid);
        out += abs(abs(result(length(result(:, 1)), 1) - outMtx(i)).^2/length(inMtx));
    end
end
