package ar.edu.itba.ss.cim;


import ar.edu.itba.ss.cim.dto.ExecutionStats;
import ar.edu.itba.ss.cim.helper.FileNamesWrapper;
import ar.edu.itba.ss.cim.models.Board;
import ar.edu.itba.ss.cim.models.BoardSequence;
import ar.edu.itba.ss.cim.models.ExecutionStatsWrapper;
import ar.edu.itba.ss.cim.models.Pair;
import ar.edu.itba.ss.cim.models.Particle;
import ar.edu.itba.ss.cim.models.StaticStats;
import ar.edu.itba.ss.cim.models.TemporalCoordinates;
import ar.edu.itba.ss.cim.utils.Arguments;
import ar.edu.itba.ss.cim.utils.Fileparser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {

        Arguments argsObj;
        if (args.length == 0) {
            argsObj = new Arguments(new int[]{80,160,320, 640},10,1,3, new Integer[]{6,7,8,9,10,11});
        } else {
            // Ejemplo:
            // java -jar ./neighbouring-particles-search-1.0-SNAPSHOT.jar -m 3 -m 4 -n 50 -n 100 -n 500 -n 1000 -l 10 -r 2.5 -t 5
            argsObj = Arguments.parseArguments(args);
        }
        int timeValues = argsObj.getTimes();
        int[] numberOfParticles = argsObj.getNumberOfParticles();
        double interactionRadius = argsObj.getInteractionRadius();
        double boardLength = argsObj.getBoardLength();
        Integer[] ms = argsObj.getMs();

        if (ms.length == 0) {
            ms = new Integer[]{null};
        }

        //Integer M = null;
        ExecutionStatsWrapper stats = new ExecutionStatsWrapper();
        for (int particlesNumber : numberOfParticles) {
            for (Integer m : ms) {
                FileNamesWrapper fileNameWrapper = Fileparser.generateInputData(particlesNumber, boardLength, interactionRadius, timeValues);

                String STATIC_FILE_PATH = fileNameWrapper.StaticFileName;
                String DYNAMIC_FILE_PATH = fileNameWrapper.DynamicFileName;
                StaticStats staticStats = Fileparser.parseStaticFile(STATIC_FILE_PATH);
                List<TemporalCoordinates> temporalCoordinates = Fileparser.parseDynamicFile(DYNAMIC_FILE_PATH);
                BoardSequence boardSequence = new BoardSequence(staticStats, temporalCoordinates, m, interactionRadius, Board.BoundaryConditions.NOT_PERIODIC);
                int actualM = boardSequence.getM();
                for (Board b : boardSequence) {
                    long start = System.currentTimeMillis();
                    b.getNeighbours(Board.Method.BRUTE_FORCE);
                    long end = System.currentTimeMillis();
                    long bruteForceComputationTime = end - start;
                    System.out.printf("Brute force Computation time: %d ms\n", bruteForceComputationTime);

                    start = System.currentTimeMillis();
                    b.getNeighbours(Board.Method.CIM);
                    end = System.currentTimeMillis();
                    long cimComputationTime = end - start;
                    System.out.printf("CIM Computation time: %d ms\n", cimComputationTime);

                    stats.addStats(particlesNumber, bruteForceComputationTime, cimComputationTime, actualM);
                }

                boardSequence.writeToFile("sequence" + fileNameWrapper.getId() + ".json");

                Particle.resetIdCounter();
            }
        }

        stats.writeToFile("stats.json");
        stats.writeMToFile("mstats.json");


    }
}
