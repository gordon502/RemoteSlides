package org.example.files;


import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract class responsible for deleting/downlaoding only needed files
 */
public abstract class FileManager {

    /**
     * Removes not needed files and downloading new ones.
     * @param fileNames set of file names in "content" directory
     */
    public static void updateFilesInDirectory(Set<String> fileNames, FTPConnector ftpConnector, String ftpContentPath) {
        File dir = new File("./content");
        Set<String> dirFileNames = Arrays.stream(dir.list()).collect(Collectors.toSet());

        dirFileNames.forEach(fileName -> {
            if (!fileNames.contains(fileName) && !fileName.endsWith("db")) { //file not used in current program iteration
                new File("./content/" + fileName).delete(); //just delete it
            }
        });

        fileNames.forEach(file -> {
            if (!dirFileNames.contains(file)) {
                ftpConnector.downloadFile(ftpContentPath, file);
            }
        });

    }

}
