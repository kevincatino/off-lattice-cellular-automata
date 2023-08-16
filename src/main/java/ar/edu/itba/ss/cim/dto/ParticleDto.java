package ar.edu.itba.ss.cim.dto;


import ar.edu.itba.ss.cim.models.Particle;

public class ParticleDto {
    private final int id;
    private final CoordinatesDto coordinates;

    public VelocityDto getVelocity() {
        return velocity;
    }

    private final VelocityDto velocity;
    private final double radius;

    private ParticleDto(int id, CoordinatesDto coordinates, VelocityDto velocity, double radius) {
        this.id = id;
        this.coordinates = coordinates;
        this.velocity = velocity;
        this.radius = radius;
    }

    public static ParticleDto from(Particle particle) {
        return new ParticleDto(particle.getId(), CoordinatesDto.from(particle.getCoordinates()), VelocityDto.from(particle.getVelocity()),particle.getRadius());
    }

    public int getId() {
        return id;
    }

    public CoordinatesDto getCoordinates() {
        return coordinates;
    }

    public double getRadius() {
        return radius;
    }
}
