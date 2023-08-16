package ar.edu.itba.ss.cim.dto;

public class DensityDataDto {
   public final double va;

    public double getVa() {
        return va;
    }

    public double getDensity() {
        return density;
    }

    public final double density;

    public DensityDataDto(double va, double density) {
        this.va = va;
        this.density = density;
    }
}
