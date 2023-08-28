package ar.edu.itba.ss.cim;


import ar.edu.itba.ss.cim.utils.Arguments;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        Arguments argsObj;
        if (args.length == 0) {
//            argsObj = new Arguments(new int[]{50,100,150,200,300,400,500,600,700,800,1000,1200,1400,2000,2400,3200,3800}, new double[]{20}, 1, 6000, new double[]{2.5}, Arguments::regularRunner); // Change densityRunner to regularRunner or noiseRunner
            argsObj = new Arguments(new int[]{400,40,100,400}, new double[]{10,Math.sqrt(10),5,10}, 1, 6000, new double[]{5,0.1,0.2,0.4,0.6,0.8,1.2,1.4,1.6,1.8,2,2.2,2.4,2.6,2.8,3,3.2,3.4,3.6,3.8,4,4.2,4.4,4.6,4.8,5}, Arguments::regularRunner); // Change densityRunner to regularRunner or noiseRunner
        } else {
            argsObj = Arguments.parseArguments(args);
        }

        argsObj.run();
    }
}
