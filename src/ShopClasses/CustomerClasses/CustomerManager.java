package ShopClasses.CustomerClasses;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.IOException;

public class CustomerManager {
    private static final String CUSTOMER_FILE_PATH = "path/to/customers.json";

    public static JSONArray getCustomerData() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(CUSTOMER_FILE_PATH)));
        JSONObject json = new JSONObject(content);
        return json.getJSONArray("clients");
    }

    public static void addCustomer(JSONObject newCustomer) throws IOException {
        JSONArray customers = getCustomerData();
        customers.put(newCustomer);

        JSONObject updatedData = new JSONObject();
        updatedData.put("clients", customers);

        try (FileWriter file = new FileWriter(CUSTOMER_FILE_PATH)) {
            file.write(updatedData.toString(4));  // Indent for formatting
        }
    }

    public static JSONObject findCustomerByID(String id) throws IOException {
        JSONArray customers = getCustomerData();
        for (int i = 0; i < customers.length(); i++) {
            JSONObject customer = customers.getJSONObject(i);
            if (customer.getString("ID").equals(id)) {
                return customer;
            }
        }
        return null;  // Customer not found
    }
}
