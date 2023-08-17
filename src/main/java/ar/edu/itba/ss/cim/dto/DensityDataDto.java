package ar.edu.itba.ss.cim.dto;

public class DensityDataDto implements Comparable<DensityDataDto> {
   public final TimeStatsDto va;

    public TimeStatsDto getVa() {
        return va;
    }

    public double getDensity() {
        return density;
    }

    public final double density;

    public DensityDataDto(TimeStatsDto va, double density) {
        this.va = va;
        this.density = density;
    }

    @Override
    public int compareTo(DensityDataDto o) {
        return (int) (density*100000 + va.getAvg()*100 - o.density*100000 - o.va.getAvg()*100);
    }
}
