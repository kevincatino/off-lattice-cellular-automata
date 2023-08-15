package ar.edu.itba.ss.cim.models;

public class Velocity {
    private final double vx;
    private final double vy;

    private Velocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }


    public static Velocity of(double vx, double vy) {
        return new Velocity(vx, vy);
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
