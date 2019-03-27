package dev.rocco.libaamp.aamp;

import dev.rocco.libaamp.aamp.type.*;
import dev.rocco.libaamp.io.FileReader;
import dev.rocco.libaamp.io.FileWriter;

public class AampParameter {

    int crc32Hash;

    private int offset;
    private ParameterTypes type;

    private Object value;

    public AampParameter(int offset) {
        this.offset = offset;
    }

    public int getCrc32Hash() {
        return crc32Hash;
    }

    public void parse(FileReader reader) {
        crc32Hash = reader.readInt(offset);

        int offsetField = reader.readInt(offset + 4);

        int dataOffset = offset + 4 * (offsetField & 0xffffff);
        int paramType = reader.readByte(offset + 0x7);
        type = ParameterTypes.values()[paramType];

        switch(type) {
            case BOOL:
                value = reader.readInt(dataOffset) != 0;
                break;

            case INT:
            case U32:
                value = reader.readInt(dataOffset);
                break;
            case F32:
                value = reader.readFloat(dataOffset);
                break;

            case STRING32:
                value = parseString(reader, dataOffset, 32);
                break;
            case STRING64:
                value = parseString(reader, dataOffset, 64);
                break;
            case STRING256:
                value = parseString(reader, dataOffset, 256);
                break;
            case STRING_REF:
                value = parseString(reader, dataOffset, -1);
                break;

            case VEC2:
                value = new Vec2(reader.readFloat(dataOffset), reader.readFloat(dataOffset + 4));
                break;
            case VEC3:
                value = new Vec3(reader.readFloat(dataOffset), reader.readFloat(dataOffset + 4), reader.readFloat(dataOffset + 8));
                break;
            case VEC4:
                value = new Vec4(reader.readFloat(dataOffset), reader.readFloat(dataOffset + 4),
                        reader.readFloat(dataOffset + 8), reader.readFloat(dataOffset + 12));
                break;
            case COLOR:
                value = new Color(reader.readFloat(dataOffset), reader.readFloat(dataOffset + 4),
                        reader.readFloat(dataOffset + 8), reader.readFloat(dataOffset + 12));
                break;

            case QUAT:
                float[] values = new float[4];
                for(int i = 0; i < 4; i++) {
                    values[i] = reader.readFloat(dataOffset + 4 * i);
                }
                value = new Quat(values);
                break;

            case U32_BUFFER:
            case INT_BUFFER:
                int size = reader.readInt(dataOffset - 4);
                int[] result = new int[size];
                for(int i = 0; i < size; i++) {
                    result[i] = reader.readInt(dataOffset + 4 * i);
                }
                value = result;
                break;

            case F32_BUFFER:
                int sz = reader.readInt(dataOffset - 4);
                float[] rs = new float[sz];
                for(int i = 0; i < sz; i++) {
                    rs[i] = reader.readFloat(dataOffset + 4 * i);
                }
                value = rs;
                break;

            case BINARY_BUFFER:
                int bufferSize = reader.readInt(dataOffset - 4);
                value = reader.readBytes(dataOffset, bufferSize);
                break;

            case CURVE1:
            case CURVE2:
            case CURVE3:
            case CURVE4:
                int numCurves = type.ordinal() - ParameterTypes.CURVE1.ordinal() + 1;
                int[] ints = new int[2 * numCurves];
                float[] floats = new float[30 * numCurves];

                for(int i = 0; i < numCurves; i++) {
                    for(int ii = 0; i < 2; i++) {
                        ints[i + ii] = reader.readInt(dataOffset + 0x80 * i + 4 * ii);
                    }

                    for(int f = 0; f < 30; f++) {
                        floats[i + f] = reader.readFloat(dataOffset + 0x80 * i + 8 + 4 * f);
                    }
                }
                value = new Curve(ints, floats);

                break;

            case SPECIAL:
            default:
                System.err.println("Unable to parse type " + type);
                break;
        }
    }

    public AampString parseString(FileReader reader, int offset, int maxSize) {
        int dataSize = reader.getStringOffset(offset) - offset;
        int stringLength = maxSize == -1 ? dataSize : Math.min(dataSize, maxSize);

        byte[] raw = reader.readBytes(offset, stringLength);

        return new AampString(new String(raw), dataSize);
    }

    public void write(FileWriter writer) {
        int offset = writer.getCurrentOffset();
        writer.writeInt(offset, crc32Hash);

        writer.writeByte(offset + 0x7, (byte) type.ordinal());

    }

    public ParameterTypes getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }
}
