package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.dto.DensityDataNWrapperDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DensityDataMainWrapper {
    private final Map<Integer, DensityDataNWrapperDto> data = new TreeMap<>();

    public void addData(int n, List<Double> vas, double density) {
        data.putIfAbsent(n, new DensityDataNWrapperDto(n));
        data.get(n).addData(vas, density);
    }

    public void writeFile(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Collection<DensityDataNWrapperDto> values = data.values();
        values.forEach(DensityDataNWrapperDto::computeValues);
        mapper.writeValue(new File(path), values);
    }
}
