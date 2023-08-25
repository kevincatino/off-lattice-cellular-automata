package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Board;
import ar.edu.itba.ss.cim.models.BoardSequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VaDto {
    private final double L;

    public int getN() {
        return N;
    }

    private final int N;

    public double getNoise() {
        return noise;
    }

    public double getDensity() {
        return density;
    }

    private final double noise;

    private final double density;

    private final List<VaStateDto> data;

    private VaDto(double L, int N, double noise, List<VaStateDto> data) {
        this.L = L;
        this.N = N;
        this.noise = noise;
        this.density = N/(L*L);
        this.data = data;
    }

    public static VaDto from(BoardSequence bs) {
        List<VaStateDto> data = new ArrayList<>();

        for (Board b : bs) {

            data.add(VaStateDto.from(b));
        }

        return new VaDto(bs.getBoardLength(), bs.getParticleNumber(), bs.getNoise(), data);

    }

    public double getL() {
        return L;
    }






    public List<VaStateDto> getData() {
        return data;
    }

}
