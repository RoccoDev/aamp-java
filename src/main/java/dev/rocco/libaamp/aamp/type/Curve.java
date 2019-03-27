package dev.rocco.libaamp.aamp.type;

public class Curve {

    private int[] ints;
    private float[] floats;

    public Curve(int[] ints, float[] floats) {
        this.ints = ints;
        this.floats = floats;
    }

    public int[] getInts() {
        return ints;
    }

    public float[] getFloats() {
        return floats;
    }
}
