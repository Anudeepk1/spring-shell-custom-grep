package com.learning.TikaTask.app;

//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import org.springframework.shell.standard.ShellOption;
//
//import javax.swing.plaf.metal.MetalButtonUI;
//
//@ShellComponent
//public class ShellApp {
//
//
//}

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.*;

@ShellComponent
public class ShellApp {

    @ShellMethod("Count occurrences of a string in a file")
    public String countOccurrences(String searchString) {
        String inputPath = "/home/anudeep/Documents/word.txt";
        String outputPath = "/home/anudeep/Documents/output.txt";

        int count = countStringOccurrences(inputPath, searchString);

        writeOutput(outputPath, searchString, count);
        return "Occurrences counted and output written to directory: " + outputPath;
    }

    // Method to count occurrences of a string in a file
    private int countStringOccurrences(String filePath, String searchString) {
        int wordCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (word.equalsIgnoreCase(searchString)) {
                        wordCount++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordCount;
    }

    // Method to write output to a file in a directory
    private void writeOutput(String outputDirPath, String searchString, int count) {
        try {
            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Create the directory if it doesn't exist
            }

            File outputFile = new File(outputDir, "output.txt");
            FileWriter writer = new FileWriter(outputFile);
            writer.write("The string '" + searchString + "' occurred " + count + " times.");
            writer.close();

            System.out.println("Output written to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

