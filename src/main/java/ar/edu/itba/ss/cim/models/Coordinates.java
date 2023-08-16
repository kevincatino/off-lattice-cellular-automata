package ar.edu.itba.ss.cim.models;

public class Coordinates {
    private final double x;
    private final double y;

    private Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public static Coordinates from(Coordinates c) {
        return Coordinates.of(c.getX(), c.getY());
    }

    private double wrapAxis(double value, double boardLength) {
        if (value > boardLength) {
            value -=boardLength;
        } else if (value < 0) {
            value += boardLength;
        }

        return value;
    }
    public Coordinates getNext(Velocity v, double boardLength) {
        double nextX = this.x + v.getVx();
        double nextY = this.y + v.getVy();
        nextX = wrapAxis(nextX, boardLength);
        nextY = wrapAxis(nextY, boardLength);
        return Coordinates.of(nextX,nextY);
    }

    public double getDistanceTo(Coordinates c) {
        return Math.sqrt(Math.pow(x - c.x, 2) + Math.pow(y - c.y, 2));
    }

    public static Coordinates of(double x, double y) {
        return new Coordinates(x, y);
    }

    public String toString() {
        return String.format("{%3.2f,%3.2f}", x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
