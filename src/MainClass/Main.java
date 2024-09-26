package MainClass;

import MethodButtons.*;
import Utilities.JSONHandler;
import MenuClasses.MainMenuItem;
import MenuClasses.MethodMenuItem;
import MenuClasses.SubMenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class Main {
// hope it works
    public static boolean isUserLoggedIn = false;
    public static boolean isAdmin = false;
    public static boolean isShiftManager = false;
    public static String workerBranch = "";
    public static JSONObject theWorkerThatIsLoggedIn;

    private static final String bigErrorMessage = "A big error happened, please contact support";
    public static final String littleErrorMessage = "Met an error, couldn't do task, returning to menu. please contact support";

    public static void main(String[] args) {
        try {
            // login screen
            login();

            if (isUserLoggedIn) {
                // log in succeeded, open main menu
                setUpShop();
            } else {
                System.out.println("You were locked out of your account, please try again later");
            }

        } catch (JSONException e) {
            System.out.println(bigErrorMessage);
            // Raised when trying to access a field or element that doesn't exist
            System.out.println("JSONException: " + e);
            System.err.println("JSONException: " + e);
        } catch (NullPointerException e) { //
            System.out.println(bigErrorMessage);
            // If the structure in the JSON file doesn't match what the code expects, accessing a non-existent object
            System.out.println("NullPointerException: " + e);
            System.err.println("NullPointerException: " + e);
        } catch (ClassCastException e) { //
            System.out.println(bigErrorMessage);
            // Occurs when you attempt to cast a JSON value to an incompatible type.
            // For example, trying to retrieve a string from a JSON field that holds an integer.
            System.out.println("ClassCastException: " + e);
            System.err.println("ClassCastException: " + e);
        } catch (IOException e) {
            System.out.println(bigErrorMessage);
            // If the JSON file path does not exist or if there’s a problem reading the file (e.g., incorrect permissions).
            System.out.println("File I/O Exception: " + e);
            System.err.println("File I/O Exception: " + e);
        } catch (Exception e) {
            System.out.println(bigErrorMessage);
            // any Exception we didn't think of
            System.out.println("Unexpected Exception: " + e);
            System.err.println("Unexpected Exception: " + e);
        }
    }

    public static void login()  throws IOException, JSONException {
        JSONArray workersArray = null;

        // Load the JSON data from the file
        JSONObject jsonData = JSONHandler.readFrom(JSONHandler.WorkersJsonFilePath);
        workersArray = jsonData.getJSONArray("workers");

        isUserLoggedIn = false;
        int numOfTries = 0;

        Scanner scanner = new Scanner(System.in);

        while (!isUserLoggedIn && numOfTries < 3) {
            System.out.println("Please enter username: ");
            String userName = scanner.nextLine();
            System.out.println("Please enter password: ");
            String password = scanner.nextLine();

            numOfTries++;

            // Check users (workers/admins)
            for (int i = 0; i < workersArray.length(); i++) {
                JSONObject user = workersArray.getJSONObject(i);
                String userNameInJson = user.getString("username");
                String passwordInJson = user.getString("password");

                if (userName.equals(userNameInJson) && password.equals(passwordInJson)) {
                    isUserLoggedIn = true;
                    String jobRole = user.getString("jobRole");
                    if (jobRole.equalsIgnoreCase("ShiftManager")) {
                        isShiftManager = true;
                    }

                    theWorkerThatIsLoggedIn = user;
                    workerBranch = user.getString("branchName");

                    if (jobRole.equalsIgnoreCase("Admin")) {
                        isAdmin = true;
                        System.out.println("Welcome " + userNameInJson + ", you are logged in as an admin.");
                    } else {
                        isAdmin = false;
                        System.out.println("Welcome " + userNameInJson + ", you are logged in as a worker.");
                    }
                    break;
                }
            }

            if (!isUserLoggedIn && numOfTries < 3) {
                System.out.println("Invalid credentials, please try again.");
            }
        }

        if (!isUserLoggedIn) {
            System.out.println("You were locked out of your account, please try again later.");
        }
    }

    public static void setUpShop() throws IOException {
        MainMenuItem mainMenu = new MainMenuItem("DeShooppa Manager MainClass.Main Menu");

        if (isAdmin) {
            // Manage Workers (admin only) - sub
            SubMenuItem subMenuManageWorkers = new SubMenuItem("Manage Workers (Admin Only)");

            MethodMenuItem methodViewAllAccounts = new MethodMenuItem("View All Accounts"); //MethodMenuItem //DONE
            methodViewAllAccounts.AttachObserver(new ViewAllAccountsButton());
            subMenuManageWorkers.AddOption(methodViewAllAccounts);

            MethodMenuItem methodRegisterNewAccount = new MethodMenuItem("Register New Account"); //MethodMenuItem //DONE
            methodRegisterNewAccount.AttachObserver(new AddNewWorkerButton()); //!!!change!!!
            subMenuManageWorkers.AddOption(methodRegisterNewAccount);

            MethodMenuItem methodUpdateAnAccount = new MethodMenuItem("Update An Account"); //MethodMenuItem
            methodUpdateAnAccount.AttachObserver(new testingButton_Delete_later()); //!!!change!!!
            subMenuManageWorkers.AddOption(methodUpdateAnAccount);

            mainMenu.AddOption(subMenuManageWorkers);
        }

        // Manage Product Inventory - sub
        SubMenuItem subMenuProductInventory = new SubMenuItem("Manage Stock");

        if (!isAdmin) {
            MethodMenuItem methodViewBranchStock = new MethodMenuItem("View All Stock in This Branch"); //MethodMenuItem //DONE
            methodViewBranchStock.AttachObserver(new ViewAllStockInThisBranchButton());
            subMenuProductInventory.AddOption(methodViewBranchStock);
        }

        MethodMenuItem methodPerformPurchase = new MethodMenuItem("Perform a Purchase on behalf of a Customer"); //MethodMenuItem
        methodPerformPurchase.AttachObserver(new PurchaseMenu());
        subMenuProductInventory.AddOption(methodPerformPurchase);

        MethodMenuItem methodViewAllStock = new MethodMenuItem("View Product Stock in All Branches"); //MethodMenuItem //DONE
        methodViewAllStock.AttachObserver(new ViewStockInAllBranchesButton());
        subMenuProductInventory.AddOption(methodViewAllStock);

        mainMenu.AddOption(subMenuProductInventory);

        // Manage Customers - sub
        SubMenuItem subMenuManageCustomers = new SubMenuItem("Manage Customers");

        MethodMenuItem methodAddNewCustomer = new MethodMenuItem("Add New Customer"); //MethodMenuItem
        methodAddNewCustomer.AttachObserver(new testingButton_Delete_later()); //!!!change!!!
        subMenuManageCustomers.AddOption(methodAddNewCustomer);

        MethodMenuItem methodViewCustomerDetails = new MethodMenuItem("View Customer's Details"); //MethodMenuItem
        methodViewCustomerDetails.AttachObserver(new testingButton_Delete_later()); //!!!change!!! //methodViewAllCustomers might answer this
        subMenuManageCustomers.AddOption(methodViewCustomerDetails);

        MethodMenuItem methodViewAllCustomers = new MethodMenuItem("View All The Customers"); //MethodMenuItem  //DONE
        methodViewAllCustomers.AttachObserver(new ViewAllCustomersButton());
        subMenuManageCustomers.AddOption(methodViewAllCustomers);

        mainMenu.AddOption(subMenuManageCustomers);

        // Manage Sales Analytics Reports - sub
        SubMenuItem subMenuManageReports = new SubMenuItem("View Sales Statistics (all branches)");

        MethodMenuItem methodViewSalesBranch = new MethodMenuItem("View Sales for Branch"); //MethodMenuItem
        methodViewSalesBranch.AttachObserver(new testingButton_Delete_later()); //!!!change!!!
        subMenuManageReports.AddOption(methodViewSalesBranch);

        MethodMenuItem methodViewSalesByProduct = new MethodMenuItem("View Sales by Product"); //MethodMenuItem
        methodViewSalesByProduct.AttachObserver(new testingButton_Delete_later()); //!!!change!!!
        subMenuManageReports.AddOption(methodViewSalesByProduct);

        MethodMenuItem methodViewSalesByCategory = new MethodMenuItem("View Sales by Category"); //MethodMenuItem
        methodViewSalesByCategory.AttachObserver(new testingButton_Delete_later()); //!!!change!!!
        subMenuManageReports.AddOption(methodViewSalesByCategory);

        mainMenu.AddOption(subMenuManageReports);

        // Manage Chat - sub
        SubMenuItem subMenuManageChat = new SubMenuItem("Open Chat");

        SubMenuItem methodStartChat = new SubMenuItem("Start Chat with Another Employee"); //!!!MethodMenuItem or sub?
        subMenuManageChat.AddOption(methodStartChat);
        if (isShiftManager || isAdmin) {
            SubMenuItem methodManagerJoinsChat = new SubMenuItem("Join Existing Chat (If Shift Manager)"); //!!!MethodMenuItem or sub?
            subMenuManageChat.AddOption(methodManagerJoinsChat);
        }

        mainMenu.AddOption(subMenuManageChat);

        // Manage Logs - sub
        SubMenuItem subMenuManageLogs = new SubMenuItem("Manage Logs");

        MethodMenuItem methodViewLogs = new MethodMenuItem("view Logs by Date"); //MethodMenuItem
        methodViewLogs.AttachObserver(new testingButton_Delete_later()); //!!!change!!!
        subMenuManageLogs.AddOption(methodViewLogs);

        mainMenu.AddOption(subMenuManageLogs);

        mainMenu.ActivateMenuItem();
    }
}