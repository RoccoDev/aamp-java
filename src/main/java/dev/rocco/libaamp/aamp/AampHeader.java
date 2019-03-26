package dev.rocco.libaamp.aamp;

import dev.rocco.libaamp.io.FileReader;

import java.util.Arrays;

public class AampHeader {

    private byte[] magic;

    private int version;
    private int flags;
    private int fileSize;

    private int listNum, objNum, paramNum;

    private int dataSize, stringSize;

    public ParameterIO parse(FileReader reader) {
        magic = reader.readBytes(0, 4);

        if(!Arrays.equals(magic, "AAMP".getBytes())) {
            System.err.println("Invalid magic.");
        }

        version = reader.readInt(0x4);
        flags = reader.readInt(0x8);
        fileSize = reader.readInt(0xc);

        int pioVersion = reader.readInt(0x10);
        String pioType = reader.readString(0x30);

        listNum = reader.readInt(0x18);
        objNum = reader.readInt(0x1c);
        paramNum = reader.readInt(0x20);

        dataSize = reader.readInt(0x24);
        stringSize = reader.readInt(0x28);

        return new ParameterIO(pioType, pioVersion);
    }

    public byte[] getMagic() {
        return magic;
    }

    public int getVersion() {
        return version;
    }

    public int getFlags() {
        return flags;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getListNum() {
        return listNum;
    }

    public int getObjNum() {
        return objNum;
    }

    public int getParamNum() {
        return paramNum;
    }

    public int getDataSize() {
        return dataSize;
    }

    public int getStringSize() {
        return stringSize;
    }
}
