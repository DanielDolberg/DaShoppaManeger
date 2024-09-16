package MethodButtons;
import Utilities.JSONHandler;
import MenuClasses.IMethodObserver;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewAllAccountsButton implements IMethodObserver
{
    public void Invoke()
    {
        // read from JSON, make an array string of all customers
        JSONObject jsonData = JSONHandler.readFrom("J&sons/workers.json");

        /// Get the arrays for workers and admins
        JSONArray workersArray = jsonData.getJSONArray("workers");
        JSONArray adminsArray = jsonData.getJSONArray("admins");

        // Print table header
        System.out.printf("%-20s %-5s %-15s %-12s %-20s %-15s%n", "Full Name", "ID", "Phone Number", "Account Number", "Branch Name", "Job Role");
        System.out.println("---------------------------------------------------------------------------------------------");

        // Print each worker's information in table format
        printAccountsArray(workersArray);

        // Print each admin's information in table format
        printAccountsArray(adminsArray);
    }

    // Method to print the accounts from a JSONArray
    private static void printAccountsArray(JSONArray accountsArray) {
        for (int i = 0; i < accountsArray.length(); i++) {
            JSONObject account = accountsArray.getJSONObject(i);
            String fullName = account.getString("fullName");
            int id = account.getInt("ID");
            String phoneNumber = account.getString("phoneNumber");
            int accountNumber = account.getInt("accountNumber");
            String branchName = account.getString("branchName");
            String jobRole = account.getString("jobRole");

            // Print account information
            System.out.printf("%-20s %-5d %-15s %-12d %-20s %-15s%n", fullName, id, phoneNumber, accountNumber, branchName, jobRole);
        }
    }
}
