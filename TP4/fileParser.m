function out = fileParser(file)
    patterns = [];
    fid = fopen(file,'r');


    if (fid < 0) 
        printf('Error:could not open file\n')
    else
        while ~feof(fid),
            line = fgetl(fid);
            patterns = [patterns; str2num(line)]; 
        end;
        fclose(fid);
    end; 
    out = patterns;

endfunction
