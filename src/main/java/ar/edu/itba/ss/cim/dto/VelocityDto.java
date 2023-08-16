package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Coordinates;
import ar.edu.itba.ss.cim.models.Velocity;

public class VelocityDto {
      private final double vx;
    private final double vy;

    private VelocityDto(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public static VelocityDto from(Velocity v) {
        return new VelocityDto(v.getVx(), v.getVy());
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }
}
