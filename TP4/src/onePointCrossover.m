function [s1 s2] = onePointCrossover(p1, p2)

  r = length(p1(:, 1, 1));
  c = length(p1(1, :, 1));
  l = length(p1(1, 1, :));

  serializedP1 = serializePerceptron(p1);
  serializedP2 = serializePerceptron(p2);

  selectedPoint = uint16((sort(rand(1, 2)) * (length(serializedP1) - 1)) + 1);

  s1 = serializedP1;
  s2 = serializedP2;

  s1(selectedPoint(1):selectedPoint(2)) = serializedP2(selectedPoint(1):selectedPoint(2));
  s2(selectedPoint(1):selectedPoint(2)) = serializedP1(selectedPoint(1):selectedPoint(2));

  s1 = deserializePerceptron(s1, r, c, l);
  s2 = deserializePerceptron(s2, r, c, l);
end
