package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.dto.NoiseDataNWrapperDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class NoiseDataMainWrapper {
    private final Map<Integer, NoiseDataNWrapperDto> data = new TreeMap<>();

    public void addData(int n, double va, double noise) {
        data.putIfAbsent(n, new NoiseDataNWrapperDto(n));
        data.get(n).addData(va, noise);
    }

    public void writeFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path), data.values());
    }
}
