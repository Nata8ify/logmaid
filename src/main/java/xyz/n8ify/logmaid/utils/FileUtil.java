package xyz.n8ify.logmaid.utils;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File createFile(File outputDir, String fileName) throws IOException {
        File file = new File(outputDir, fileName);
        if (file.exists() && file.delete()) {
            System.out.println("File is deleted before create.");
        }
        if (file.createNewFile()) {
            return file;
        } else {
            throw new IllegalArgumentException(String.format("Cannot create file [%s]", file.getAbsolutePath()));
        }
    }

}
