package MethodButtons;
import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static MainClass.Main.littleErrorMessage;

public class ViewAllCustomersButton implements IMethodObserver
{
    public void Invoke() throws IOException {
        try {
            // read from JSON, make an array string of all customers
            JSONArray customersArray = null;
            JSONObject jsonData = JSONHandler.readFrom(JSONHandler.CustomersJsonFilePath);
            customersArray = jsonData.getJSONArray("clients");

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
        } catch (IOException e) {
            System.out.println(littleErrorMessage);
            System.err.println("File I/O Exception: " + e);
            return; // Go back to the menu
        } catch (JSONException e) {
            System.out.println(littleErrorMessage);
            System.err.println("JSONException: " + e);
            return; // Go back to the menu
        }
    }
}