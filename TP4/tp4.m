function out = tp4(N,file, layerSize,fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc,    
  crossoverProbability,mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod, maxGenerations, targetFitness, 
  trainingSeasons, structurePercentage, unmutableGenerations)

  tic

  % 'Best' values for TP2

  beta = 1;
  learningFactor = 0.01;
  alpha = 0.3;
  epsilon = 0.0001;
  funcIndex = 1;
  iterationCount = 50;
  abValues = [0.01 0.1];
  consistency = 5;
  amount = 40;
  arbitrary = 0.01;
  minn = 1;
  maxx = amount;

  %Index for functions
  func{1, 1} = @sigmoid;
  func{1, 2} = @sigmoidDerivated;

  func{2, 1} = @exponential;
  func{2, 2} = @exponentialDerivated;

  %Select one function pfor the index
  func = func{funcIndex, 1};


  x1 = [0 : 0.1 : 1];
  y1 = sin(10*x1) .* e.^(-1*x1);
  x5 = [-4 : 0.15 : 4];
  y5 = tanh(0.1*x5) + sin(3*x5);
  x11 = [-1:0.01:1];
  y11 = sin(x11 + 2 * x11.^ 2 + 3 * x11.^3);
  %meanX = mean(x);
  %meanY = mean(y);
  %devStdX = std(x);
  %devStdY = std(y);
  
  x = x11;
  y = y11;
  %y = y ./ max(y);
  %x = x ./ max(x);   
  learnInMtx = x';
  learnOutMtx = y';
  testInMtx = learnInMtx;
  testOutMtx = learnOutMtx;

  [out maxFitnessVector meanFitnessVector] = genetics(N, layerSize, learnInMtx, learnOutMtx, fitnessFunc, selectionFunc, selectionNumber,   
  mixSelectionNumber, crossoverFunc, crossoverProbability,mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod,   
  maxGenerations, targetFitness, trainingSeasons, structurePercentage, unmutableGenerations)

  toc
end
