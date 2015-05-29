function [s1 s2] = anularCrossover(p1, p2)

  r = length(p1(:, 1, 1));
  c = length(p1(1, :, 1));
  l = length(p1(1, 1, :));

  serializedP1 = serializePerceptron(p1);
  serializedP2 = serializePerceptron(p2);

  selectedPoint = uint16((sort(rand(1, 2)) * (length(serializedP1) - 1)) + 1);

  s1 = serializedP1;
  s2 = serializedP2;

  for i = 0:selectedPoint(2)
    index = mod(selectedPoint(1) +i ,length(serializedP1)) + 1;
    s1(index) = serializedP2(index);
    s2(index) = serializedP1(index);
  end


  s1 = deserializePerceptron(s1, r, c, l);
  s2 = deserializePerceptron(s2, r, c, l);
end
