package MethodButtons;
import org.json.JSONArray;
import org.json.JSONObject;
import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;

import java.util.Scanner;

public class AddNewWorkerButton implements IMethodObserver {

    @Override
    // Function to register a new account and write to the JSON file
    public void Invoke() {
        // Read existing data
        JSONObject jsonData = JSONHandler.readFrom(JSONHandler.WorkersJsonFilePath);
        JSONArray workersArray = jsonData.getJSONArray("workers");

        Scanner scanner = new Scanner(System.in);

        // Collect new user details
        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.println("Enter branch name: ");
        String branchName = scanner.nextLine();

        System.out.println("Enter job role: ");
        String jobRole = scanner.nextLine();

        String username;
        boolean usernameExists;
        do {
            usernameExists = false;
            System.out.println("Enter username: ");
            username = scanner.nextLine();

            // Check if username already exists
            for (int i = 0; i < workersArray.length(); i++) {
                JSONObject worker = workersArray.getJSONObject(i);
                if (worker.getString("username").equals(username)) {
                    usernameExists = true;
                    System.out.println("Username already exists, please choose another one.");
                    break;
                }
            }
        } while (usernameExists);

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        // Calculate new ID and account number (auto-increment)
        int newID = workersArray.length() + 1;
        int newAccountNumber = newID * 111;

        // Create new worker JSONObject
        JSONObject newWorker = new JSONObject();
        newWorker.put("fullName", fullName);
        newWorker.put("ID", newID);
        newWorker.put("phoneNumber", phoneNumber);
        newWorker.put("accountNumber", newAccountNumber);
        newWorker.put("branchName", branchName);
        newWorker.put("jobRole", jobRole);
        newWorker.put("username", username);
        newWorker.put("password", password);

        // Append new worker to workersArray
        workersArray.put(newWorker);

        JSONHandler.writeTo(JSONHandler.WorkersJsonFilePath,jsonData);
    }
}
