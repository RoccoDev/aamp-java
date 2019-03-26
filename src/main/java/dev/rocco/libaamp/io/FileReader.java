package dev.rocco.libaamp.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class FileReader {

    private File inputFile;
    private byte[] fileContents;

    public FileReader(File inputFile) {
        this.inputFile = inputFile;
    }

    public void parse() {
        try(FileChannel channel = (FileChannel)
                Files.newByteChannel(Paths.get(inputFile.toURI()), StandardOpenOption.READ)) {

            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            channel.read(buffer);
            buffer.flip();

            fileContents = buffer.array();

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] readBytes(int offset, int size) {
        return Arrays.copyOfRange(fileContents, offset, offset + size);
    }

    private ByteBuffer wrap(byte[] in) {
        ByteBuffer bb = ByteBuffer.wrap(in);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
    }

    public String readString(int offset, int size) {
        return new String(readBytes(offset, size));
    }

    public String readString(int offset) {
        byte[] copy = Arrays.copyOfRange(fileContents, offset, fileContents.length - 1);
        for(int i = 0; i < copy.length; i++) {
          if(copy[i] == 0) return new String(Arrays.copyOfRange(fileContents, offset, offset + i));
        }
        return null;
    }

    public int readInt(int offset) {
        return wrap(readBytes(offset, 4)).getInt();
    }

    public float readFloat(int offset) {
        return wrap(readBytes(offset, 4)).getFloat();
    }
}
