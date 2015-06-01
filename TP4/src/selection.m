function out = selection(generation, k, totalFitness, relativeFitness, selectionFunc, n1, temperature)
    out = selectionFunc(generation, totalFitness, relativeFitness, k, n1, temperature);
end
