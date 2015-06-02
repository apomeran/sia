function out = stats(perceptron, learnInMtx, learnOutMtx, testInMtx, testOutMtx, beta, func, diffe)

  trainedTotalError = 0; generalizedTotalError = 0; minimunTrainingError = Inf; maximumTrainingError = 0;

  minimumTestError = Inf; maximumTestError = 0;

  calculatedTrainValuesY = zeros(length(learnInMtx), 1);
  calculatedTestValuesY = zeros(length(testInMtx), 1);

  for (i = 1:length(learnInMtx))
    [layers, result] = perceptronEval(learnInMtx(i, :), perceptron, beta, func);
    calculatedValue = result(length(result(:, 1)), 1);
    calculatedTrainValuesY(i, 1) = calculatedValue;
    calculatedError = abs(learnOutMtx(i, 1) - calculatedValue);
    if (calculatedError > maximumTrainingError)
      maximumTrainingError = calculatedError;
    end
    if (calculatedError < minimunTrainingError)
      minimunTrainingError = calculatedError;
    end
    trainedTotalError += calculatedError;
  end
  %trainedAvgError = trainedTotalError / length(learnInMtx)
  %maximumTrainingError
  %minimunTrainingError

  for (i = 1:length(testInMtx))
    [layers, result] = perceptronEval(testInMtx(i, :), perceptron, beta, func);
    calculatedValue = result(length(result(:, 1)), 1);
    calculatedTestValuesY(i, 1) = calculatedValue;
    trueValuesTestX = testInMtx(:, 1);
    calculatedError = abs(testOutMtx(i, 1) - calculatedValue);
    if (calculatedError > maximumTestError)
      maximumTestError = calculatedError;
    end
    if (calculatedError < minimumTestError)
      minimumTestError = calculatedError;
    end
    generalizedTotalError += calculatedError;
  end
  %generalizedAvgError = generalizedTotalError / length(testInMtx)
  %maximumTestError
  %minimumTestError

  trueValuesX = testInMtx(:, 1);
  trueValuesY = testOutMtx(:, 1);


  
 % plot both functions, original and calculated one.
 %subplot(2,1,1);  
 printf("Function has been plotted \n");
 plot(trueValuesX, trueValuesY, '-', "markersize", 2, trueValuesTestX, calculatedTestValuesY, '-', "markersize",4);
 %subplot(2,1,2);
 %plot(diffe);
% refresh
 drawnow();
end
