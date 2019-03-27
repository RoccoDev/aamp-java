package dev.rocco.libaamp.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.zip.CRC32;

public class Crc32 {

    public static HashMap<Integer, String> knownHashes = new HashMap<>();

    private static void loadHash(String s) {
        CRC32 crc = new CRC32();
        crc.update(s.getBytes());
        knownHashes.put((int)crc.getValue(), s);
    }

    public static void loadAllHashes(File file) throws IOException {
        Files.readAllLines(Paths.get(file.toURI())).forEach(Crc32::loadHash);
    }
}
