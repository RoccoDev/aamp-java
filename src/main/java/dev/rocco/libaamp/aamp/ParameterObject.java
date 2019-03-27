package dev.rocco.libaamp.aamp;

import dev.rocco.libaamp.io.FileReader;

import java.util.HashMap;

public class ParameterObject {

    private int offset;

    int crc32Hash;

    private HashMap<Integer, AampParameter> children = new HashMap<>();

    public ParameterObject(int offset) {
        this.offset = offset;
    }

    public void parse(FileReader reader) {
        crc32Hash = reader.readInt(offset);

        int currentChildOffset = offset + 4 * reader.readShort(offset + 4);
        int childrenCount = reader.readShort(offset + 6);

        for(int i = 0; i < childrenCount; i++) {
            AampParameter param = new AampParameter(currentChildOffset);
            param.parse(reader);

            children.put(param.crc32Hash, param);
            currentChildOffset += 8;
        }
    }

    public int getCrc32Hash() {
        return crc32Hash;
    }

    public HashMap<Integer, AampParameter> getChildren() {
        return children;
    }
}
