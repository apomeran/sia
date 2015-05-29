function out = sigmoidDerivated(x, b)
    out = b*(1 - sigmoid(x, b).^2);
end
