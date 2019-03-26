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

    public String readString(int offset, int size) {
        return new String(readBytes(offset, size));
    }
}
