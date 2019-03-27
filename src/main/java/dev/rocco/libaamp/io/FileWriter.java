package dev.rocco.libaamp.io;

import dev.rocco.libaamp.aamp.AampFile;
import dev.rocco.libaamp.aamp.AampHeader;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileWriter {

    private OffsetByteArrayOutputStream fileContents;
    private AampFile file;

    public FileWriter(AampFile file) {
        this.file = file;
        fileContents = new OffsetByteArrayOutputStream();
    }

    private void writeBytes(int offset, byte[] bytes) {

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        fileContents.write(bb.array(), offset);
    }

    public int getCurrentOffset() {
        return fileContents.getOffset();
    }

    public void writeBytes(int offset, ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        fileContents.write(buffer.array(), offset);
    }

    public void writeString(int offset, String s) {
        writeBytes(offset, s.getBytes());
    }

    public void writeInt(int offset, int write) {
        writeBytes(offset, ByteBuffer.allocate(4).putInt(write));
    }

    public void writeFloat(int offset, float write) {
        writeBytes(offset, ByteBuffer.allocate(4).putFloat(write));
    }

    public void writeShort(int offset, short write) {
        writeBytes(offset, ByteBuffer.allocate(2).putShort(write));
    }

    public void writeByte(int offset, byte write) {
        /* Skip endianness check, since we're only writing one byte. */
        fileContents.write(new byte[] {write}, offset, 1);
    }

    private void writeHeader() {
        writeString(0, "AAMP");
        AampHeader header = file.getHeader();

        writeInt(4, header.getVersion());
        writeInt(8, header.getFlags());

        writeInt(0x10, file.getParameterIO().getVersion());

        writeInt(0x2c, 0); // Unknown, see https://zeldamods.org/wiki/AAMP#Header

        writeString(0x30, file.getParameterIO().getType());

    }

    public void writeAll() {
        writeHeader();

        writeInt(12, fileContents.size());
    }

    public void writeToFile(File f) {
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println(fileContents.size());
            Files.write(Paths.get(f.toURI()), fileContents.toByteArray(), StandardOpenOption.WRITE);
            fileContents.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
