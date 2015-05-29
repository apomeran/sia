function trainedPerceptron = perceptronTrainer(inMtx, outMtx, p, beta, learningFactor, alpha, epsilon, funcIndex, iterationCount, ab, cons, arbitrary, testIn, testOut)
    format long;
    activationFunctions{1, 1} = @sigmoid;
    activationFunctions{1, 2} = @sigmoidDerivated;

    activationFunctions{2, 1} = @exponential;
    activationFunctions{2, 2} = @exponentialDerivated;

    func = activationFunctions{funcIndex, 1};
    derivatedFunc = activationFunctions{funcIndex, 2};

    layerSize = length(p(1, :, 1));
    numLayers = length(p(1, 1, :));
    differentials = zeros(length(p(:, 1, 1)), layerSize, numLayers, length(inMtx));
    if (iterationCount == 0)
      iterationCount = Inf; %up to ECM
    end
    k = 0;
    lastDiff = Inf;
    arraydiff = [];
    storedAlpha = alpha;
    consistentStepsCount = 0;
    
    while (k < iterationCount)
      r = [];
      diff = 0;

      %We sort the patterns in every iteration
      [randoms evaluationPatternOrder] = sort(rand(length(inMtx), 1));
      
      numberOfLayers = length(p(1, 1, :));
        
     %evaluationPatternOrder = [length(inMtx):-1:1]';
      for (i = 1:length(inMtx))
        [layers, result] = perceptronEval(inMtx(i, :), p, beta, func);
	r(i) = result(numberOfLayers+1, 1);
        diff += abs(abs(result(length(result(:, 1)), 1) - outMtx(i)).^2/length(inMtx));
      end
      % Adaptative ETHA

      if (ab != 0)
        if (diff < lastDiff)
          consistentStepsCount++;
          if (consistentStepsCount >= cons)
            learningFactor = learningFactor + ab(1);
            %we are doing ok, so lets put alpha momentum again
            alpha = storedAlpha;
            lastDiff = diff;
            %reset consistency counter
            consistentStepsCount = 0;
          end
        else
	  if(arbitrary != 0) %Arbitrary value for learningRate
           learningFactor = arbitrary;
          else
	   learningFactor = learningFactor - learningFactor * ab(2);
          end
	  %we set alpha to zero, to prevent goin in the wrong way	
	  alpha = 0;
          p = oldPerceptron;
          consistentStepsCount = 0;
        end
      end

      oldPerceptron = p;
      %show the ECM
      %comment to improve performance
      printf("K= %d\t ECM %f\t Etha %f \n",k, diff,learningFactor);
      
      arraydiff(k+1) = diff;
      stats(p, inMtx, outMtx, testIn, testOut, beta, func, arraydiff);
      disp(diff)
      fflush(stdout);
      %disp(p)
      counterPatternSkipped = 0;
      if (diff > epsilon)
        for (i = evaluationPatternOrder')
          out = outMtx(i, 1);
	  if(abs(out-r(i)) > 0.000)
          	[p differentials(:, :, :, i)] = perceptronLearner(inMtx(i, :), outMtx(i, :), p, beta, learningFactor, func, derivatedFunc, 				alpha, differentials(:, :, :, i));
	  
          else
             counterPatternSkipped++;
          end
        end
      else
        break;
      end
      printf("SKipped %d\n", counterPatternSkipped); 
      k++;
    end

    k

    trainedPerceptron = p;

end
