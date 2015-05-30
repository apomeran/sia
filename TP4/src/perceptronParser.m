function perceptron = perceptronParser(filename)
    perceptronAux = {};
    layerAux = {};
    layerQty = 1;
    height = 1;
    fid = fopen(filename,'r');

    if (fid < 0)
        printf('Error:could not open file\n')
    else
        while ~feof(fid)
            line = fgetl(fid);
            if (line == ";")
              perceptronAux{layerQty} = layerAux;
              layerQty++;
              height = 1;
              layerAux = {};
            else
              layerAux{height} = str2num(line);
              height++;
            end
        end
        perceptronAux{layerQty} = layerAux;
        fclose(fid);
    end

    perceptron = zeros(length(layerAux), length(layerAux{1}), layerQty);

    for i = 1:layerQty
      for j = 1:length(perceptronAux{i})
        perceptron(j, :, i) = perceptronAux{i}{j};
      end
    end
endfunction
