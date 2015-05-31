function out = tp4(N,sampleFunctionIndex, layerSize,fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc,    
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
   
  x1 = [0 : 0.1 : 4];
  y1 = sin(10*x1) .* e.^(-1*x1);

  x2 = [-6.3 : 0.1 : 6.3];
  y2 = 13 * sin(x2) .* cos(x2).^2;

  x3 = [-10:0.1:10]; 
  y3 = 5 * sin(x3).^2 + cos(x3);
 	
  x4 = [-2*pi : 0.1 : 2*pi];
  y4 = 1./(cos(x4) + 2);

  x5 = [-4 : 0.2 : 4];
  y5 = tanh(0.1*x5) + sin(3*x5);

  x9 = [-1 : 0.1 : 1];
  y9 = x9.^3 - 2 * x9.^2 + sin(5*x9); 

  x11 = [-1:0.01:1];
  y11 = sin(x11 + 2 * x11.^ 2 + 3 * x11.^3);

  sampleFunc{1,1} = x1;
  sampleFunc{1,2} = y1;
  sampleFunc{2,1} = x2;
  sampleFunc{2,2} = y2;
  sampleFunc{3,1} = x3;
  sampleFunc{3,2} = y3;
  sampleFunc{4,1} = x4;
  sampleFunc{4,2} = y4;
  sampleFunc{5,1} = x5;
  sampleFunc{5,2} = y5;
  sampleFunc{5,1} = x5;
  sampleFunc{5,2} = y5;
  sampleFunc{9,1} = x9; 
  sampleFunc{9,2} = y9; 
  sampleFunc{11,1} = x11; 
  sampleFunc{11,2} = y11; 
  
  x = sampleFunc{sampleFunctionIndex,1};
  y = sampleFunc{sampleFunctionIndex,2}; 

  x = ((x - mean(x)) / std(x));
  %y = ((y - mean(y)) / std(y));
  learnInMtx = x';
  learnOutMtx = y';
  testInMtx = learnInMtx;
  testOutMtx = learnOutMtx;

  [out maxFitnessVector meanFitnessVector] = genetics(N, layerSize, learnInMtx, learnOutMtx, fitnessFunc, selectionFunc, selectionNumber,   
  mixSelectionNumber, crossoverFunc, crossoverProbability,mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod,   
  maxGenerations, targetFitness, trainingSeasons, structurePercentage, unmutableGenerations)

  toc
end
