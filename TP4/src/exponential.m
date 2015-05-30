function resp = exponential(xx, betaa)
    resp = 2./(1+exp(-1*betaa*xx)) - 1;
end
