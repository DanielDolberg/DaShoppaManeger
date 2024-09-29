package MethodButtons;

import MenuClasses.IMethodObserver;
import ShopClasses.CustomerClasses.CustomerManager;
import Utilities.JSONHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

import static MainClass.Main.workerBranch;

public class IPurchaseMenu implements IMethodObserver {

    @Override
    public void Invoke() {
        Scanner scanner = new Scanner(System.in);

        // Get customer ID
        System.out.println("Enter Customer ID: ");
        String customerId = scanner.nextLine();

        try {
            JSONObject customer = CustomerManager.findCustomerByID(customerId);
            if (customer == null) {
                // If customer does not exist, prompt to add new customer
                System.out.println("Customer not found.");
                System.out.println("Would you like to make a new customer? yes / no");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("no")) {
                    return;
                }
                System.out.println("Please enter details to add a new customer:");
                System.out.print("Full Name: ");
                String fullName = scanner.nextLine();
                System.out.print("Phone Number: ");
                String phoneNumber = scanner.nextLine();

                JSONObject newCustomer = new JSONObject();
                newCustomer.put("fullName", fullName);
                newCustomer.put("ID", customerId);
                newCustomer.put("phoneNumber", phoneNumber);
                newCustomer.put("status", "New");

                // Add the new customer to JSON
                CustomerManager.addCustomer(newCustomer);
                customer = newCustomer;
            }

            // Proceed with purchase
            processPurchase(customer);

        } catch (IOException e) {
            System.out.println("Error handling customer data: " + e.getMessage());
        }
    }

    private void processPurchase(JSONObject customer) throws IOException {
        System.out.println("Perform a purchase on behalf of " + customer.getString("fullName") + " who is a " + customer.getString("status") + " customer.");
        Scanner scanner = new Scanner(System.in);

        // Select branch and item to purchase
        JSONObject branches = getBranches();
        if (workerBranch.equals("All")) {
            // If workerBranch is "All", prompt the user to select a branch
            System.out.println("Select a branch:");
            String[] branchNames = JSONObject.getNames(branches);
            for (int i = 0; i < branchNames.length; i++) {
                System.out.println((i + 1) + ". " + branchNames[i]);
            }

            int branchChoice = scanner.nextInt() - 1;
            scanner.nextLine(); // Clear buffer
            if (branchChoice < 0 || branchChoice >= branchNames.length) {
                System.out.println("Invalid branch selection!");
                return;
            }
            workerBranch = branchNames[branchChoice]; // Set the selected branch
        }

        // Proceed to get items from the selected branch
        JSONArray items = branches.getJSONObject(workerBranch).getJSONArray("items");
        displayItems(items);

        System.out.println("Enter item number to purchase: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // Clear buffer
        if (itemIndex < 0 || itemIndex >= items.length()) {
            System.out.println("Invalid item number!");
            return;
        }

        JSONObject selectedItem = items.getJSONObject(itemIndex);

        // Update item stock
        int stock = selectedItem.getInt("amountInStock");
        if (stock > 0) {
            selectedItem.put("amountInStock", stock - 1);
            System.out.println("Purchased " + selectedItem.getString("itemName") + " successfully.");
        } else {
            System.out.println("Item is out of stock.");
            return;
        }

        // Update customer status
        updateCustomerStatus(customer);

        // Save updated inventory and customer status
        saveBranchItems(workerBranch, items);
    }


    private JSONObject getBranches() throws IOException {
        // Read from JSON and create an array
        JSONObject jsonData = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);
        return jsonData.getJSONObject("branches");
    }

    private void saveBranchItems(String branchName, JSONArray items) throws IOException {
        // Read the current stock file
        JSONObject jsonData = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);

        // Update the branch items
        jsonData.getJSONObject("branches").getJSONObject(branchName).put("items", items);

        // Write the updated data back to the file
        JSONHandler.writeTo(JSONHandler.StockJsonFilePath, jsonData);
        System.out.println("Branch items updated successfully.");
    }

    private void displayItems(JSONArray items) {
        System.out.println("Available items:");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            System.out.println((i + 1) + ". " + item.getString("itemName") +
                    " - Price: " + item.getDouble("price") +
                    ", Stock: " + item.getInt("amountInStock"));
        }
    }

    private void updateCustomerStatus(JSONObject customer) throws IOException {
        String currentStatus = customer.getString("status");

        // Update the status based on current state
        if (currentStatus.equals("New")) {
            customer.put("status", "Returning");
        } else if (currentStatus.equals("Returning")) {
            customer.put("status", "VIP");
        }

        // Save the updated customer data to the file
        JSONArray customers = JSONHandler.readFrom(JSONHandler.CustomersJsonFilePath).getJSONArray("clients");

        // Find the customer in the array and update them
        for (int i = 0; i < customers.length(); i++) {
            JSONObject existingCustomer = customers.getJSONObject(i);
            if (existingCustomer.getString("ID").equals(customer.getString("ID"))) {
                customers.put(i, customer);  // Update the customer data
                break;
            }
        }

        // Write the updated customers list back to the JSON file
        JSONObject jsonData = new JSONObject();
        jsonData.put("clients", customers);
        JSONHandler.writeTo(JSONHandler.CustomersJsonFilePath, jsonData);
        System.out.println("Customer status updated successfully.");
    }

}
