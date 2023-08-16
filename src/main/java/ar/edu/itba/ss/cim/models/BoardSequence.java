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
        Iterator<Board> it = iterator();
        Board b = null;
        while(it.hasNext())
            b = it.next(); // Podriamos usar un criterio para definir con codigo si llega al estacionario o no
        if(b == null) {
            throw new RuntimeException();
        }
        double vx = 0;
        double vy = 0;
        double vmod = -1;
        Collection<Particle> particles = b.getAllParticles();
        for (Particle p : particles) {
            Velocity v = p.getVelocity();
            vx += v.getVx();
            vy += v.getVy();
            if (vmod == -1) {
                vmod = v.getMod();
            }

        }
        double mod = Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2));
        return mod/(vmod* particles.size()); // TODO check if okay
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
        for (Particle particle : particles) {
            Collection<Particle> neighbours = particle.getNeighbours();
            Coordinates nextCoordinates = particle.getCoordinates().getNext(particle.getVelocity());
            Velocity nextVelocity = particle.getVelocity().getNext(neighbours, noise);
            particle.setCoordinates(nextCoordinates);
            particle.setVelocity(nextVelocity);
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
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
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
