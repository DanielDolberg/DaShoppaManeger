package MethodButtons;

import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static MainClass.Main.littleErrorMessage;

public class IViewStockInAllBranchesButton implements IMethodObserver
{
    public void Invoke() throws IOException {
        try {
            JSONObject jsonData = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);
            JSONObject branches = jsonData.getJSONObject("branches");

            System.out.printf("%-20s %-12s %-15s %-20s%n", "Item Name", "Price", "Amount In Stock", "Branch");
            System.out.println("-----------------------------------------------------------------------");

            for (String branchName : branches.keySet()) {
                JSONArray itemsArray = branches.getJSONObject(branchName).getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    String itemName = item.getString("itemName");
                    double price = item.getDouble("price");
                    int amountInStock = item.getInt("amountInStock");

                    System.out.printf("%-20s %-12.2f %-15d %-20s%n", itemName, price, amountInStock, branchName);
                }
            }
        } catch (IOException e) {
            System.out.println(littleErrorMessage);
            System.err.println("File I/O Exception: " + e);
        } catch (JSONException e) {
            System.out.println(littleErrorMessage);
            System.err.println("JSONException: " + e);
        }
    }
}