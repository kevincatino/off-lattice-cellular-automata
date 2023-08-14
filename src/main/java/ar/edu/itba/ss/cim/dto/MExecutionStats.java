package ar.edu.itba.ss.cim.dto;

public class MExecutionStats {
    public int getM() {
        return M;
    }



    private final int M;

    public TimeStatsDto getTime() {
        return time;
    }


    private final TimeStatsDto time;

    public MExecutionStats(int M, TimeStatsDto time) {
        this.M = M;
        this.time = time;
    }
}
