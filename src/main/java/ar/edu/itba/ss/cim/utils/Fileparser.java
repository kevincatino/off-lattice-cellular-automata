package ar.edu.itba.ss.cim.utils;


import ar.edu.itba.ss.cim.helper.FileNamesWrapper;
import ar.edu.itba.ss.cim.models.Coordinates;
import ar.edu.itba.ss.cim.models.Properties;
import ar.edu.itba.ss.cim.models.StaticStats;
import ar.edu.itba.ss.cim.models.TemporalCoordinates;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public interface Fileparser {
    static List<TemporalCoordinates> parseDynamicFile(String filePath) throws IOException {

        String dynamicFileText = IO.readFile(filePath);

        List<TemporalCoordinates> timeData = new ArrayList<>();
        Scanner scanner = new Scanner(dynamicFileText);

        String line;
        Integer currentTime = null;

        TemporalCoordinates tc = null;
        Integer idCounter = null;

        while ((scanner.hasNextLine() && (line = scanner.nextLine().trim()) != null)) {

            if (line.contains(" ")) {
                if (idCounter == null || tc == null) {
                    throw new RuntimeException("Invalid dynamic file format");
                }
                String[] parts = line.split("\s+");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                tc.addCoordinates(idCounter++, Coordinates.of(x, y));
            } else {
                currentTime = Integer.parseInt(line);
                idCounter = 1;
                tc = new TemporalCoordinates(currentTime);
                timeData.add(currentTime, tc);
            }
        }

        scanner.close();
        return timeData;
    }

    static FileNamesWrapper generateInputData(int numberOfParticles, double boardLength, double interactionLength, int intervals) throws IOException {
        FileNamesWrapper fileNames = new FileNamesWrapper("static" , "dynamic" );
        FileWriter writer = new FileWriter(fileNames.DynamicFileName);

        Random rand = new Random();
            BufferedWriter dynamicFileBuffer = new BufferedWriter(writer);
        for (int time=0 ; time<intervals ; time++) {
            dynamicFileBuffer.write(String.format("%d\n",time));
            for (int i=0 ; i<numberOfParticles ; i++) {

               double x = rand.nextDouble(boardLength);
                double y = rand.nextDouble(boardLength);
                dynamicFileBuffer.write(String.format("%f %f\n",x,y));
            }
        }

        dynamicFileBuffer.close();

        writer = new FileWriter(fileNames.StaticFileName);
            BufferedWriter staticFileBuffer = new BufferedWriter(writer);
            staticFileBuffer.write(String.format("%d\n%f\n",numberOfParticles,boardLength));
            for (int i=0 ; i<numberOfParticles ; i++) {
               double r = rand.nextDouble(boardLength/200);
                staticFileBuffer.write(String.format("%f\n",r));
            }

        staticFileBuffer.close();
        return fileNames;

    }


    static StaticStats parseStaticFile(String filePath) throws IOException {

        String staticFileText = IO.readFile(filePath);

        HashMap<Integer, List<Coordinates>> timeData = new HashMap<>();
        Scanner scanner = new Scanner(staticFileText);

        Map<Integer, Properties> propertiesMap = new HashMap<>();
        String line;
        int numberOfParticles = 0;
        double length = 0;
        int lineNumber = 0;

        while ((scanner.hasNextLine() && (line = scanner.nextLine().trim()) != null)) {
            lineNumber++;

            if (lineNumber == 1) {
                numberOfParticles = Integer.parseInt(line.trim());
            } else if (lineNumber == 2) {
                length = Double.parseDouble(line.trim());
            } else {
                String[] parts = line.split("\s+");
                float radius = Float.parseFloat(parts[0]);
                int particleId = lineNumber - 2;
                propertiesMap.put(particleId, new Properties(radius, parts.length > 1 ? parts[1]: ""));
            }
        }

        scanner.close();

        return new StaticStats(numberOfParticles, length, propertiesMap);
    }
}
