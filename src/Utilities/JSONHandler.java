package Utilities;

import ShopClasses.CustomerClasses.Customer;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONHandler {

    private final static Object mutEx = new Object();

    public final static String CustomersJsonFilePath = "J&sons/customers.json";
    public final static String StockJsonFilePath = "J&sons/stock.json";
    public final static String WorkersJsonFilePath = "J&sons/workers.json";
    public final static String SalesJsonFilePath = "J&sons/sales.json";

    public static JSONObject readFrom(String JSONFilePath) throws IOException, JSONException {
        JSONObject jsonData;

        // Read the file content into a String
        String content = new String(Files.readAllBytes(Paths.get(JSONFilePath)));

        // Parse the content into a JSONObject
        jsonData = new JSONObject(content);

        return jsonData;
    }

    public static void writeTo(String JSONFilePath, JSONObject infoToWriteToFile) throws IOException, JSONException {

        new Thread(new Runnable()
            {
                @Override
                public void run() {
                    synchronized(mutEx) {
                        try {
                            FileWriter file = new FileWriter(JSONFilePath);
                            file.write(infoToWriteToFile.toString(4));
                            file.close();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
            }).start();
    }
}
