function [s1 s2] = uniformCrossover(p1, p2)

    p = 0.5;

    r = length(p1(:, 1, 1));
    c = length(p1(1, :, 1));
    l = length(p1(1, 1, :));

    serializedP1 = serializePerceptron(p1);
    serializedP2 = serializePerceptron(p2);

    for i = 1:length(serializedP1)
      if (rand < p)
        aux = serializedP1(i);
        serializedP1(i) = serializedP2(i);
        serializedP2(i) = aux;
      end
    end

    s1 = deserializePerceptron(serializedP1, r, c, l);
    s2 = deserializePerceptron(serializedP2, r, c, l);
end
