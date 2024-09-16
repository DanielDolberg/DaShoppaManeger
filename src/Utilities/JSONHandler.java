package Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONHandler {
    public static JSONObject readFrom(String JSONFilePath){
        JSONObject jsonData;
        try {
            // Read the file content into a String
            String content = new String(Files.readAllBytes(Paths.get(JSONFilePath)));

            // Parse the content into a JSONObject
            jsonData = new JSONObject(content);

        } catch (IOException e) {
            System.err.println("IO Exception!");
            jsonData = new JSONObject();
        }
        return jsonData;
    }

    public static void writeTo(String JSONFilePath, String infoToWriteToFile){
        try {
            // Read the file content into a String
            String content = new String(Files.readAllBytes(Paths.get(JSONFilePath)));

            // Parse the content into a JSONObject
            JSONObject jsonData = new JSONObject(content);

        } catch (IOException e) {
            System.err.println("IO Exception!");
        }
    }
}
