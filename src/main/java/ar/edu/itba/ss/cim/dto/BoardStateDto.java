package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Board;

import java.util.Collection;
import java.util.stream.Collectors;

public class BoardStateDto {
    private final int time;
    private final Collection<ParticleDto> particles;

    public double getVa() {
        return va;
    }

    private final double va;

    private BoardStateDto(int time, Collection<ParticleDto> particles, double va) {
        this.particles = particles;
        this.time = time;
        this.va = va;
    }

    public static BoardStateDto from(Board board) {
        return new BoardStateDto(board.getTime(), board.getAllParticles().stream().map(ParticleDto::from).collect(Collectors.toList()), board.getCurrentVa());
    }

    public int getTime() {
        return time;
    }

    public Collection<ParticleDto> getParticles() {
        return particles;
    }
}
