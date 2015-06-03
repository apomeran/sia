function [auxSerializedPerceptron1 auxSerializedPerceptron2] = anularCrossover(p1, p2)
  r = length(p1(:, 1, 1));
  c = length(p1(1, :, 1));
  l = length(p1(1, 1, :));

  serializedPerceptron1 = serializePerceptron(p1);
  serializedPerceptron2 = serializePerceptron(p2);

  selectedPoint = uint16((sort(rand(1, 2)) * (length(serializedPerceptron1) - 1)) + 1);

  auxSerializedPerceptron1 = serializedPerceptron1;
  auxSerializedPerceptron2 = serializedPerceptron2;

  for i = 0:selectedPoint(2)
    index = mod(selectedPoint(1) +i ,length(serializedPerceptron1)) + 1;
    auxSerializedPerceptron1(index) = serializedPerceptron2(index);
    auxSerializedPerceptron2(index) = serializedPerceptron1(index);
  end

  auxSerializedPerceptron1 = deserializePerceptron(auxSerializedPerceptron1, r, c, l);
  auxSerializedPerceptron2 = deserializePerceptron(auxSerializedPerceptron2, r, c, l);
end
