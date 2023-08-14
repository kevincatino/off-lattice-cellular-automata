package ar.edu.itba.ss.cim.dto;

public class ExecutionStats {
    public int getParticles() {
        return particles;
    }



    private final int particles;

    public TimeStatsDto getBruteForce() {
        return bruteForce;
    }

    public TimeStatsDto getCim() {
        return cim;
    }

    private final TimeStatsDto bruteForce;
    private final TimeStatsDto cim;

    public ExecutionStats(int particles, TimeStatsDto bruteForce, TimeStatsDto cim) {
        this.particles = particles;
        this.bruteForce = bruteForce;
        this.cim = cim;
    }
}
