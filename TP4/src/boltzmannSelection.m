function out = boltzmannSelection(generation, totalFitness, relativeFitness, k, n1, temperature)
   %calculate the boltzmann value
    boltzmannValues = exp(totalFitness / temperature)/mean(exp(totalFitness / temperature));
   
 % call roulette with boltzmann value
   out = rouletteSelection(generation, totalFitness, boltzmannValues, k, n1, temperature);
end
