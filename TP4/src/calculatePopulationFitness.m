function [total relative] = calculatePopulationFitness(fitnessFunc, population, inMtx, outMtx)
    total = relative = zeros(1, length(population(1, 1, 1, :)));
    for i = 1:length(population(1, 1, 1, :))
       %wrapper 
       total(i) = calculateFitness(fitnessFunc, population(:, :, :, i), inMtx, outMtx);
    end
    relative = total./sum(total);
end
