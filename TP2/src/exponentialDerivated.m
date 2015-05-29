function resp = exponentialDerivated(x, b)
    resp = 2*b*exp(b*x)./((exp(b*x) + 1).^2);
end
