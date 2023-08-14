package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Coordinates;

public class CoordinatesDto {
      private final double x;
    private final double y;

    private CoordinatesDto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static CoordinatesDto from(Coordinates coordinates) {
        return new CoordinatesDto(coordinates.getX(), coordinates.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
