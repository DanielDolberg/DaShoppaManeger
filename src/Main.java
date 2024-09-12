import MenuClasses.IMethodObserver;
import MenuClasses.MainMenuItem;
import MenuClasses.MethodMenuItem;
import MenuClasses.SubMenuItem;
import MethodButtons.testingButton;

public class Main {
    public static void main(String[] args) {
        setUpToTest();
    }


    public static void setUpShop()
    {

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