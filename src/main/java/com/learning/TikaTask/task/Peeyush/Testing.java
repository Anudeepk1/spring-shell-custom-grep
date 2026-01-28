package com.learning.TikaTask.task.Peeyush;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ShellComponent
public class Testing {

    private int folderCounter = 1000;

    @ShellMethod("Search for a string in files within a directory using a thread pool")
    public String searchFiles(@ShellOption String directoryPath, String searchTerm, String logFilePath) {

        if (directoryPath.isEmpty() || searchTerm.isEmpty() || logFilePath.isEmpty()) {
            return "Invalid input parameters. Please provide valid values for directoryPath, searchTerm, and logFilePath.";
        }

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return "Invalid directory path.";
        }
        List<File> files = findFiles(directory);

        List<String> foundPaths = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            executorService.submit(() -> {
                int count = processFile(files, searchTerm);
                generateLogFile(logFilePath, searchTerm, foundPaths, count);
            });

        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return "Search completed. Log file created at: " + logFilePath;
    }

    private void generateLogFile(String logFilePath, String searchTerm, List<String> foundPaths, int occurrencesList) {
        // Append a file name to the logFilePath
        String logFileName = "log_" + folderCounter + ".txt";
        String fullLogFilePath = logFilePath + File.separator + logFileName;

        try (FileWriter writer = new FileWriter(fullLogFilePath, true)) {
            writer.write("Search term: " + searchTerm + "\n");

            writer.write("Found paths and occurrences:\n");

            // Iterate through the lists and write each entry
            for (int i = 0; i < foundPaths.size(); i++) {
                writer.write(foundPaths.get(i) + " - Occurrences: " + occurrencesList + "\n");
            }

            writer.write("\n");

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int countDir = createOutputDirectory(logFilePath, folderCounter);
    }

    private int createOutputDirectory(String outputDirectory, int folderCounter) {
        int count = folderCounter;
        File directory = new File(outputDirectory + File.separator + "log_" + folderCounter);
        if (directory.exists()) {
            return -1;
        } else {
            if (!directory.mkdirs()) {
                System.err.println("Error creating log directory: " + directory.getAbsolutePath());
                count++;
            }
        }
        return count;
    }

    private int processFile(List<File> files, String searchTerm) {
        int fileSize = files.size();

        if (fileSize > 0) {
            for (File file : files) {
                try (FileInputStream stream = new FileInputStream(file)) {
                    BodyContentHandler handler = new BodyContentHandler(-1);
                    Metadata metadata = new Metadata();
                    ParseContext context = new ParseContext();

                    AutoDetectParser parser = new AutoDetectParser();
                    parser.parse(stream, handler, metadata, context);

                    String content = handler.toString();
                    return countOccurrences(content, searchTerm);

                } catch (IOException | SAXException | TikaException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    private List<File> findFiles(File file) {
        List<File> fileList = new ArrayList<>();
        File[] elements = file.listFiles();
        if (elements != null) {
            for (File element : elements) {
                if (element.isFile()) {
                    fileList.add(element);

                } else if (element.isDirectory()) {
                    fileList.addAll(findFiles(element));
                }
            }
        }
        return fileList;
    }

    private int countOccurrences(String content, String searchTerm) {
        int wordCount = 0;
        int index = 0;

        while ((index = content.indexOf(searchTerm, index)) != -1) {
            wordCount++;
            index += searchTerm.length();
        }

        return wordCount;
    }
}
