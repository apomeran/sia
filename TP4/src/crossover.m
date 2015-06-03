function [child1 child2] = crossover(Perceptron1, Perceptron2, crossoverFunc, crossoverProbability)
    child1 = Perceptron1;
    child2 = Perceptron2;
    %wrapper
    if (rand < crossoverProbability)
        [child1 child2] = crossoverFunc(Perceptron1, Perceptron2);
    end
end
