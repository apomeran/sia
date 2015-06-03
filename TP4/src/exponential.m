function resp = exponential(xx, beta)
    resp = 2./(1+exp(-1*beta*xx)) - 1;
end
