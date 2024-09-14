import MenuClasses.MainMenuItem;
import MenuClasses.MethodMenuItem;
import MenuClasses.SubMenuItem;
import MethodButtons.testingButton;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        login();
        setUpShop();
    }

    public static void login()
    {
        // Specify the path to the JSON file
        String filePath = "J&sons/Workers.json";

        try {
            // Read the file content into a String
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the content into a JSONObject
            JSONObject jsonObject = new JSONObject(content);

            // Use the JSONObject
            System.out.println(jsonObject.toString(4)); // Pretty print with 4-space indentation

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void setUpShop()
    {
        MainMenuItem main = new MainMenuItem("DeShooppa Manager Main Menu");

        // Branch Info - option
        MethodMenuItem branchInfo = new MethodMenuItem("Print Branch Info");
        branchInfo.AttachObserver(new testingButton()); //!!!change!!!
        main.AddOption(branchInfo);

        // Manage Product Inventory - sub
        SubMenuItem productInventory = new SubMenuItem("Manage Stock");
        SubMenuItem productInventory_1 = new SubMenuItem("View All Stock in This Branch"); //!!!option or sub ?
        productInventory.AddOption(productInventory_1);
        SubMenuItem productInventory_2 = new SubMenuItem("Preform a Purchase on behalf of a Customer"); //!!!option or sub ?
        productInventory.AddOption(productInventory_2);
        SubMenuItem productInventory_3 = new SubMenuItem("View Product Stock in All Branches"); //!!!option or sub ?
        productInventory.AddOption(productInventory_3);
        main.AddOption(productInventory);

        // Manage Workers (admin only) - sub
        SubMenuItem manageWorkers = new SubMenuItem("Manage Workers (Admin Only)");
        MethodMenuItem viewAllAccounts = new MethodMenuItem("View All Accounts"); //option
        viewAllAccounts.AttachObserver(new testingButton()); //!!!change!!!
        manageWorkers.AddOption(viewAllAccounts);
        MethodMenuItem registerNewAccount = new MethodMenuItem("Register New Account"); //option
        registerNewAccount.AttachObserver(new testingButton()); //!!!change!!!
        manageWorkers.AddOption(registerNewAccount);
        MethodMenuItem updateAnAccount = new MethodMenuItem("Update An Account"); //option
        updateAnAccount.AttachObserver(new testingButton()); //!!!change!!!
        manageWorkers.AddOption(updateAnAccount);
        main.AddOption(manageWorkers);

        // Manage Customers - sub
        SubMenuItem manageCustomers = new SubMenuItem("Manage Customers");
        MethodMenuItem manageCustomers_1 = new MethodMenuItem("Add New Customer"); //option
        manageCustomers_1.AttachObserver(new testingButton()); //!!!change!!!
        manageCustomers.AddOption(manageCustomers_1);
        MethodMenuItem manageCustomers_2 = new MethodMenuItem("View Customer's Details"); //option
        manageCustomers_2.AttachObserver(new testingButton()); //!!!change!!!
        manageCustomers.AddOption(manageCustomers_2);
        MethodMenuItem manageCustomers_3 = new MethodMenuItem("Update Customer Details"); //option
        manageCustomers_3.AttachObserver(new testingButton()); //!!!change!!!
        manageCustomers.AddOption(manageCustomers_3);
        main.AddOption(manageCustomers);

        // Manage Sales Analytics Reports - sub
        SubMenuItem manageReports = new SubMenuItem("View Sales Statisttics (all branches)");
        MethodMenuItem manageReports_1 = new MethodMenuItem("View Sales for Branch"); //option
        manageReports_1.AttachObserver(new testingButton()); //!!!change!!!
        manageReports.AddOption(manageReports_1);
        MethodMenuItem manageReports_2 = new MethodMenuItem("View Sales by Product"); //option
        manageReports_2.AttachObserver(new testingButton()); //!!!change!!!
        manageReports.AddOption(manageReports_2);
        MethodMenuItem manageReports_3 = new MethodMenuItem("View Sales by Category"); //option
        manageReports_3.AttachObserver(new testingButton()); //!!!change!!!
        manageReports.AddOption(manageReports_3);
        main.AddOption(manageReports);

        // Manage Chat - sub
        SubMenuItem manageChat = new SubMenuItem("Manage Chats");
        SubMenuItem manageChat_1 = new SubMenuItem("Start Chat with Another Employee"); //!!!option or sub?
        manageChat.AddOption(manageChat_1);
        SubMenuItem manageChat_2 = new SubMenuItem("Join Existing Chat (If Shift Manager)"); //!!!option or sub?
        manageChat.AddOption(manageChat_2);
        main.AddOption(manageChat);

        // Manage Logs - sub
        SubMenuItem manageLogs = new SubMenuItem("Manage Logs");
        MethodMenuItem manageLogs_1 = new MethodMenuItem("view Logs by Date"); // option
        manageLogs_1.AttachObserver(new testingButton()); //!!!change!!!
        manageLogs.AddOption(manageLogs_1);
        main.AddOption(manageLogs);

        main.ActivateMenuItem();
    }

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