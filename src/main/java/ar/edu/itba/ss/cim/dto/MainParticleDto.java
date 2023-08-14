package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Particle;

import java.util.Collection;
import java.util.stream.Collectors;

public class MainParticleDto {
      private final int id;
    private final CoordinatesDto coordinates;
    private final double radius;
    private final Collection<ParticleDto> neigbours;

    public int getId() {
        return id;
    }


    public CoordinatesDto getCoordinates() {
        return coordinates;
    }

    public double getRadius() {
        return radius;
    }

    public Collection<ParticleDto> getNeigbours() {
        return neigbours;
    }


    private MainParticleDto(int id, CoordinatesDto coordinates, double radius, Collection<ParticleDto> neigbours) {
        this.id = id;
        this.coordinates = coordinates;
        this.radius = radius;
        this.neigbours = neigbours;
    }

    public static MainParticleDto from(Particle particle) {
        return new MainParticleDto(particle.getId(),CoordinatesDto.from(particle.getCoordinates()),particle.getRadius(),particle.getNeighbours().stream().map(ParticleDto::from).collect(Collectors.toList()));
    }
}
