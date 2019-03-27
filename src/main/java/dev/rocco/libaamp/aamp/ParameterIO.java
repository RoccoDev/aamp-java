package dev.rocco.libaamp.aamp;

public class ParameterIO {

    private String type;
    private int version;

    private ParameterList rootList;

    public ParameterIO(String type, int version, ParameterList rootList) {
        this.type = type;
        this.version = version;
        this.rootList = rootList;
    }

    public String getType() {
        return type;
    }

    public int getVersion() {
        return version;
    }

    public ParameterList getRootList() {
        return rootList;
    }
}
