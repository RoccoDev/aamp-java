package dev.rocco.libaamp.aamp.type;

public class AampString {

    private String wrappedString;
    private int size;

    public AampString(String raw, int size) {
        this.wrappedString = raw;
        this.size = size;
    }

    public String getWrappedString() {
        return wrappedString;
    }

    public int getSize() {
        return size;
    }
}
