package ar.edu.itba.ss.cim.dto;

public class NoiseDataDto implements Comparable<NoiseDataDto> {
   public final TimeStatsDto va;
    public TimeStatsDto getVa() {
        return va;
    }

    public double getNoise() {
        return noise;
    }

    public final double noise;

    public NoiseDataDto(TimeStatsDto va, double noise) {
        this.va = va;
        this.noise = noise;
    }

    @Override
    public int compareTo(NoiseDataDto o) {
        return (int) (noise*100000 + va.getAvg()*100 - o.noise*100000 - o.va.getAvg()*100);
    }
}
