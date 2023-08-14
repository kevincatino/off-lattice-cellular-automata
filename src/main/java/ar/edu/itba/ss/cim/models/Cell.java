package ar.edu.itba.ss.cim.models;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Cell {

    private final Set<Particle> cellParticles = new HashSet<>();

    public Set<Particle> getParticles() {
        return this.cellParticles;
    }

    public void clearCell() {
        cellParticles.clear();
    }

    public void addParticle(Particle particle) {
        this.cellParticles.add(particle);
    }

    @Override
    public String toString() {
        String particlesString = cellParticles.stream().map(p -> String.format("%d", p.getId())).collect(Collectors.joining(","));
        return String.format("[%40s]", particlesString);

    }

}
