package com.learning.TikaTask.app;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileReading {
    private int numberOfFiles;
    private int numberOfDirs;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void findFiles(File file, ExecutorService executor){
        if(file.isFile()){
            System.out.println(file.getAbsoluteFile());
            numberOfFiles++;
            executor.submit(() -> processFile(file));
        } else {
            numberOfDirs++;
            File[] files = file.listFiles();
            for(File tempFile: files){
                findFiles(tempFile, executor);
            }
        }
    }

    public static boolean isFilePathValid(String filePath) {
        try {
            Path path = Paths.get(filePath);
            // Check if the file exists and is a regular file
            return Files.exists(path) && Files.isRegularFile(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false; // Invalid path format or null
        }
    }

    public void processFile(File file) {
    }

    public static void main(String[] args) {
        String rootPath = "/home/anudeep/IdeaProjects";

        FileReading fr = new FileReading();
        File file = new File(rootPath);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        fr.findFiles(file, executor);
        System.out.println(fr);
    }
}
