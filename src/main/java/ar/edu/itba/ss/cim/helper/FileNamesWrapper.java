package ar.edu.itba.ss.cim.helper;

public class FileNamesWrapper {
    public final String StaticFileName;
    public final String DynamicFileName;


    private static int COUNTER = 1;

    public int getId() {
        return id;
    }

    private final int id;

    public FileNamesWrapper(String staticFileName, String dynamicFileName) {
        StaticFileName = staticFileName + COUNTER + ".txt";
        DynamicFileName = dynamicFileName + COUNTER + ".txt";
        id = COUNTER;
        COUNTER++;
    }
}
