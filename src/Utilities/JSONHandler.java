package Utilities;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONHandler {

    private final static Object mutEx = new Object();

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

    public static void writeTo(String JSONFilePath, JSONObject infoToWriteToFile){

        new Thread(new Runnable()
            {
                @Override
                public void run() {
                    synchronized(mutEx) {
                        try {
                            FileWriter file = new FileWriter(JSONFilePath);
                            file.write(infoToWriteToFile.toString());
                            file.close();
                        } catch (Exception ex) {
                            System.err.println(ex);
                        }
                    }
                }
            }).start();
    }
}
