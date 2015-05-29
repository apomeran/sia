function out = deserializePerceptron(p, r, c, l)
    out = zeros(r, c, l);

    for i = 1:l
        for j = 1:r
            start = (j-1)*c + (i-1)*c*r + 1;
            ending = (j*c) + (i-1)*r*c;
            out(j, :, i) = p(start:ending);
        end
    end

end
