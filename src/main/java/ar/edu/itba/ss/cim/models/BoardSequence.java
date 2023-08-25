package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.dto.BoardSequenceDto;
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



    public double getVa() {
        final double DELTA = 0.008;
        final int MAX_TIMES = 2000;
        final int TIMES = 2000;
        int counter = TIMES;
        int idx = 0;
        Iterator<Board> it = iterator();
        Board b = it.next();
        double prevVa = b.getCurrentVa();
        while(true) {
            idx++;
            b = it.next();
            double newVa = b.getCurrentVa();
//            System.out.printf("prev: %f, new: %f\n", prevVa,newVa);
            if (Math.abs(newVa - prevVa) < DELTA) {
                counter--;
            } else {
                counter = TIMES;
            }
            prevVa = newVa;
            if (counter == 0){
                System.out.println("Times: " + idx);
                break;
            }
            else if (idx == MAX_TIMES) {
                System.out.println("STABLE VALUE OF va COULD NOT BE FOUND");
                break;
            }
        }
        return prevVa;
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
