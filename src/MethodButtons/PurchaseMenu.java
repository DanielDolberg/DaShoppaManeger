package MethodButtons;
import MenuClasses.IMethodObserver;
//import ShopClasses.CustomerClasses.CustomerManager;
import ShopClasses.CustomerClasses.CustomerManager;
import Utilities.JSONHandler;
import org.json.JSONArray;
import org.json.JSONObject;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PurchaseMenu implements IMethodObserver {
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
                System.out.println("Would you like to make new customer? yes / no");
                String answer = scanner.nextLine();
                if(answer.equals("no")) {
                    return;
                }
                System.out.println(" Please enter details to add a new customer:");
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
        System.out.println("Perform a Purchase on behalf of " + customer.getString("fullName"));
        Scanner scanner = new Scanner(System.in);

        // Select branch and item to purchase
        JSONObject allBranchs = getBranchs();
        JSONArray allBranchsNames = new JSONArray(allBranchs.keySet());
        System.out.println("Select branch:");
        for (int i = 0; i < allBranchsNames.length(); i++) {
            System.out.println((i + 1) + ". " + allBranchsNames.getString(i));
        }
        // # System.out.println("Select branch (1. Rishon Letzion, 2. Ashdod): ");
        int branchChoice = scanner.nextInt();
        scanner.nextLine();  // Clear buffer
        String branch = allBranchsNames.getString(branchChoice);

        // Fetch items from the branch
        // # JSONArray items = getBranchItems(branch);
        JSONArray items = allBranchs.getJSONArray(branch);
        displayItems(items);

        System.out.println("Enter item number to purchase: ");
        int itemIndex = scanner.nextInt() - 1;
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
        saveBranchItems(branch, items);
        CustomerManager.addCustomer(customer);  // Update customer data
    }

    private JSONObject getBranchs() throws IOException {
        // Read from JSON and create an array
        JSONObject jsonData;
        jsonData = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);
        return jsonData.getJSONObject("branches");
    }

    private JSONArray getBranchItems(String branchName) throws IOException {
        // Read from JSON and create an array
        JSONObject jsonData;
        jsonData = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);

        // Get the array for
        JSONObject branchesArray = jsonData.getJSONObject("branches");
        JSONArray branchNameArray = branchesArray.getJSONArray(branchName);
        return branchNameArray;
        //
        //String content = new String(Files.readAllBytes(Paths.get(JSONHandler.StockJsonFilePath)));
        //JSONObject json = new JSONObject(content);
        //System.out.println("testing 1.");
        //return json.getJSONObject("branches").getJSONArray(branchName);
    }

    private void saveBranchItems(String branchName, JSONArray items) throws IOException {
        // Similar to the inventory loading, this function saves the updated branch items
    }

    private void displayItems(JSONArray items) {
        System.out.println("testing 2.");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            System.out.println((i + 1) + ". " + item.getString("itemName") +
                    " - Price: " + item.getDouble("price") +
                    ", Stock: " + item.getInt("amountInStock"));
        }
    }

    private void updateCustomerStatus(JSONObject customer) {
        String currentStatus = customer.getString("status");
        if (currentStatus.equals("New")) {
            customer.put("status", "Returning");
        } else if (currentStatus.equals("Returning")) {
            customer.put("status", "VIP");
        }
    }
}
