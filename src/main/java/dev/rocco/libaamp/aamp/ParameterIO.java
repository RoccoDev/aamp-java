package dev.rocco.libaamp.aamp;

public class ParameterIO {

    private String type;
    private int version;

    public ParameterIO(String type, int version) {
        this.type = type;
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public int getVersion() {
        return version;
    }
}
