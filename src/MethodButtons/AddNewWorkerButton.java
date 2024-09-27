package MethodButtons;
import Logs.LogManager;
import MainClass.Main;
import org.json.JSONArray;
import org.json.JSONObject;
import MenuClasses.IMethodObserver;
import Utilities.JSONHandler;
import ShopClasses.WorkerClasses.JobRole;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static MainClass.Main.littleErrorMessage;

public class AddNewWorkerButton implements IMethodObserver {

    @Override
    public void Invoke() throws IOException {
        // Read existing data
        JSONObject jsonData = JSONHandler.readFrom(JSONHandler.WorkersJsonFilePath);
        JSONArray workersArray = jsonData.getJSONArray("workers");

        Scanner scanner = new Scanner(System.in);

        // Validate full name
        String fullName;
        do {
            System.out.println("Enter full name: ");
            fullName = scanner.nextLine();
            if (!Pattern.matches("[a-zA-Z ]+", fullName) || fullName.isEmpty()) {
                System.out.println("Invalid full name. Please enter only letters and spaces.");
            }
        } while (!Pattern.matches("[a-zA-Z ]+", fullName) || fullName.isEmpty());

        // Validate phone number (e.g., must be in format +972-XX-XXXXXXX)
        String phoneNumber;
        do {
            System.out.println("Enter phone number (format: +972-XX-XXXXXXX): ");
            phoneNumber = scanner.nextLine();
            if (!Pattern.matches("\\+972-\\d{2}-\\d{7}", phoneNumber)) {
                System.out.println("Invalid phone number format. Please follow the format +972-XX-XXXXXXX.");
            }
        } while (!Pattern.matches("\\+972-\\d{2}-\\d{7}", phoneNumber));

        // Validate branch name (must be "Rishon Letzion", "Ashdod", or "All")
        String branchName;
        do {
            System.out.println("Enter branch name (Rishon Letzion, Ashdod, or All): ");
            branchName = scanner.nextLine();
            if (!branchName.equals("Rishon Letzion") &&
                    !branchName.equals("Ashdod") &&
                    !branchName.equals("All")) {
                System.out.println("Invalid branch name. Please enter 'Rishon Letzion', 'Ashdod', or 'All'.");
            }
        } while (!branchName.equals("Rishon Letzion") &&
                !branchName.equals("Ashdod") &&
                !branchName.equals("All"));

        // Validate job role
        String jobRoleStr;
        JobRole jobRole = null;
        do {
            System.out.println("Enter job role (Admin, ShiftManager, Cashier, SalesPerson): ");
            jobRoleStr = scanner.nextLine();
            try {
                jobRole = JobRole.valueOf(jobRoleStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid job role. Please choose one of the following: Admin, ShiftManager, Cashier, SalesPerson.");
            }
        } while (jobRole == null);

        // Validate username (already checks for uniqueness)
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

        // Validate password
        String password;
        do {
            System.out.println("Enter password (minimum 8 characters): ");
            password = scanner.nextLine();
            if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters long.");
            }
        } while (password.length() < 8);

        // Calculate new ID and account number (auto-increment)
        int newID = workersArray.length() + 1;
        int newAccountNumber = newID * 111;

        // Create new worker JSONObject
        JSONObject newWorker = new JSONObject();
        newWorker.put("password", password);
        newWorker.put("phoneNumber", phoneNumber);
        newWorker.put("fullName", fullName);
        newWorker.put("branchName", branchName);
        newWorker.put("jobRole", jobRole.toString());
        newWorker.put("ID", newID);
        newWorker.put("accountNumber", newAccountNumber);
        newWorker.put("username", username);

        // Append new worker to workersArray
        workersArray.put(newWorker);

        try {
            // Write the updated JSON back to file
            JSONHandler.writeTo(JSONHandler.WorkersJsonFilePath, jsonData);

            System.out.println("Worker successfully added.");
            LogManager logManager = Main.logManager;
            logManager.WriteToFile("INFO: New worker registered with ID: " + newID + " by User: " + Main.loggedInUsersName);
        } catch (IOException e) {
            System.out.println(littleErrorMessage);
            System.err.println("File I/O Exception: " + e);
            return; // Go back to the menu
        }
    }
}