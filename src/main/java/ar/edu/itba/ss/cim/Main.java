package ar.edu.itba.ss.cim;


import ar.edu.itba.ss.cim.utils.Arguments;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        Arguments argsObj;
        if (args.length == 0) {
            argsObj = new Arguments(new int[]{40}, new double[]{2,2.2,2.4,2.7,4.2,5}, 1, 3000, new double[]{0.1, 0.3, 0.5, 0.7, 1}, Arguments::densityRunner); // Change densityRunner to regularRunner or noiseRunner
        } else {
            argsObj = Arguments.parseArguments(args);
        }

        argsObj.run();
    }
}
