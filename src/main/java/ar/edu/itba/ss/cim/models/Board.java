package ar.edu.itba.ss.cim.models;


import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private final int M;
    private final Cell[][] cells;

    private final Set<Particle> boardParticles = new HashSet<>();

    private final double boardLength;
    private final double cellLength;


    public double getInteractionRadius() {
        return interactionRadius;
    }

    private final double interactionRadius;

    public int getTime() {
        return time;
    }


    private int time = 0;
    private final InteractionRadiusComputer interactionRadiusComputer;

    private final NeighbourCellsComputer neighbourCellsComputer;

    private final BoundaryConditions boundaryConditions;


    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                board.append(cell).append(" ");
            }
            // Remove the trailing space and add a newline
            board.setLength(board.length() - 1);
            board.append("\n");
        }
        // Remove the trailing newline
        board.setLength(board.length() - 1);
        return "Board{" +
                "M=" + M +
                ", boardLength=" + boardLength +
                ", cellLength=" + cellLength +
                ", time=" + time +
                ", interactionRadius=" + interactionRadius +
                ",\n cells=\n" + board +
                '}';
    }

    public Board(int M, double boardLength, double interactionRadius, BoundaryConditions boundaryConditions) {
        this.M = M;
        this.interactionRadius = interactionRadius;
        this.boardLength = boardLength;
        this.cellLength = boardLength / (double) M;
        this.cells = new Cell[M][M];
        this.boundaryConditions = boundaryConditions;
        this.neighbourCellsComputer = boundaryConditions.getNeighbourCellsComputer();
        this.interactionRadiusComputer = boundaryConditions.getInteractionRadiusComputer(boardLength, interactionRadius);

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                this.cells[i][j] = new Cell();
            }
        }
    }

    public static int computeOptimalM(double boardLength, double interactionRadius, Collection<Particle> particles) {
        Iterator<Particle> it = particles.iterator();
        double maxRadius = it.next().getRadius();

        while (it.hasNext()) {
            double radius = it.next().getRadius();
            maxRadius = Math.max(radius, maxRadius);
        }

        return (int) Math.floor(boardLength/(interactionRadius + maxRadius*2));
    }




    private void clearCells() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                cells[i][j].clearCell();
            }
        }
    }

    public int getM() {
        return M;
    }

    public void setTime(int time) {
        this.time = time;
    }



    public Set<Particle> getAllParticles() {
        return boardParticles;
    }

    public double getBoardLength() {
        return boardLength;
    }

    private void computeParticleCell(Particle particle) {
        double x = particle.getX();
        double y = particle.getY();
        if (x > boardLength || y > boardLength || x < 0 || y < 0) {
            throw new IllegalArgumentException("particle does not fit inside board");
        }
        int row = (int) (y / cellLength);
        int col = (int) (x / cellLength);
        this.cells[row][col].addParticle(particle);
    }

    public void recomputeParticlesCell() {
        clearCells();
        for (Particle particle : boardParticles) {
            computeParticleCell(particle);
        }
    }

    public void addParticle(Particle particle) {
        boardParticles.add(particle);
        computeParticleCell(particle);
    }

    public void addParticles(Collection<Particle> particles) {
        for (Particle particle : particles) {
            addParticle(particle);
        }
        this.boardParticles.addAll(particles);
    }

    public void computeNeighbours(Cell cell1, Cell cell2) {
        Set<Particle> cell1Particles = cell1.getParticles();
        Set<Particle> cell2Particles = cell2.getParticles();
        for (Particle particle : cell1Particles) {
            for (Particle otherParticle : cell2Particles) {
                if (!particle.equals(otherParticle) && interactionRadiusComputer.isWithinInteractionRadius(particle, otherParticle)) {
                    particle.addNeighbour(otherParticle);
                    otherParticle.addNeighbour(particle);
                }
            }
        }
    }


    private void clearNeighbours() {
        boardParticles.forEach(Particle::clearNeighbours);
    }


    public Map<Particle, Set<Particle>> getNeighbours(Method method) {
        clearNeighbours();
        Map<Particle, Set<Particle>> neighbours = new HashMap<>();
        switch (method) {
            case CIM:
                Set<Pair<Cell>> comparedCells = new HashSet<>();
                for (int i = 0; i < M; i++) {
                    for (int j = 0; j < M; j++) {
                        Cell cell = cells[i][j];
                        Collection<Cell> neighbourCells = neighbourCellsComputer.getNeighbourCells(cells, i, j);
                        for (Cell otherCell : neighbourCells) {
                            Pair<Cell> pair = Pair.of(cell, otherCell);
                            if (!comparedCells.contains(pair)) {
                                comparedCells.add(pair);
                                computeNeighbours(cell, otherCell);
                            }
                        }
                    }
                }
                break;
            case BRUTE_FORCE:
                for (Particle particle : boardParticles) {
                    for (Particle otherParticle : boardParticles) {
                        if (!particle.equals(otherParticle) && interactionRadiusComputer.isWithinInteractionRadius(particle, otherParticle)) {
                            particle.addNeighbour(otherParticle);
                            otherParticle.addNeighbour(particle);
                        }
                    }
                }
        }


        for (Particle particle : boardParticles) {
            neighbours.put(particle, particle.getNeighbours());
        }

        return neighbours;
    }

    public enum Method {
        BRUTE_FORCE,
        CIM
    }

    @FunctionalInterface
    private interface InteractionRadiusComputer {
        boolean isWithinInteractionRadius(Particle particle1, Particle particle2);
    }

    @FunctionalInterface
    private interface NeighbourCellsComputer {
        Collection<Cell> getNeighbourCells(Cell[][] cells, int i, int j);
    }

    public enum BoundaryConditions {
        PERIODIC {
            public NeighbourCellsComputer getNeighbourCellsComputer() {
                return (cells, i, j) -> {
                    int M = cells.length;
                    Set<Cell> neighbourCells = new HashSet<>();

                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            int xIndex = (x < 0 ? M - 1 : (x >= M ? 0 : x));
                            int yIndex = (y < 0 ? M - 1 : (y >= M ? 0 : y));
                            neighbourCells.add(cells[xIndex][yIndex]);
                        }
                    }

                    return neighbourCells;
                };
            }

            @Override
            public InteractionRadiusComputer getInteractionRadiusComputer(double boardLength, double interactionRadius) {
                return (Particle p1, Particle p2) -> {
                    if (super.getInteractionRadiusComputer(boardLength, interactionRadius).isWithinInteractionRadius(p1, p2)) {
                        return true;
                    }
                    double horizontalDistance = Math.abs(p1.getX() - p2.getX());
                    double verticalDistance = Math.abs(p1.getY() - p2.getY());
                    double alternativeHorizontalDistance = boardLength - horizontalDistance;
                    double alternativeVerticalDistance = boardLength - verticalDistance;

                    horizontalDistance = Math.min(horizontalDistance, alternativeHorizontalDistance);
                    verticalDistance = Math.min(verticalDistance, alternativeVerticalDistance);


                    return (Math.sqrt(Math.pow(horizontalDistance, 2) + Math.pow(verticalDistance, 2)) - p1.getRadius() - p2.getRadius()) < interactionRadius;

                };
            }
        },
        NOT_PERIODIC {
            public NeighbourCellsComputer getNeighbourCellsComputer() {
                return (cells, i, j) -> {
                    int M = cells.length;
                    Set<Cell> neighbourCells = new HashSet<>();

                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            if (x >= 0 && x < M && y >= 0 && y < M) {
                                neighbourCells.add(cells[x][y]);
                            }
                        }
                    }

                    return neighbourCells;
                };
            }
        };

        public abstract NeighbourCellsComputer getNeighbourCellsComputer();

        public InteractionRadiusComputer getInteractionRadiusComputer(double boardLength, double interactionRadius) {
            return (Particle particle1, Particle particle2) -> {
                double totalDistance = particle1.getDistanceTo(particle2);

                totalDistance -= particle1.getRadius();
                totalDistance -= particle2.getRadius();

                return totalDistance <= interactionRadius;
            };
        }


    }

}
