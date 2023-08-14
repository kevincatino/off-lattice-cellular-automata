package ar.edu.itba.ss.cim.models;

public class Properties {
    private final double radius;
    private final String metadata;

    public Properties(double radius, String metadata) {
        this.radius = radius;
        this.metadata = metadata;
    }

    public double getRadius() {
        return radius;
    }

    public String getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "radius=" + radius +
                ", metadata='" + metadata + '\'' +
                '}';
    }
}
