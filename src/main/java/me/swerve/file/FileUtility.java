package me.swerve.file;

import lombok.SneakyThrows;
import org.bson.Document;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class FileUtility {
    /**
     * Read JSON from document
     *
     * @param file File to read
     * @return Parsed JSON
     */
    @SneakyThrows
    public static Document readFromFile(File file) {
        StringBuilder s = new StringBuilder();

        Scanner reader = new Scanner(file);
        while(reader.hasNextLine()) s.append(reader.nextLine());

        reader.close();
        return Document.parse(s.toString());
    }

    /**
     * Write JSON to file
     *
     * @param file File
     * @param document Document
     */
    @SneakyThrows
    public static void write(File file, Document document) {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(document.toJson());
        fileWriter.close();
    }
}
