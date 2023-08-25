package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.utils.MathHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class NoiseDataNWrapperDto {
    public Collection<NoiseDataDto> getValues() {
        return values;
    }
    private final Map<Double, Set<Double>> rawValues = new HashMap<>();

    public int getN() {
        return n;
    }

    public final Collection<NoiseDataDto> values = new TreeSet<>();
   public final int n;

    public NoiseDataNWrapperDto(int n) {
        this.n = n;
    }

    public void computeValues() {
        for (Map.Entry<Double, Set<Double>> entry : rawValues.entrySet()) {
            Set<Double> data = entry.getValue();
            double avg = data.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double min = data.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
            double max = data.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);

            double std = MathHelper.calculateSD(data.stream().mapToDouble(Double::doubleValue).toArray());
            values.add(new NoiseDataDto(new TimeStatsDto(max,min, avg, std), entry.getKey()));
        }
    }

    public void addData(List<Double> vas, double noise) {
        rawValues.putIfAbsent(noise,new HashSet<>());
        rawValues.get(noise).addAll(vas);
    }
}
