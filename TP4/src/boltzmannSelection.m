function out = boltzmannSelection(generation, totalFitness, relativeFitness, k, n1, temperature)

    boltzmannValues = exp(totalFitness / temperature)/mean(exp(totalFitness / temperature));

    out = rouletteSelection(generation, totalFitness, boltzmannValues, k, n1, temperature);
end
