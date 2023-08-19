package ar.edu.itba.ss.cim;


import ar.edu.itba.ss.cim.utils.Arguments;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        Arguments argsObj;
        if (args.length == 0) {
//            argsObj = new Arguments(new int[]{10,100,200,400,600,800,1000,1200,1400,1600,1800,2000,2200,2400,2600,2800,3000,3200}, new double[]{20}, 1, 3000, new double[]{1}, Arguments::densityRunner); // Change densityRunner to regularRunner or noiseRunner
            argsObj = new Arguments(new int[]{40,80,120}, new double[]{2}, 1, 150, new double[]{1.5, 2, 3, 4}, Arguments::regularRunner); // Change densityRunner to regularRunner or noiseRunner
        } else {
            argsObj = Arguments.parseArguments(args);
        }

        argsObj.run();
    }
}
