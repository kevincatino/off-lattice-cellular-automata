package ar.edu.itba.ss.cim.models;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Particle {


    private static int ID = 1;

    private final int id;
    private Coordinates coordinates;

    private Set<Particle> neighbours;

    private final Properties properties;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", r=" + properties.getRadius() + " " +
                ", " + coordinates +
                '}';
    }

    Particle(double x, double y, Properties properties) {
        this(Coordinates.of(x, y), properties);
    }

    Particle(Coordinates coordinates, Properties properties) {
        this.id = ID;
        ID++;
        this.properties = properties;
        this.coordinates = coordinates;
        this.neighbours = new HashSet<>();
    }

    public int getId() {
        return this.id;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public double getRadius() {
        return properties.getRadius();
    }

    public double getX() {
        return this.coordinates.getX();
    }

    public static void resetIdCounter() {
        ID = 1;
    }

    public double getY() {
        return this.coordinates.getY();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public boolean isNeighbourOf(int particleId) {
        return neighbours.stream().anyMatch(neighbour -> neighbour.getId() == particleId);
    }

    public double getDistanceTo(Particle other) {
        return coordinates.getDistanceTo(other.coordinates);
    }



    public boolean isNeighbour(int particleId) {
        return neighbours.stream().anyMatch(neighbour -> neighbour.getId() == particleId);
    }

    public void addNeighbour(Particle particle) {
        this.neighbours.add(particle);
    }

    public void clearNeighbours() {
        neighbours = new HashSet<>();
    }

    public Set<Particle> getNeighbours() {
        return neighbours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Particle aux)) {
            return false;
        }
        return this.id == aux.id;
    }

}
