package ar.edu.itba.ss.cim.models;

import java.util.Map;
import java.util.Set;

public class StaticStats {
    private final Map<Integer, Properties> propertiesByParticleId;

    public double getBoardLength() {
        return boardLength;
    }

    public int getParticlesQty() {
        return particlesQty;
    }

    private final double boardLength;
    private final int particlesQty;

    public StaticStats(int particlesQty, double boardLength, Map<Integer, Properties> propertiesByParticleId) {
        if (particlesQty != propertiesByParticleId.size()) {
            throw new IllegalArgumentException("particlesQty does not match number of properties");
        }

        this.boardLength = boardLength;
        this.particlesQty = particlesQty;
        this.propertiesByParticleId = propertiesByParticleId;
    }

    public Properties getProperty(int id) {
        return propertiesByParticleId.get(id);
    }

    public Set<Map.Entry<Integer, Properties>> getIdPropertyPairs() {
        return propertiesByParticleId.entrySet();
    }


}
