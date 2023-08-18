package ar.edu.itba.ss.cim.dto;

public class TimeStatsDto {
    private final double max;
    private final double min;
    private final double avg;

    private final double std;

    public TimeStatsDto(double max, double min, double avg, double std) {
        this.max = max;
        this.min = min;
        this.std = std;
        this.avg = avg;
    }

    public double getStd() {
        return std;
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
