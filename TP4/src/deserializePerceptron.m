function out = deserializePerceptron(p, row, col, layer)
    out = zeros(row, col, layer);

    for i = 1:layer
        for j = 1:row
            start = (j-1)*col + (i-1)*col*row + 1;
            ending = (j*col) + (i-1)*row*col;
            out(j, :, i) = p(start:ending);
        end
    end

end
