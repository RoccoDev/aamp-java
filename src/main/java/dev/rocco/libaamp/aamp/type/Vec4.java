package dev.rocco.libaamp.aamp.type;

public class Vec4 {

    private float x, y, z, fourth;

    public Vec4(float x, float y, float z, float fourth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.fourth = fourth;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getFourth() {
        return fourth;
    }
}
