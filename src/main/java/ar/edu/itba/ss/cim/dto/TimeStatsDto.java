package ar.edu.itba.ss.cim.dto;

public class TimeStatsDto {
    private final double max;
    private final double min;
    private final double avg;

    public TimeStatsDto(double max, double min, double avg) {
        this.max = max;
        this.min = min;
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getAvg() {
        return avg;
    }
}
