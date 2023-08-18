package ar.edu.itba.ss.cim.utils;


import ar.edu.itba.ss.cim.helper.FileNamesWrapper;
import ar.edu.itba.ss.cim.models.Coordinates;
import ar.edu.itba.ss.cim.models.Properties;
import ar.edu.itba.ss.cim.models.StaticStats;
import ar.edu.itba.ss.cim.models.TemporalCoordinates;
import ar.edu.itba.ss.cim.models.Velocity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public interface Fileparser {
    static TemporalCoordinates parseDynamicFile(String filePath) throws IOException {

        String dynamicFileText = IO.readFile(filePath);

        Scanner scanner = new Scanner(dynamicFileText);

        String line;

        TemporalCoordinates tc = new TemporalCoordinates(0);
        int idCounter = 1;

        while ((scanner.hasNextLine() && (line = scanner.nextLine()) != null)) {
                line = line.trim();
                String[] parts = line.split("\s+");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                tc.addCoordinates(idCounter, Coordinates.of(x, y));
                double vx = Double.parseDouble(parts[2]);
                double vy = Double.parseDouble(parts[3]);
                tc.addVelocity(idCounter++,Velocity.of(vx,vy));
        }

        scanner.close();
        return tc;
    }

    static FileNamesWrapper generateInputData(int numberOfParticles, double boardLength, double speed) throws IOException {
        FileNamesWrapper fileNames = new FileNamesWrapper("static" , "dynamic" );
        FileWriter writer = new FileWriter(fileNames.DynamicFileName);
        double ratio = 1.5;
        Random rand = new Random();
            BufferedWriter dynamicFileBuffer = new BufferedWriter(writer);
            for (int i=0 ; i<numberOfParticles ; i++) {

                double x = rand.nextDouble(boardLength/ratio) + (boardLength - boardLength/ratio)/2; // Do not wlloe particles to start in the border
                double y = rand.nextDouble(boardLength/ratio) + (boardLength - boardLength/ratio)/2;
                double angle = rand.nextDouble(2); //angle is in radians
                double vx = speed*Math.cos(angle*Math.PI);
                double vy = speed*Math.sin(angle*Math.PI);
                dynamicFileBuffer.write(String.format("%f %f %f %f\n",x,y, vx, vy));
            }

        dynamicFileBuffer.close();

        writer = new FileWriter(fileNames.StaticFileName);
            BufferedWriter staticFileBuffer = new BufferedWriter(writer);
            staticFileBuffer.write(String.format("%d\n%f\n",numberOfParticles,boardLength));
            for (int i=0 ; i<numberOfParticles ; i++) {
               double r = 0;
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

        while ((scanner.hasNextLine() && (line = scanner.nextLine()) != null)) {
            line = line.trim();
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
