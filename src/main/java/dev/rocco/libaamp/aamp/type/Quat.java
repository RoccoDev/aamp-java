package dev.rocco.libaamp.aamp.type;

public class Quat {

    private float a, b, c, d;

    public Quat(float[] values) {
        this.a = values[0];
        this.b = values[1];
        this.c = values[2];
        this.d = values[3];
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getC() {
        return c;
    }

    public float getD() {
        return d;
    }
}
