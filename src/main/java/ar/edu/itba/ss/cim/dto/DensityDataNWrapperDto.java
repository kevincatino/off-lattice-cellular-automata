package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.utils.MathHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.DoubleBinaryOperator;

public class DensityDataNWrapperDto {
    public Collection<DensityDataDto> getValues() {
        return values;
    }

    public int getN() {
        return n;
    }

    private final Collection<DensityDataDto> values = new TreeSet<>();
    private final Map<Double, Set<Double>> rawValues = new HashMap<>();
   public final int n;

    public DensityDataNWrapperDto(int n) {
        this.n = n;
    }

    public void addData(double va, double density) {
        rawValues.putIfAbsent(density,new HashSet<>());
        rawValues.get(density).add(va);
    }



    public void computeValues() {
        for (Map.Entry<Double, Set<Double>> entry : rawValues.entrySet()) {
            Set<Double> data = entry.getValue();
            double avg = data.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double min = data.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
            double max = data.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
            double std = MathHelper.calculateSD(data.stream().mapToDouble(Double::doubleValue).toArray());
            values.add(new DensityDataDto(new TimeStatsDto(max,min, avg, std), entry.getKey()));
        }

    }
}
