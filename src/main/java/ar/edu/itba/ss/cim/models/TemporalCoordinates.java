package ar.edu.itba.ss.cim.models;

import java.util.HashMap;
import java.util.Map;

public class TemporalCoordinates {
    private final Map<Integer, Coordinates> coordinatesById = new HashMap<>();
    private final Map<Integer, Velocity> velocityById = new HashMap<>();
    private final int time;

    public TemporalCoordinates(int time) {
        this.time = time;
    }

    public void addCoordinates(int particleId, Coordinates coordinates) {
        coordinatesById.put(particleId, coordinates);
    }

    public void addVelocity(int particleId, Velocity velocity) {
        velocityById.put(particleId, velocity);
    }

    public int getCoordinatesCount() {
        return coordinatesById.size();
    }

    public Coordinates getCoordinates(int particleId) {
        return coordinatesById.get(particleId);
    }

    public Velocity getVelocity(int particleId) {
        return velocityById.get(particleId);
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder(String.format("time: %d\n", time));
        for (Map.Entry<Integer, Coordinates> entry : coordinatesById.entrySet()) {
            toReturn.append(String.format("{id: %d, coordinates: %s}\n", entry.getKey(), entry.getValue()));
        }
        return toReturn.toString();
    }
}
