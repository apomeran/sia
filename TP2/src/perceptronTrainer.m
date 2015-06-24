function [trainedPerceptron lastDiff] = perceptronTrainer(inMtx, outMtx, p, beta, learningFactor, alpha, epsilon, funcIndex, iterationCount, ab, cons, arbitrary, testIn, testOut)
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
    kickthreshhold = 0.05;

    %beta adaptative definition
    global betas;
    betas = ones(numLayers+1, layerSize) .* beta;
    
    if (false)
     minbeta = 1;
     maxbeta = 2;
     factor1 = [minbeta:((maxbeta-minbeta)/(layerSize-1)):maxbeta];
     factor2 = 1;
     betas(2,:) = ones(1,layerSize) .* 1;
     betas(3,:) = ones(1,layerSize) .* 2;
    end    
    
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
            %if (diff > 100)
	     %learningFactor *= 1.5; 
            %end
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

      
      %give it a kick! (in case it is a local minima)
      if (learningFactor < kickthreshhold)
        if (k < 100000)
          learningFactor = 0.0001;
          kickthreshhold = 0.00001;
        end
        if (k < 30000)
          learningFactor = 0.001;
          kickthreshhold = 0.0001;
        end
        if (k < 20000)
          learningFactor = 0.01;
          kickthreshhold = 0.005;
        end
        if (k < 9000)
          learningFactor = 0.03;
          kickthreshhold = 0.005;
        end
        if (k < 6500)
          learningFactor = 0.025;
          kickthreshhold = 0.005;
        end
        if (k < 4000)
          learningFactor = 0.05;
          kickthreshhold = 0.01;
        end
        if (k < 1500)
          learningFactor = 0.1;
          kickthreshhold = 0.05;
	end
        if (k < 50)
          learningFactor = 0.2;
          kickthreshhold = 0.08;
	end

      end

      oldPerceptron = p;
      %show the ECM
      %comment to improve performance
      %counterPatternSkipped = 0;

      arraydiff(k+1) = diff;
      if (diff > epsilon)
        for (i = evaluationPatternOrder')
          out = outMtx(i, 1);
	     %if(true || abs(out-r(i)) > 0.01)
          	[p differentials(:, :, :, i)] = perceptronLearner(inMtx(i, :), outMtx(i, :), p, beta, learningFactor, func, derivatedFunc, 				alpha, differentials(:, :, :, i));
 	     %else
              %counterPatternSkipped++;
             %end
        end
      else
        break;
      end
  
     if(k != 0 && mod(k,15) == 0)  
      	printf("K= %d\t ECM %f\t Etha %f \n",k, diff,learningFactor);
        %printf("SKipped %d\n", counterPatternSkipped); 
      	stats(p, inMtx, outMtx, testIn, testOut, beta, func, arraydiff);
      	disp(diff)
      	fflush(stdout);
      end
 
      k++;
    end

    %k

    trainedPerceptron = p;
    lastDiff = diff;

end
