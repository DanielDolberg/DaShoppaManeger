package MethodButtons;

import org.json.JSONArray;
import org.json.JSONObject;
import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IViewSalesByCategoryButton implements IMethodObserver {

    @Override
    public void Invoke() throws IOException {
        JSONObject salesData = JSONHandler.readFrom(JSONHandler.SalesJsonFilePath);
        JSONObject branches = salesData.getJSONObject("branches");

        System.out.println("Sales statistics by category for branches:");

        for (String branchName : branches.keySet()) {
            JSONObject branchData = branches.getJSONObject(branchName);
            System.out.println("Branch: " + branchName);
            System.out.println("Total Amount Sold: " + branchData.getInt("totalAmountSold"));
            System.out.println("Total Revenue: " + branchData.getDouble("totalRevenue"));
            System.out.println("Sales by Category:");

            // Use a map to accumulate sales data by category
            Map<String, Double> categorySales = new HashMap<>();
            JSONArray items = branchData.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String category = item.getString("category");
                double revenue = item.getDouble("revenue");

                // Aggregate revenue by category
                categorySales.put(category, categorySales.getOrDefault(category, 0.0) + revenue);
            }

            // Display sales by category
            for (Map.Entry<String, Double> entry : categorySales.entrySet()) {
                System.out.println(" - " + entry.getKey() + ": Total Revenue - " + entry.getValue());
            }

            System.out.println("-------------------------------");
        }
    }
}
