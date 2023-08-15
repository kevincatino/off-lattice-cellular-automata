package ar.edu.itba.ss.cim.utils;

import java.util.ArrayList;
import java.util.List;

public class Arguments {
    private final int[] numberOfParticles;
    private final int boardLength;
    private final double interactionRadius;
    private final int times;

    public Double[] getMs() {
        return noise;
    }

    private final Double[] noise;

    public Arguments(int[] numberOfParticles, int boardLength, double interactionRadius, int times, Double[] noise) {
        this.numberOfParticles = numberOfParticles;
        this.boardLength = boardLength;
        this.interactionRadius = interactionRadius;
        this.times = times;
        this.noise = noise;
    }

    public int[] getNumberOfParticles() {
        return numberOfParticles;
    }

    public int getBoardLength() {
        return boardLength;
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
        int boardLength = -1;
        double interactionRadius = -1;
        int times = -1;

        for (int i = 0; i < args.length; i += 2) {
            String flag = args[i];
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException("Missing argument value for flag: " + flag);
            }
            String value = args[i + 1];

            switch (flag) {
                case "-n" -> numberOfParticles.add(Integer.parseInt(value));
                case "-l" -> boardLength = Integer.parseInt(value);
                case "-r" -> interactionRadius = Double.parseDouble(value);
                case "-t" -> times = Integer.parseInt(value);
                case "-noise" -> noiseList.add(Double.parseDouble(value));
                default -> throw new IllegalArgumentException("Unknown flag: " + flag);
            }
        }

        if (numberOfParticles.isEmpty() || boardLength == -1 || interactionRadius == -1 || times == -1 ) {
            throw new IllegalArgumentException("Missing or incomplete arguments.");
        }
        int[] intArray = new int[numberOfParticles.size()];

        for (int i = 0; i < numberOfParticles.size(); i++) {
            intArray[i] = numberOfParticles.get(i);
        }
        Double[] intArrayM = new Double[noiseList.size()];

        for (int i = 0; i < noiseList.size(); i++) {
            intArrayM[i] = noiseList.get(i);
        }
        return new Arguments(intArray, boardLength, interactionRadius, times, intArrayM);
    }
}
