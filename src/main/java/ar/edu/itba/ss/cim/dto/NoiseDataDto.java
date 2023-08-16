package ar.edu.itba.ss.cim.dto;

public class NoiseDataDto {
   public final double va;

    public double getVa() {
        return va;
    }

    public double getNoise() {
        return noise;
    }

    public final double noise;

    public NoiseDataDto(double va, double noise) {
        this.va = va;
        this.noise = noise;
    }
}
