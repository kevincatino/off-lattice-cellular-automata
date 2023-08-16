package ar.edu.itba.ss.cim.dto;

import java.util.Collection;
import java.util.HashSet;

public class DensityDataNWrapperDto {
    public Collection<DensityDataDto> getValues() {
        return values;
    }

    public int getN() {
        return n;
    }

    public final Collection<DensityDataDto> values = new HashSet<>();
   public final int n;

    public DensityDataNWrapperDto(int n) {
        this.n = n;
    }

    public void addData(double va, double noise) {
        values.add(new DensityDataDto(va, noise));
    }
}
