function out = statisticGenerator(perceptron, learnInMtx, learnOutMtx, testInMtx, testOutMtx, beta, func, errorRates)

  trainedTotalError = 0;
  generalizedTotalError = 0;

  trainedRateSuccess = zeros(1, length(errorRates));
  generalizedRateSuccess = zeros(1, length(errorRates));

  minTrainError = Inf;
  maxTrainError = 0;

  minTestError = Inf;
  maxTestError = 0;

  calculatedTrainValuesZ = zeros(length(learnInMtx), 1);
  calculatedTestValuesZ = zeros(length(testInMtx), 1);

  for (i = 1:length(learnInMtx))
    [layers, result] = perceptronEvaluator(learnInMtx(i, :), perceptron, beta, func);
    calculatedValue = result(length(result(:, 1)), 1);
    calculatedTrainValuesZ(i, 1) = calculatedValue;
    calculatedError = abs(learnOutMtx(i, 1) - calculatedValue);
    if (calculatedError > maxTrainError)
      maxTrainError = calculatedError;
    end
    if (calculatedError < minTrainError)
      minTrainError = calculatedError;
    end
    trainedTotalError += calculatedError;
    for (j = 1:length(errorRates))
      if (calculatedError < errorRates(j))
        trainedRateSuccess(1, j)++;
      end
    end
  end
  trainedAvgError = trainedTotalError / length(learnInMtx)
  maxTrainError
  minTrainError
  errorRates
  errorRateTrainedSuccess = trainedRateSuccess / length(learnInMtx)

  for (i = 1:length(testInMtx))
    [layers, result] = perceptronEvaluator(testInMtx(i, :), perceptron, beta, func);
    calculatedValue = result(length(result(:, 1)), 1);
    calculatedTestValuesZ(i, 1) = calculatedValue;
    calculatedError = abs(testOutMtx(i, 1) - calculatedValue);
    if (calculatedError > maxTestError)
      maxTestError = calculatedError;
    end
    if (calculatedError < minTestError)
      minTestError = calculatedError;
    end
    generalizedTotalError += calculatedError;
    for (j = 1:length(errorRates))
      if (calculatedError < errorRates(j))
        generalizedRateSuccess(1, j)++;
      end
    end
  end
  generalizedAvgError = generalizedTotalError / length(testInMtx)
  maxTestError
  minTestError
  errorRates
  errorRateGeneralizedSuccess = generalizedRateSuccess / length(testInMtx)

  % plot both functions, original and calculated one.
  trueValuesX = [learnInMtx(:, 1)' testInMtx(:, 1)']';
  trueValuesY = [learnInMtx(:, 2)' testInMtx(:, 2)']';
  trueValuesZ = [learnOutMtx(:, 1)' testOutMtx(:, 1)']';

  trueValuesTrainX = learnInMtx(:, 1);
  trueValuesTrainY = learnInMtx(:, 2);

  trueValuesTestX = testInMtx(:, 1);
  trueValuesTestY = testInMtx(:, 2);
  
  %plot3(trueValuesX, trueValuesY, trueValuesZ, "o", trueValuesTrainX, trueValuesTrainY, calculatedTrainValuesZ, "x", trueValuesTestX, trueValuesTestY, calculatedTestValuesZ, "x");
  plot3(trueValuesTrainX, trueValuesTrainY, calculatedTrainValuesZ, "x", trueValuesTestX, trueValuesTestY, calculatedTestValuesZ, "x");
end
