function out = stats(perceptron, learnInMtx, learnOutMtx, testInMtx, testOutMtx, beta, func, diffe)

  calculatedTestValuesY = zeros(length(testInMtx), 1);

  for (i = 1:length(testInMtx))
    [layers, result] = perceptronEval(testInMtx(i, :), perceptron, beta, func);
    calculatedValue = result(length(result(:, 1)), 1);
    calculatedTestValuesY(i, 1) = calculatedValue;
    trueValuesTestX = testInMtx(:, 1);
  end

  trueValuesX = testInMtx(:, 1);
  trueValuesY = testOutMtx(:, 1);

 % plot both functions, original and calculated one.
 subplot(2,1,1);  
 plot(trueValuesX, trueValuesY, '-', "markersize", 2, trueValuesTestX, calculatedTestValuesY, '-', "markersize",4);
 %subplot(2,1,2);
 %plot(diffe);
% refresh
 drawnow();
end
