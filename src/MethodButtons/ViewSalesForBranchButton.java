package MethodButtons;
import org.json.JSONArray;
import org.json.JSONObject;
import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import java.io.IOException;

public class ViewSalesForBranchButton implements IMethodObserver {

    @Override
    public void Invoke() throws IOException {
        JSONObject salesData = JSONHandler.readFrom(JSONHandler.SalesJsonFilePath);
        JSONObject branches = salesData.getJSONObject("branches");

        System.out.println("Sales statistics for branches:");
        for (String branchName : branches.keySet()) {
            JSONObject branchData = branches.getJSONObject(branchName);
            System.out.println("Branch: " + branchName);
            System.out.println("Total Amount Sold: " + branchData.getInt("totalAmountSold"));
            System.out.println("Total Revenue: " + branchData.getDouble("totalRevenue"));
            System.out.println("Items Sold:");
            JSONArray items = branchData.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                System.out.println(" - " + item.getString("itemName") + ": Revenue - " + item.getDouble("revenue"));
            }
            System.out.println("-------------------------------");
        }
    }
}