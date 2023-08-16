package ar.edu.itba.ss.cim.utils;

import java.io.IOException;

@FunctionalInterface
public interface Runner {
    void run(Arguments args) throws IOException;
}
