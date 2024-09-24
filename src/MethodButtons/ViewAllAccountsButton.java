package MethodButtons;

import Utilities.JSONHandler;
import MenuClasses.IMethodObserver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ViewAllAccountsButton implements IMethodObserver {
    @Override
    public void Invoke() throws IOException, JSONException {
        // Read from JSON and create an array of all accounts
        JSONObject jsonData;
        jsonData = JSONHandler.readFrom(JSONHandler.WorkersJsonFilePath);

        // Get the array for workers (includes all accounts)
        JSONArray workersArray = jsonData.getJSONArray("workers");

        // Print table header
        System.out.printf("%-20s %-6s %-16s %-15s %-18s %-15s %-12s %-20s%n",
                "Full Name", "ID", "Phone Number", "Account Number", "Branch Name", "Job Role", "Username", "Password");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");

        // Print each worker's information in table format
        printAccountsArray(workersArray);
    }

    // Method to print the accounts from a JSONArray
    private static void printAccountsArray(JSONArray accountsArray) throws JSONException  {
        for (int i = 0; i < accountsArray.length(); i++) {
            JSONObject account = accountsArray.getJSONObject(i);
            String fullName = account.getString("fullName");
            int id = account.getInt("ID");
            String phoneNumber = account.getString("phoneNumber");
            int accountNumber = account.getInt("accountNumber");
            String branchName = account.getString("branchName");
            String jobRole = account.getString("jobRole");
            String username = account.getString("username");
            String password = account.getString("password");

            // Print account information
            System.out.printf("%-20s %-5d %-15s %-12d %-20s %-15s %-10s %-15s%n",
                    fullName, id, phoneNumber, accountNumber, branchName, jobRole, username, password);
        }
    }
}
