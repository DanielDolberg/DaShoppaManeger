import MenuClasses.MainMenuItem;
import MenuClasses.MethodMenuItem;
import MenuClasses.SubMenuItem;
import MethodButtons.testingButton;

public class Main {
    public static void main(String[] args) {
        setUpShop();
    }


    public static void setUpShop()
    {
        MainMenuItem main = new MainMenuItem("DeShooppa Manager Main Menu");

        // Branch Info - option
        MethodMenuItem branchInfo = new MethodMenuItem("Print Branch Info");
        branchInfo.AttachObserver(new testingButton()); //!!!change!!!
        main.AddOption(branchInfo);

        // Manage Product Inventory - sub
        SubMenuItem productInventory = new SubMenuItem("Manage Product Inventory");
        SubMenuItem productInventory_1 = new SubMenuItem("view all stock in this branch");
        productInventory.AddOption(productInventory_1);
        SubMenuItem productInventory_2 = new SubMenuItem("perform a purchase on behalf of a customer");
        productInventory.AddOption(productInventory_2);
        SubMenuItem productInventory_3 = new SubMenuItem("view all stock in all branches");
        productInventory.AddOption(productInventory_3);
        main.AddOption(productInventory);

        // Manage Workers (admin only) - sub
        SubMenuItem manageWorkers = new SubMenuItem("Manage Workers (Admin Only)");
        MethodMenuItem viewAllAccounts = new MethodMenuItem("print/view All Accounts");
        viewAllAccounts.AttachObserver(new testingButton()); //!!!change!!!
        manageWorkers.AddOption(viewAllAccounts);
        MethodMenuItem registerNewAccount = new MethodMenuItem("Register New Account");
        registerNewAccount.AttachObserver(new testingButton()); //!!!change!!!
        manageWorkers.AddOption(registerNewAccount);
        MethodMenuItem updateAnAccount = new MethodMenuItem("Update An Account");
        updateAnAccount.AttachObserver(new testingButton()); //!!!change!!!
        manageWorkers.AddOption(updateAnAccount);
        main.AddOption(manageWorkers);

        // Manage Customers - sub
        SubMenuItem manageCustomers = new SubMenuItem("Manage Customers");
        SubMenuItem manageCustomers_1 = new SubMenuItem("add new customer");
        manageCustomers.AddOption(manageCustomers_1);
        SubMenuItem manageCustomers_2 = new SubMenuItem("view customer");
        manageCustomers.AddOption(manageCustomers_2);
        SubMenuItem manageCustomers_3 = new SubMenuItem("update customer details");
        manageCustomers.AddOption(manageCustomers_3);
        main.AddOption(manageCustomers);

        // Manage Sales Analytics Reports - sub
        SubMenuItem manageReports = new SubMenuItem("Manage Sales Analytics Reports (all branches)");
        SubMenuItem manageReports_1 = new SubMenuItem("view sales for branch");
        manageReports.AddOption(manageReports_1);
        SubMenuItem manageReports_2 = new SubMenuItem("view sales by product");
        manageReports.AddOption(manageReports_2);
        SubMenuItem manageReports_3 = new SubMenuItem("view sales by category");
        manageReports.AddOption(manageReports_3);
        main.AddOption(manageReports);

        // Manage Chat - sub
        SubMenuItem manageChat = new SubMenuItem("Manage Chat");
        SubMenuItem manageChat_1 = new SubMenuItem("start chat with another employee");
        manageChat.AddOption(manageChat_1);
        SubMenuItem manageChat_2 = new SubMenuItem("if shift manager join existing chat");
        manageChat.AddOption(manageChat_2);
        main.AddOption(manageChat);

        // Manage Logs - sub
        SubMenuItem manageLogs = new SubMenuItem("Manage Logs");
        SubMenuItem manageLogs_1 = new SubMenuItem("view logs by date"); //!!! change to option!!!
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