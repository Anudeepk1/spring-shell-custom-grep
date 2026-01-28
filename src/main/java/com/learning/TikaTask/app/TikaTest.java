package com.learning.TikaTask.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TikaTest{

    public static void main(String[] args) throws IOException, TikaException, SAXException {
        String filePath = "/home/anudeep/Downloads/Spring Boot .docx";
        File file = new File(filePath);

        if (file.exists()) {
            BodyContentHandler handler = new BodyContentHandler(-1); // Passing -1 to prevent content truncation
            Metadata metadata = new Metadata();
            FileInputStream inputStream = new FileInputStream(file);
            ParseContext parseContext = new ParseContext();


            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream, handler, metadata, parseContext);

            System.out.println("Contents of the document:");
            System.out.println(handler);
            System.out.println("------------------------");

            // Print metadata if needed
            System.out.println("Metadata of the document:");
            String[] metadataNames = metadata.names();
            for (String name : metadataNames) {
                System.out.println(name + " :  " + metadata.get(name));
            }

            inputStream.close();
        } else {
            System.err.println("File not found!");
        }
    }
}
