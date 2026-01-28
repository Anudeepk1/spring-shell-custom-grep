package com.learning.TikaTask.task;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@ShellComponent
public class Main {

    private List<String> filePaths = new ArrayList<>();

    @ShellMethod("Count occurrences of the given string in the file")
    public int countOccurrence(String search, String inputPath, String outputPath) {
        findFiles(new File(inputPath), null); // Pass null ExecutorService for simplicity
        int totalCount = 0;
        for (String filePath : filePaths) {
            int count = stringOccurrence(search, filePath);
            System.out.println("Occurrences in " + filePath + ": " + count);
            totalCount += count;
        }
        return totalCount;
    }

    public void findFiles(File file, ExecutorService executor) {
        if (file.isFile()) {
            filePaths.add(file.getAbsolutePath());
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File tempFile : files) {
                    findFiles(tempFile, executor);
                }
            }
        }
    }

    private int stringOccurrence(String search, String inputPath) {
        int wordCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (word.equalsIgnoreCase(search)) {
                        wordCount++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordCount;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }
}

