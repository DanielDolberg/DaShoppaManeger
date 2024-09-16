package MethodButtons;
import Utilities.JSONHandler;
import MenuClasses.IMethodObserver;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewAllCustomersButton implements IMethodObserver
{
    public void Invoke()
    {
        // read from JSON, make an array string of all customers
        JSONArray customersArray = null;
        JSONObject jsonData = JSONHandler.readFrom("J&sons/LoginCredentials.json");
        customersArray = jsonData.getJSONArray("clients");
        /*
        try {
            // Specify the path to the JSON file
            String filePath = "J&sons/customers.json";

            // Read the file content into a String
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the content into a JSONObject
            JSONObject jsonData = new JSONObject(content);
            customersArray = jsonData.getJSONArray("clients");
        } catch (IOException e) {
            System.err.println("IO Exception!");
        }*/

        // print the array *in an orderly way
        // Print table header
        System.out.printf("%-20s %-12s %-15s %-10s%n", "Full Name", "ID", "Phone Number", "Status");
        System.out.println("--------------------------------------------------------------");

        // Print each customer's information in table format
        for (int i = 0; i < customersArray.length(); i++) {
            JSONObject customer = customersArray.getJSONObject(i);
            String fullName = customer.getString("fullName");
            String id = customer.getString("ID");
            String phoneNumber = customer.getString("phoneNumber");
            String status = customer.getString("status");

            // Print customer information
            System.out.printf("%-20s %-12s %-15s %-10s%n", fullName, id, phoneNumber, status);
        }
    }
}