package MethodButtons;
import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class IViewSalesByProductButton implements IMethodObserver {
    public void Invoke() throws IOException {
        JSONObject salesData = JSONHandler.readFrom(JSONHandler.SalesJsonFilePath);
        JSONObject branches = salesData.getJSONObject("branches");
        JSONObject productSales = new JSONObject();

        // Aggregate sales data by product
        for (String branchName : branches.keySet()) {
            JSONObject branchData = branches.getJSONObject(branchName);
            JSONArray items = branchData.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String itemName = item.getString("itemName");
                double revenue = item.getDouble("revenue");

                if (!productSales.has(itemName)) {
                    productSales.put(itemName, 0.0);
                }
                productSales.put(itemName, productSales.getDouble(itemName) + revenue);
            }
        }

        // Display aggregated sales by product
        System.out.println("Sales by Product:");
        for (String productName : productSales.keySet()) {
            System.out.println(productName + ": Total Revenue - " + productSales.getDouble(productName));
        }
    }
}
