function out = selection(gen, k, totalFitness, relativeFitness, selectionFunc, n1, temperature)
    out = selectionFunc(gen, totalFitness, relativeFitness, k, n1, temperature);
end
