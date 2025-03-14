package org.mobile.utils;
import java.io.File;

import static org.mobile.config.LogConfig.logInfo;

public class FileUtil {

    public static void cleanTestOutputFolder() {
        File testOutputDir = new File("test-output");
        if (testOutputDir.exists()) {
            deleteDirectoryContents(testOutputDir);
            logInfo("[Test-OutputFile] test-output folder cleaned successfully.");
        } else {
            logInfo("[Test-OutputFile] test-output folder does not exist. No need to clean.");
        }
    }

    private static void deleteDirectoryContents(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryContents(file);
                }
                file.delete();
            }
        }
    }
}

