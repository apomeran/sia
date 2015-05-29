function out = tp2(file, layerSize, beta, learningFactor, alpha, epsilon, funcIndex, iterationCount, abValues, consistency, amount, arbitrary, minn, maxx)
  
  tic
  %Index for functions
  func{1, 1} = @sigmoid;
  func{1, 2} = @sigmoidDerivated;

  func{2, 1} = @exponential;
  func{2, 2} = @exponentialDerivated;

  %Select one function pfor the index
  func = func{funcIndex, 1};



  x1 = [0 : 0.1 : 1];
  y1 = sin(10*x1) .* e.^(-1*x1);
  x2 = [-6.3 : 0.1 : 6.3];
  y2 = 12 * sin(x2) * cos(x2).^2;
  x5 = [-4 : 0.15 : 4];
  y5 = tanh(0.1*x5) + sin(3*x5);
  x11 = [-1:0.01:1];
  y11 = sin(x11 + 2 * x11.^ 2 + 3 * x11.^3);
  %meanX = mean(x);
  %meanY = mean(y);
  %devStdX = std(x);
  %devStdY = std(y);
  
  x = x5 + 0;
  y = y5;
  %y = y ./ max(y);
  %x = x ./ max(x);   
  learnInMtx = x';
  learnOutMtx = y';
  testInMtx = learnInMtx;
  testOutMtx = learnOutMtx;


  %create a perceptron!
  perceptron = perceptronFactory(layerSize);
  
  %train & plot on every iteration
  trainedPerceptronValue = perceptronTrainer(learnInMtx, learnOutMtx, perceptron, beta, learningFactor, alpha, epsilon, funcIndex, iterationCount, abValues, consistency,arbitrary,testInMtx, testOutMtx);

  %final statistics 
  stats(trainedPerceptronValue, learnInMtx, learnOutMtx, testInMtx, testOutMtx, beta, func);
  toc
end
