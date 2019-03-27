package dev.rocco.libaamp.aamp;

import dev.rocco.libaamp.io.FileReader;
import dev.rocco.libaamp.io.FileWriter;

import java.util.HashMap;

public class ParameterList {

    private int offset;
    private int crc32Hash;

    private int objCount, listCount;

    private HashMap<Integer, ParameterList> childLists = new HashMap<>();
    private HashMap<Integer, ParameterObject> childObjects = new HashMap<>();

    public ParameterList(int offset) {
        this.offset = offset;
    }

    public void parse(FileReader reader) {
        crc32Hash = reader.readInt(offset);
        int currentObjOffset = offset + 4 * reader.readShort(offset + 8);
        objCount = reader.readShort(offset + 10);

        for(int i = 0; i < objCount; i++) {
            ParameterObject object = new ParameterObject(currentObjOffset);
            object.parse(reader);

            childObjects.put(object.crc32Hash, object);

            currentObjOffset += 8;
        }

        int currentListOffset = offset + 4 * reader.readShort(offset + 4);
        listCount = reader.readShort(offset + 6);

        for(int j = 0; j < listCount; j++) {
            ParameterList list = new ParameterList(currentListOffset);
            list.parse(reader);

            childLists.put(list.crc32Hash, list);

            currentListOffset += 12;
        }
    }

    public void write(FileWriter writer) {
        int offset = writer.getCurrentOffset();
        writer.writeInt(offset, crc32Hash);

        writer.writeInt(offset + 0x6, childLists.size());
        writer.writeInt(offset + 0xa, childObjects.size());
    }

    public HashMap<Integer, ParameterList> getChildLists() {
        return childLists;
    }

    public HashMap<Integer, ParameterObject> getChildObjects() {
        return childObjects;
    }

    public int getListCount() {
        return listCount;
    }

    public int getObjectCount() {
        return objCount;
    }

    public int getCrc32Hash() {
        return crc32Hash;
    }
}
