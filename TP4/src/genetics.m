function [out maxFitnessVector meanFitnessVector] = genetics(generation, inMtx, outMtx, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc, crossoverProbability, mutationFunc, mutationProbability, replacementSelectionFunc, replacementMethod, maxGenerations, targetFitness, trainingSeasons, structurePercentage, unmutableGenerations)

    % Create the first generation
    %generation = newGeneration(N, layerSizes);
    %if (trainingSeasons > 0)
    %train to not start with random values
    %trainGeneration(generation, inMtx, outMtx, 500);
    %end

    if (maxGenerations == 0)
        maxGenerations = Inf;
    end
    maxFitnessVector = [];
    meanFitnessVector = [];
    N = length(generation(1,1,1,:));
    genNumber = 0;
    temperature = 1e+5;
    previousFitness = zeros(1, N);
    unmuted = 0;
    maxFitness = 0;
    while (genNumber < maxGenerations)
        %calculate Total Fitness and Relative Fitness for all population
        [totalFitness relativeFitness] = calculatePopulationFitness(fitnessFunc, generation, inMtx, outMtx);
          
	%Get the maximum fitness from all population and its position
        [maxFitnessGeneration, position] = max(totalFitness);

	%check how many individual fitnesses hasnt changed along one iteration
        unmutedFitnesses = 0;
        for i = 1:N
          if (previousFitness(i) == totalFitness(i))
            unmutedFitnesses++;
          end
        end

        if (maxFitnessGeneration == maxFitness)
          unmuted++;
        else
          unmuted = 0;
        end
        maxFitness = maxFitnessGeneration;
	
        %Calculate how Max-Fitness change over iterations
        maxFitnessVector = [maxFitnessVector max(totalFitness)];
        %Calculate how Mean-Fitness change over iterations 
        meanFitnessVector = [meanFitnessVector mean(totalFitness)];

	%Calculate percentage of unmuted fitness individuals
        unmutedPercentage = unmutedFitnesses * 100 / N;

        % If target fitness is achieved within this generation, return the individual.
        if (maxFitnessGeneration > targetFitness || (unmutableGenerations != 0 && unmuted > unmutableGenerations)
          || (structurePercentage > 0 && structurePercentage < unmutedPercentage))
            out = generation(:, :, :, position);
            break;
        end
	printf("Min= %f\t Mean= %f\t Max %f\t \n", min(totalFitness), mean(totalFitness), maxFitnessGeneration);
        stats(generation(:, :, :, position), inMtx, outMtx, inMtx, outMtx, 1, @sigmoid, 0);
        fflush(stdout);

        % Replace the generation for the next one
        generation = replacementMethod(generation, inMtx, outMtx, totalFitness, relativeFitness, fitnessFunc, selectionFunc, selectionNumber, mixSelectionNumber, crossoverFunc, crossoverProbability, mutationFunc, mutationProbability, replacementSelectionFunc, trainingSeasons, temperature);

        if (temperature > 20000)
          temperature = temperature * 0.95;
        end
        genNumber++;
    end

    [totalFitness relativeFitness] = calculatePopulationFitness(fitnessFunc, generation, inMtx, outMtx);
    [maxFitnessGeneration, position] = max(totalFitness);
    plot(1:length(maxFitnessVector), maxFitnessVector, 1:length(meanFitnessVector), meanFitnessVector);
    % axis -> [x_low x_high y_low y_high]
    axis([1 maxGenerations 0 200000]);
    xlabel("Generation");
    ylabel("Fitness");
    title("Fitness across generations");
    legend("     Max fitness ", "      Mean fitness ");
    legend('boxon');
    out = generation(:, :, :, position);
end
