package ar.edu.itba.ss.cim.utils;

import ar.edu.itba.ss.cim.helper.FileNamesWrapper;
import ar.edu.itba.ss.cim.models.Board;
import ar.edu.itba.ss.cim.models.BoardSequence;
import ar.edu.itba.ss.cim.models.DensityDataMainWrapper;
import ar.edu.itba.ss.cim.models.NoiseDataMainWrapper;
import ar.edu.itba.ss.cim.models.StaticStats;
import ar.edu.itba.ss.cim.models.TemporalCoordinates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arguments {
    private final int[] numberOfParticles;
    private final double[] boardLengths;
    private final double interactionRadius;
    private final int times;

    public double[] getNoise() {
        return noise;
    }

    private final double[] noise;

    private final Runner runner;

    public Arguments(int[] numberOfParticles, double[] boardLengths, double interactionRadius, int times, double[] noise, Runner runner) {
        this.numberOfParticles = numberOfParticles;
        this.boardLengths = boardLengths;
        this.interactionRadius = interactionRadius;
        this.times = times;
        this.noise = noise;
        this.runner = runner;
    }

    public void run() throws IOException {
        this.runner.run(this);
    }

    public static void regularRunner(Arguments args) throws IOException {
        int[] numberOfParticles = args.getNumberOfParticles();
        double interactionRadius = args.getInteractionRadius();
        double[] boardLength = args.getBoardLengths();
        double[] noise = args.getNoise();
        int periods = args.getTimes();
        double maxSpeed = 0.03;


        for (int particlesNumber : numberOfParticles) {
            for (Double n : noise) {
                FileNamesWrapper fileNameWrapper = Fileparser.generateInputData(particlesNumber, boardLength[0], maxSpeed);

                String STATIC_FILE_PATH = fileNameWrapper.StaticFileName;
                String DYNAMIC_FILE_PATH = fileNameWrapper.DynamicFileName;
                StaticStats staticStats = Fileparser.parseStaticFile(STATIC_FILE_PATH);
                TemporalCoordinates temporalCoordinates = Fileparser.parseDynamicFile(DYNAMIC_FILE_PATH);
                BoardSequence boardSequence = new BoardSequence(staticStats, temporalCoordinates, n, interactionRadius, Board.BoundaryConditions.NOT_PERIODIC, periods);
                boardSequence.writeToFile("sequence" + fileNameWrapper.getId() + ".json");
                break;
            }
            break;
        }
    }

    public static double calculateMedian(double[] values) {
        Arrays.sort(values);

        int length = values.length;
        if (length % 2 == 0) {
            int middleIndex1 = length / 2 - 1;
            int middleIndex2 = length / 2;
            return (values[middleIndex1] + values[middleIndex2]) / 2.0;
        } else {
            int middleIndex = length / 2;
            return values[middleIndex];
        }
    }

    public static void densityRunner(Arguments args) throws IOException {
        final int ITER = 20;
        int[] numberOfParticles = args.getNumberOfParticles();
        double interactionRadius = args.getInteractionRadius();
        double[] boardLengths = args.getBoardLengths();
        int periods = args.getTimes();
        double noise = args.getNoise()[0];
        double maxSpeed = 0.03;
        DensityDataMainWrapper data = new DensityDataMainWrapper();
        for (int particleNumber : numberOfParticles) {
            for (double l : boardLengths) {
                double density = particleNumber/Math.pow(l,2);
                for (int i=0 ; i< ITER ; i++) {

                    FileNamesWrapper fileNameWrapper = Fileparser.generateInputData(particleNumber, l, maxSpeed);

                    String STATIC_FILE_PATH = fileNameWrapper.StaticFileName;
                    String DYNAMIC_FILE_PATH = fileNameWrapper.DynamicFileName;
                    StaticStats staticStats = Fileparser.parseStaticFile(STATIC_FILE_PATH);
                    TemporalCoordinates temporalCoordinates = Fileparser.parseDynamicFile(DYNAMIC_FILE_PATH);
                    BoardSequence boardSequence = new BoardSequence(staticStats, temporalCoordinates, noise, interactionRadius, Board.BoundaryConditions.NOT_PERIODIC, periods);
                    double va = boardSequence.getVa();
                    Files.delete(Paths.get(STATIC_FILE_PATH));
                    Files.delete(Paths.get(DYNAMIC_FILE_PATH));

                    data.addData(particleNumber, va, density);
                }
            }
        }
        data.writeFile("density.json");
        }

    public static void noiseRunner(Arguments args) throws IOException {
        int[] numberOfParticles = args.getNumberOfParticles();
        double interactionRadius = args.getInteractionRadius();
        double[] boardLength = args.getBoardLengths();
        double[] noise = args.getNoise();
        int periods = args.getTimes();
        double maxSpeed = 0.03;
        final int ITER = 10;
        NoiseDataMainWrapper data = new NoiseDataMainWrapper();
        for (int particleNumber : numberOfParticles) {
            for (double nois : noise) {
                for (int i=0 ; i< ITER ; i++) {
                    FileNamesWrapper fileNameWrapper = Fileparser.generateInputData(particleNumber, boardLength[0], maxSpeed);

                String STATIC_FILE_PATH = fileNameWrapper.StaticFileName;
                String DYNAMIC_FILE_PATH = fileNameWrapper.DynamicFileName;
                StaticStats staticStats = Fileparser.parseStaticFile(STATIC_FILE_PATH);
                TemporalCoordinates temporalCoordinates = Fileparser.parseDynamicFile(DYNAMIC_FILE_PATH);
                BoardSequence boardSequence = new BoardSequence(staticStats, temporalCoordinates, nois, interactionRadius, Board.BoundaryConditions.NOT_PERIODIC, periods);
                double va = boardSequence.getVa();
                data.addData(particleNumber, va, nois);
                Files.delete(Paths.get(STATIC_FILE_PATH));
                Files.delete(Paths.get(DYNAMIC_FILE_PATH));
                }

            }
        }
        data.writeFile("noise.json");
        }
public int[] getNumberOfParticles() { return numberOfParticles;
    }

    public double[] getBoardLengths() {
        return boardLengths;
    }

    public double getInteractionRadius() {
        return interactionRadius;
    }

    public int getTimes() {
        return times;
    }



    public static Arguments parseArguments(String[] args) {
        List<Integer> numberOfParticles = new ArrayList<>();
        List<Double> noiseList = new ArrayList<>();
        List<Double> boardLengths = new ArrayList<>();
        double interactionRadius = -1;
        int times = -1;

        if (args.length == 0) {
            throw new RuntimeException("Arguments not pased");
        }

        Runner runner = switch(args[0]) {
            case "regular" -> Arguments::regularRunner;
            case "noise" -> Arguments::noiseRunner;
            default -> throw new RuntimeException("Invalid argument " + args[0]);
        };

        for (int i = 1; i < args.length; i += 2) {
            String flag = args[i];
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException("Missing argument value for flag: " + flag);
            }
            String value = args[i + 1];

            switch (flag) {
                case "-n" -> numberOfParticles.add(Integer.parseInt(value));
                case "-l" -> boardLengths.add(Double.parseDouble(value));
                case "-r" -> interactionRadius = Double.parseDouble(value);
                case "-t" -> times = Integer.parseInt(value);
                case "-noise" -> noiseList.add(Double.parseDouble(value));
                default -> throw new IllegalArgumentException("Unknown flag: " + flag);
            }
        }

        if (numberOfParticles.isEmpty() || boardLengths.isEmpty() || interactionRadius == -1 || times == -1 ) {
            throw new IllegalArgumentException("Missing or incomplete arguments.");
        }
        int[] intArray = new int[numberOfParticles.size()];

        for (int i = 0; i < numberOfParticles.size(); i++) {
            intArray[i] = numberOfParticles.get(i);
        }
        double[] boardArray = new double[boardLengths.size()];

        for (int i = 0; i < boardLengths.size(); i++) {
            boardArray[i] = boardLengths.get(i);
        }
        double[] intArrayM;
        if (noiseList.isEmpty()) {
            intArrayM = new double[]{0};
        } else {
            intArrayM = new double[noiseList.size()];
        }

        for (int i = 0; i < noiseList.size(); i++) {
            intArrayM[i] = noiseList.get(i);
        }
        return new Arguments(intArray, boardArray, interactionRadius, times, intArrayM, runner);
    }
}
