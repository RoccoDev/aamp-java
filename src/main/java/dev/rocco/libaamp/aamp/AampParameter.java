package dev.rocco.libaamp.aamp;

import dev.rocco.libaamp.aamp.type.*;
import dev.rocco.libaamp.io.FileReader;

public class AampParameter {

    int crc32Hash;

    private int offset;
    private ParameterTypes type;

    private Object value;

    public AampParameter(int offset) {
        this.offset = offset;
    }

    public void parse(FileReader reader) {
        crc32Hash = reader.readInt(offset);

        int offsetField = reader.readInt(offset + 4);

        int dataOffset = offset + 4 * (offsetField & 0xffffff);
        int paramType = reader.readByte(offset + 0x7);
        type = ParameterTypes.values()[paramType];

        System.out.println(type);

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
        }
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
