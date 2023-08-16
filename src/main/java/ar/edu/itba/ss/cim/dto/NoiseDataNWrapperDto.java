package ar.edu.itba.ss.cim.dto;

import java.util.Collection;
import java.util.HashSet;

public class NoiseDataNWrapperDto {
    public Collection<NoiseDataDto> getValues() {
        return values;
    }

    public int getN() {
        return n;
    }

    public final Collection<NoiseDataDto> values = new HashSet<>();
   public final int n;

    public NoiseDataNWrapperDto(int n) {
        this.n = n;
    }

    public void addData(double va, double noise) {
        values.add(new NoiseDataDto(va, noise));
    }
}
