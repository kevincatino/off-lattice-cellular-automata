package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.dto.BoardSequenceDto;
import ar.edu.itba.ss.cim.dto.VaDto;
import ar.edu.itba.ss.cim.utils.MathHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BoardSequence implements Iterable<Board> {
    private final TemporalCoordinates initialCoordinates;

    private final Set<Particle> particles = new HashSet<>();

    private int index;
    private final int periods;

    private final Board board;

    private final double noise;



    public List<Double> getVa() {
        final int TIMES = 4000;
        final int TIMES_AVG = 2000;
        int idx = 0;
        Iterator<Board> it = iterator();
        Board b = it.next();
        List<Double> values = new ArrayList<>();
        boolean reachedTime = false;
        while (idx < TIMES + TIMES_AVG) {
            if (idx == TIMES) {
                reachedTime = true;
            }
            if (reachedTime) {
                values.add(b.getCurrentVa());
            }
            idx++;
            b = it.next();
        }
        return values;
    }

    public BoardSequence(StaticStats staticStats, TemporalCoordinates initialCoordinates, double noise, double interactionRadius, Board.BoundaryConditions boundaryConditions, int periods) {
        this.initialCoordinates = initialCoordinates;
        this.periods = periods;
        this.noise = noise;
        Coordinates coordinates = Coordinates.of(0, 0);
        Velocity velocity = Velocity.of(0,0);
        for (Map.Entry<Integer, Properties> idProp : staticStats.getIdPropertyPairs()) {
            Properties properties = idProp.getValue();
            Particle particle = new Particle(coordinates,velocity, properties);
            particles.add(particle);
        }
        int M = Board.computeOptimalM(staticStats.getBoardLength(),interactionRadius,particles);
        if (staticStats.getParticlesQty() != initialCoordinates.getCoordinatesCount()) {
            throw new RuntimeException("Number of particles in static stats and coordinates of temporal coordinates should match");
        }
        board = new Board(M, staticStats.getBoardLength(), interactionRadius, boundaryConditions);


        board.addParticles(particles);
    }

    public int getParticleNumber() {
        return particles.size();
    }

    public double getNoise() {
        return noise;
    }


    public double getBoardLength() {
        return board.getBoardLength();
    }

    public double getInteractionRadius() {
        return board.getInteractionRadius();
    }

    public int getM() {
        return board.getM();
    }

    private Board resetBoard() {
        board.resetTime();
        for (Particle particle : particles) {
            int id = particle.getId();
            Coordinates coordinates = initialCoordinates.getCoordinates(id);
            particle.setCoordinates(coordinates);
            particle.setVelocity(initialCoordinates.getVelocity(id));
        }
        board.recomputeParticlesCell();
        board.getNeighbours(Board.Method.CIM);
        Particle.resetIdCounter();
        return board;
    }



    private Board getNextBoard() {
        board.increaseTime();
        double boardLength = getBoardLength();
        Map<Particle,Velocity> nextVelocities = new HashMap<>();
        Map<Particle,Coordinates> nextCoordinates = new HashMap<>();
        for (Particle particle : particles) {
            Collection<Particle> neighbours = particle.getNeighbours();
            Coordinates nextCoordinate = particle.getCoordinates().getNext(particle.getVelocity(), boardLength);
            Velocity nextVelocity = particle.getVelocity().getNext(neighbours, noise);
            nextVelocities.put(particle, nextVelocity);
            nextCoordinates.put(particle, nextCoordinate);
        }
        for (Map.Entry<Particle,Velocity> entry : nextVelocities.entrySet()) {
            Particle particle = entry.getKey();
            particle.setVelocity(entry.getValue());
            particle.setCoordinates(nextCoordinates.get(particle));
        }
        board.recomputeParticlesCell();
        board.getNeighbours(Board.Method.CIM);
        return board;
    }

    public void writeToFile(String path) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path), BoardSequenceDto.from(this));

    }


    public void writeVaToFile(String path) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path), VaDto.from(this));

    }


    @Override
    public Iterator<Board> iterator() {
        index = 0;
        return new Iterator<Board>() {
            @Override
            public boolean hasNext() {
                return index < periods;
            }

            @Override
            public Board next() {
//                if (!hasNext()) {
//                    throw new NoSuchElementException();
//                }
                if (index == 0) {
                    index++;
                    return resetBoard();
                }
                index++;
                return getNextBoard();
            }
        };
    }
}
