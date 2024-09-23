package MethodButtons;

import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewStockInAllBranchesButton  implements IMethodObserver
{
    public void Invoke() {
        // Read from JSON to get the branches
        JSONObject jsonData = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);
        JSONObject branches = jsonData.getJSONObject("branches");

        // Print table header
        System.out.printf("%-20s %-12s %-15s %-20s%n", "Item Name", "Price", "Amount In Stock", "Branch");
        System.out.println("-----------------------------------------------------------------------");

        // Loop through all branches
        for (String branchName : branches.keySet()) {
            JSONArray itemsArray = branches.getJSONObject(branchName).getJSONArray("items");

            // Loop through each item in the branch
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject item = itemsArray.getJSONObject(i);
                String itemName = item.getString("itemName");
                double price = item.getDouble("price");
                int amountInStock = item.getInt("amountInStock");

                // Print item information along with the branch name
                System.out.printf("%-20s %-12.2f %-15d %-20s%n", itemName, price, amountInStock, branchName);
            }
        }
    }
}