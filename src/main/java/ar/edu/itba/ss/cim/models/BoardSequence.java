package ar.edu.itba.ss.cim.models;

import ar.edu.itba.ss.cim.dto.BoardSequenceDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BoardSequence implements Iterable<Board> {
    private final TemporalCoordinates temporalCoordinatesList;

    private final Set<Particle> particles = new HashSet<>();

    private int index;

    private final Board board;

    // If M is null, then the optimal value of M is utilized
    public BoardSequence(StaticStats staticStats, TemporalCoordinates initialCoordinates, Integer M, double interactionRadius, Board.BoundaryConditions boundaryConditions) {
        this.temporalCoordinatesList = initialCoordinates;
        Coordinates coordinates = Coordinates.of(0, 0);
        for (Map.Entry<Integer, Properties> idProp : staticStats.getIdPropertyPairs()) {
            Properties properties = idProp.getValue();
            Particle particle = new Particle(coordinates, properties);
            particles.add(particle);
        }
        if (M == null) {
            M = Board.computeOptimalM(staticStats.getBoardLength(),interactionRadius,particles);
        }
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



    private Board getNextBoard() {
        // TODO use formula to compute next state
        TemporalCoordinates tc = temporalCoordinatesList;
        board.setTime(tc.getTime());
        for (Particle particle : particles) {
            int id = particle.getId();
            Coordinates coordinates = tc.getCoordinates(id);
            particle.setCoordinates(coordinates);
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
                return index < 3; // TODO change
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                index++; // TODO change
                return getNextBoard();
            }
        };
    }
}
