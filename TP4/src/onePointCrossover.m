function [s1 s2] = onePointCrossover(p1, p2)

    r = length(p1(:, 1, 1));
    c = length(p1(1, :, 1));
    l = length(p1(1, 1, :));

    serializedP1 = serializePerceptron(p1);
    serializedP2 = serializePerceptron(p2);

    selectedPoint = floor((rand * length(serializedP1)) + 1);

    s1 = serializedP1;
    s2 = serializedP2;

    s1(1:selectedPoint) = p2(1:selectedPoint);
    s2(1:selectedPoint) = p1(1:selectedPoint);

    s1 = deserializePerceptron(s1, r, c, l);
    s2 = deserializePerceptron(s2, r, c, l);
end
