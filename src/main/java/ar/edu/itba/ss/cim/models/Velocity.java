package ar.edu.itba.ss.cim.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Velocity {
    private final double vx;
    private final double vy;

    private final double mod;

    private Velocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
        this.mod = Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2));
    }


    public static Velocity of(double vx, double vy) {
        return new Velocity(vx, vy);
    }

    public double getMod() {
        return mod;
    }

    public Velocity getNext(Collection<Particle> neighbours, double noise) {

        Random rand = new Random();
        double noiseValue = noise == 0 ? 0 :  rand.nextDouble(noise) - noise/2;
        Collection<Double> angles = new ArrayList<>(neighbours.stream().map(p -> Math.atan2(vy, vx)).toList());
        angles.add(Math.atan2(this.vy,this.vx));
        double sinAvg = angles.stream().mapToDouble(Math::sin).average().getAsDouble();
        double cosAvg = angles.stream().mapToDouble(Math::cos).average().getAsDouble();
        double nextAngle = Math.atan2(sinAvg,cosAvg) + noiseValue;
        double nextVx = Math.cos(nextAngle)*mod;
        double nextVy = Math.sin(nextAngle)*mod;
        return Velocity.of(nextVx, nextVy);
    }

    public String toString() {
        return String.format("Vel{%3.2f,%3.2f}", vx, vy);
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }
}
