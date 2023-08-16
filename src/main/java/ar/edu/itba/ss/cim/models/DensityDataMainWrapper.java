package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.dto.DensityDataNWrapperDto;
import ar.edu.itba.ss.cim.dto.NoiseDataNWrapperDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class DensityDataMainWrapper {
    private final Map<Integer, DensityDataNWrapperDto> data = new TreeMap<>();

    public void addData(int n, double va, double density) {
        data.putIfAbsent(n, new DensityDataNWrapperDto(n));
        data.get(n).addData(va, density);
    }

    public void writeFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path), data.values());
    }
}
