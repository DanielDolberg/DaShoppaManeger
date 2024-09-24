package MethodButtons;
import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import MainClass.Main;

import java.io.IOException;

public class ViewAllStockInThisBranchButton  implements IMethodObserver
{
    public void Invoke() throws IOException {
        // read from JSON, make an array string of all customers
        JSONObject jsonData = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);
        JSONObject branches = jsonData.getJSONObject("branches");
        String branchName = Main.workerBranch;
        JSONArray itemsArray = branches.getJSONObject(branchName).getJSONArray("items");

        // Print table header
        System.out.println("Stock in " + branchName);
        System.out.printf("%-20s %-12s %-15s%n", "Item Name", "Price", "Amount In Stock");
        System.out.println("--------------------------------------------------------------");

        // Print each item's information in table format
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject item = itemsArray.getJSONObject(i);
            String itemName = item.getString("itemName");
            double price = item.getDouble("price");
            int amountInStock = item.getInt("amountInStock");

            // Print item information
            System.out.printf("%-20s %-12.2f %-15d%n", itemName, price, amountInStock);
        }
    }
}
