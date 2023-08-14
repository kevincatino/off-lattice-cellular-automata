package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.dto.ExecutionData;
import ar.edu.itba.ss.cim.dto.ExecutionStats;
import ar.edu.itba.ss.cim.dto.MExecutionData;
import ar.edu.itba.ss.cim.dto.MExecutionStats;
import ar.edu.itba.ss.cim.dto.TimeStatsDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExecutionStatsWrapper {
    private final Map<Integer, Set<ExecutionData>> stats = new TreeMap<>();
    private final Map<Integer, Set<MExecutionData>> mstats = new TreeMap<>();

    public void writeToFile(String path) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path), getStatistics());

    }

    public void writeMToFile(String path) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path), getMStatistics());

    }

    public void addStats(int numberOfParticles, long bruteForceTime, long cimTime, int M) {
        stats.putIfAbsent(numberOfParticles, new HashSet<>());
        stats.get(numberOfParticles).add(new ExecutionData(cimTime, bruteForceTime));
        mstats.putIfAbsent(M, new HashSet<>());
        mstats.get(M).add(new MExecutionData(cimTime));
    }

    private Collection<ExecutionStats> getStatistics() {
        List<ExecutionStats> execStats = new ArrayList<>();
        for (Map.Entry<Integer, Set<ExecutionData>> entry : stats.entrySet()) {
            Set<ExecutionData> data = entry.getValue();
            Collection<Double> cimTimes = data.stream().mapToDouble((s) -> s.cim).boxed().toList();
            Collection<Double> bruteForceTimes = data.stream().mapToDouble((s) -> s.bruteForce).boxed().toList();
            double cimAvg = cimTimes.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double cimMin = cimTimes.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
            double cimMax = cimTimes.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
            double bruteAvg = bruteForceTimes.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);;
            double bruteMin = bruteForceTimes.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
            double bruteMax = bruteForceTimes.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
            TimeStatsDto bruteForce = new TimeStatsDto(bruteMax, bruteMin, bruteAvg);
            TimeStatsDto cimTime = new TimeStatsDto(cimMax, cimMin, cimAvg);
            execStats.add(new ExecutionStats(entry.getKey(),bruteForce, cimTime));
        }
        return execStats;
    }
    private Collection<MExecutionStats> getMStatistics() {
        List<MExecutionStats> execStats = new ArrayList<>();
        for (Map.Entry<Integer, Set<MExecutionData>> entry : mstats.entrySet()) {
            Set<MExecutionData> data = entry.getValue();
            Collection<Double> cimTimes = data.stream().mapToDouble((s) -> s.cim).boxed().toList();
            double cimAvg = cimTimes.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double cimMin = cimTimes.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
            double cimMax = cimTimes.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
            TimeStatsDto cimTime = new TimeStatsDto(cimMax, cimMin, cimAvg);
            execStats.add(new MExecutionStats(entry.getKey(), cimTime));
        }
        return execStats;
    }
}
