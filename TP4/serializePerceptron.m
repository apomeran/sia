function out = serializePerceptron(p)
    rows = length(p(:, 1, 1));
    cols = length(p(1, :, 1));
    layers = length(p(1, 1, :));

    out = [];

    for i = 1:layers
        for j = 1:rows
            out = [out p(j, :, i)];
        end
    end
end
