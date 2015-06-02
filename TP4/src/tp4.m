function out = tp4(generation, sampleFunctionIndex, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc,    
  crossoverProbability,mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod, maxGenerations, targetFitness, 
  trainingSeasons, structurePercentage, unmutableGenerations)

  
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
  
    x6 = [-15 : 0.2 : 15];
    y6 = sin(x6) + 6*cos(x6).^2;

    %x7 = [10 : 0.2 : 45];
    %y7 = sin(x7) * (x7* x7 *x7)' + x7/2;

    x8 = [-10: 0.2 : 10];
    y8 = (sin(x8) .^ 5) .* x8;

    x9 = [-1 : 0.1 : 1];
    y9 = x9.^3 - 2 * x9.^2 + sin(5*x9); 
  
    x10 = [-2 : 0.1 : 2];
    y10 = sinh(x10) .* cos(x10.^2);

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
    sampleFunc{6,1} = x6;
    sampleFunc{6,2} = y6;
    %sampleFunc{7,1} = x7;
    %sampleFunc{7,2} = y7;
    %sampleFunc{8,1} = x8;
    %sampleFunc{8,2} = y8;
    sampleFunc{9,1} = x9;
    sampleFunc{9,2} = y9;
    sampleFunc{10,1} = x10; 
    sampleFunc{10,2} = y10; 
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

  [out maxFitnessVector meanFitnessVector] = genetics(generation, learnInMtx, learnOutMtx, fitnessFunc, selectionFunc, selectionNumber,   
  mixSelectionNumber, crossoverFunc, crossoverProbability,mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod,   
  maxGenerations, targetFitness, trainingSeasons, structurePercentage, unmutableGenerations);

end
