package ar.edu.itba.ss.cim;


import ar.edu.itba.ss.cim.utils.Arguments;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        Arguments argsObj;
        if (args.length == 0) {
            argsObj = new Arguments(new int[]{5, 10, 30, 50, 100}, new double[]{10,20,30, 40, 50, 60}, 1, 30, new double[]{0.1, 0.2, 0.3, 0.4, 0.5}, Arguments::densityRunner); // Change densityRunner to regularRunner or noiseRunner
        } else {
            argsObj = Arguments.parseArguments(args);
        }

        argsObj.run();
    }
}
