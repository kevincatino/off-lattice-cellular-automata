package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Board;

import java.util.Collection;
import java.util.stream.Collectors;

public class BoardStateDto {
    private final int time;
    private final Collection<MainParticleDto> particles;

    private BoardStateDto(int time, Collection<MainParticleDto> particles) {
        this.particles = particles;
        this.time = time;
    }

    public static BoardStateDto from(Board board) {
        return new BoardStateDto(board.getTime(), board.getAllParticles().stream().map(MainParticleDto::from).collect(Collectors.toList()));
    }

    public int getTime() {
        return time;
    }

    public Collection<MainParticleDto> getParticles() {
        return particles;
    }
}
