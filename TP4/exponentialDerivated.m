function out = exponentialDerivated(x, b)
    out = 2*b*exp(b*x)./((exp(b*x) + 1).^2);
end
