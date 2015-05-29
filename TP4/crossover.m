function [child1 child2] = crossover(p1, p2, crossoverFunc, crossoverProbability)
    % TODO: See what happens if there is no crossover
    child1 = p1;
    child2 = p2;
    if (rand < crossoverProbability)
        [child1 child2] = crossoverFunc(p1, p2);
    end
end
