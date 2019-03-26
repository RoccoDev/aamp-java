package dev.rocco.libaamp.aamp;

import dev.rocco.libaamp.io.FileReader;

import java.io.File;

public class AampFile {

    private ParameterIO parameterIO;
    private AampHeader header;

    private FileReader reader;

    public AampFile(File input) {
        reader = new FileReader(input);
        reader.parse();

        header = new AampHeader();
        parameterIO = header.parse(reader);
    }

    public AampHeader getHeader() {
        return header;
    }

    public ParameterIO getParameterIO() {
        return parameterIO;
    }

    public FileReader getReader() {
        return reader;
    }
}
