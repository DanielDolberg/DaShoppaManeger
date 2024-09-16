import Utilities.JSONHandler;
import MenuClasses.MainMenuItem;
import MenuClasses.MethodMenuItem;
import MenuClasses.SubMenuItem;
import MethodButtons.ViewAllCustomersButton;
import MethodButtons.testingButton;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class Main {

    static boolean isUserLoggedIn = false;
    static boolean isAdmin = false;
    static boolean isShiftManager = false;

    public static void main(String[] args) {
        // login screen
        login();

        if(isUserLoggedIn)
        {
            // log in succeeded, open main menu
            setUpShop();
        }
        else
        {
            System.out.println("You were locked out of your account, please try again later");
        }
    }

    // login screen
    public static void login() {
        JSONArray workersArray = null;
        JSONArray adminsArray = null;

        JSONObject jsonData = JSONHandler.readFrom("J&sons/LoginCredentials.json");
        workersArray = jsonData.getJSONArray("workers");
        adminsArray = jsonData.getJSONArray("admins");
        /*
        try {
            // Specify the path to the JSON file
            String filePath = "J&sons/LoginCredentials.json";

            // Read the file content into a String
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the content into a JSONObject
            JSONObject jsonData = new JSONObject(content);
            workersArray = jsonData.getJSONArray("workers");
            adminsArray = jsonData.getJSONArray("admins");

        } catch (IOException e) {
            System.err.println("IO Exception!");
        }
        */

        isUserLoggedIn = false;
        int numOfTries = 0;

        Scanner scanner = new Scanner(System.in);

        while (!isUserLoggedIn && numOfTries < 3) {
            String userName = "";
            String password = "";

            System.out.println("Please enter username: ");
            userName = scanner.nextLine();
            System.out.println("Please enter password: ");
            password = scanner.nextLine();

            numOfTries++;

            // Check workers
            for (int i = 0; i < workersArray.length(); i++) {
                JSONObject worker = workersArray.getJSONObject(i);
                String UserNameInJson = worker.getString("username");
                String passwordInJson = worker.getString("password");

                if (userName.equals(UserNameInJson) && password.equals(passwordInJson)) {
                    isUserLoggedIn = true;
                    isAdmin = false;  // It's a worker
                    System.out.println("Welcome " + UserNameInJson + ", you are logged in as a worker.");
                    break;
                }
            }

            // Check admins
            if (!isUserLoggedIn) {
                for (int i = 0; i < adminsArray.length(); i++) {
                    JSONObject admin = adminsArray.getJSONObject(i);
                    String adminUserNameInJson = admin.getString("username");
                    String adminPasswordInJson = admin.getString("password");

                    if (userName.equals(adminUserNameInJson) && password.equals(adminPasswordInJson)) {
                        isUserLoggedIn = true;
                        isAdmin = true;  // It's an admin
                        System.out.println("Welcome " + adminUserNameInJson + ", you are logged in as an admin.");
                        break;
                    }
                }
            }

            if (!isUserLoggedIn && numOfTries < 3) {
                System.out.println("Invalid credentials, please try again.");
            }
        }

        if (isUserLoggedIn) {
            if (isAdmin) {
                System.out.println("Admin access granted.");
            } else {
                System.out.println("Worker access granted.");
            }
        } else {
            System.out.println("You were locked out of your account, please try again later.");
        }
    }


    public static void setUpShop()
    {
        MainMenuItem main = new MainMenuItem("DeShooppa Manager Main Menu");

        if (isAdmin) {
            // Manage Workers (admin only) - sub
            SubMenuItem manageWorkers = new SubMenuItem("Manage Workers (Admin Only)");
            MethodMenuItem viewAllAccounts = new MethodMenuItem("View All Accounts"); //MethodMenuItem
            viewAllAccounts.AttachObserver(new testingButton()); //!!!change!!!
            manageWorkers.AddOption(viewAllAccounts);
            MethodMenuItem registerNewAccount = new MethodMenuItem("Register New Account"); //MethodMenuItem
            registerNewAccount.AttachObserver(new testingButton()); //!!!change!!!
            manageWorkers.AddOption(registerNewAccount);
            MethodMenuItem updateAnAccount = new MethodMenuItem("Update An Account"); //MethodMenuItem
            updateAnAccount.AttachObserver(new testingButton()); //!!!change!!!
            manageWorkers.AddOption(updateAnAccount);
            main.AddOption(manageWorkers);
        }

        // Manage Product Inventory - sub
        SubMenuItem productInventory = new SubMenuItem("Manage Stock");
        SubMenuItem productInventory_1 = new SubMenuItem("View All Stock in This Branch"); //!!!MethodMenuItem or sub ?
        productInventory.AddOption(productInventory_1);
        SubMenuItem productInventory_2 = new SubMenuItem("Preform a Purchase on behalf of a Customer"); //!!!MethodMenuItem or sub ?
        productInventory.AddOption(productInventory_2);
        SubMenuItem productInventory_3 = new SubMenuItem("View Product Stock in All Branches"); //!!!MethodMenuItem or sub ?
        productInventory.AddOption(productInventory_3);
        main.AddOption(productInventory);

        // Manage Customers - sub
        SubMenuItem manageCustomers = new SubMenuItem("Manage Customers");
        MethodMenuItem manageCustomers_1 = new MethodMenuItem("Add New Customer"); //MethodMenuItem
        manageCustomers_1.AttachObserver(new testingButton()); //!!!change!!!
        manageCustomers.AddOption(manageCustomers_1);
        MethodMenuItem manageCustomers_2 = new MethodMenuItem("View Customer's Details"); //MethodMenuItem
        manageCustomers_2.AttachObserver(new testingButton()); //!!!change!!! //manageCustomers_3 might answer this
        manageCustomers.AddOption(manageCustomers_2);
        MethodMenuItem manageCustomers_3 = new MethodMenuItem("View All The Customers"); //MethodMenuItem
        manageCustomers_3.AttachObserver(new ViewAllCustomersButton());
        manageCustomers.AddOption(manageCustomers_3);
        main.AddOption(manageCustomers);

        // Manage Sales Analytics Reports - sub
        SubMenuItem manageReports = new SubMenuItem("View Sales Statistics (all branches)");
        MethodMenuItem manageReports_1 = new MethodMenuItem("View Sales for Branch"); //MethodMenuItem
        manageReports_1.AttachObserver(new testingButton()); //!!!change!!!
        manageReports.AddOption(manageReports_1);
        MethodMenuItem manageReports_2 = new MethodMenuItem("View Sales by Product"); //MethodMenuItem
        manageReports_2.AttachObserver(new testingButton()); //!!!change!!!
        manageReports.AddOption(manageReports_2);
        MethodMenuItem manageReports_3 = new MethodMenuItem("View Sales by Category"); //MethodMenuItem
        manageReports_3.AttachObserver(new testingButton()); //!!!change!!!
        manageReports.AddOption(manageReports_3);
        main.AddOption(manageReports);

        // Manage Chat - sub
        SubMenuItem manageChat = new SubMenuItem("Open Chat");
        SubMenuItem manageChat_1 = new SubMenuItem("Start Chat with Another Employee"); //!!!MethodMenuItem or sub?
        manageChat.AddOption(manageChat_1);
        if (isShiftManager || isAdmin) {
            SubMenuItem manageChat_2 = new SubMenuItem("Join Existing Chat (If Shift Manager)"); //!!!MethodMenuItem or sub?
            manageChat.AddOption(manageChat_2);
        }
        main.AddOption(manageChat);

        // Manage Logs - sub
        SubMenuItem manageLogs = new SubMenuItem("Manage Logs");
        MethodMenuItem manageLogs_1 = new MethodMenuItem("view Logs by Date"); //MethodMenuItem
        manageLogs_1.AttachObserver(new testingButton()); //!!!change!!!
        manageLogs.AddOption(manageLogs_1);
        main.AddOption(manageLogs);

        main.ActivateMenuItem();
    }

    // !!!! DELETE LATER !!!!!
    public static void setUpToTest()
    {
        MainMenuItem main = new MainMenuItem("main");
        SubMenuItem shani = new MainMenuItem("shanig a mil");
        MethodMenuItem shaniSleep = new MethodMenuItem("sleep");
        shaniSleep.AttachObserver(new testingButton());
        shani.AddOption(shaniSleep);
        SubMenuItem Aiden = new MainMenuItem("add an kaminslky");
        SubMenuItem khen = new MainMenuItem("khen moasis");
        khen.AddOption(new SubMenuItem("ariel girgani"));
        SubMenuItem dan = new MainMenuItem("dan iel dabegth");
        main.AddOption(shani);
        main.AddOption(Aiden);
        main.AddOption(khen);
        main.AddOption(dan);
        main.AddOption(shaniSleep);
        main.ActivateMenuItem();
    }
}