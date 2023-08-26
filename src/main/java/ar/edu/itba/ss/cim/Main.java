package ar.edu.itba.ss.cim;


import ar.edu.itba.ss.cim.utils.Arguments;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        Arguments argsObj;
        if (args.length == 0) {
            argsObj = new Arguments(new int[]{50,100,150,200,300,400,500,600,700,800,1000,1200,1400,2000,2400,3200,3800}, new double[]{20}, 1, 6000, new double[]{2.5}, Arguments::densityRunner); // Change densityRunner to regularRunner or noiseRunner
//            argsObj = new Arguments(new int[]{40,80,120}, new double[]{2}, 1, 100, new double[]{1.5, 2, 3, 4}, Arguments::regularRunner); // Change densityRunner to regularRunner or noiseRunner
        } else {
            argsObj = Arguments.parseArguments(args);
        }

        argsObj.run();
    }
}
