import MenuClasses.MainMenuItem;
import MenuClasses.MethodMenuItem;
import MenuClasses.SubMenuItem;
import MethodButtons.testingButton;

public class Main {
    public static void main(String[] args) {
        setUpToTest();
    }



    public static void setUpToTest()
    {
        MainMenuItem main = new MainMenuItem("main");

        SubMenuItem hello = new MainMenuItem("hello");

        SubMenuItem shani = new MainMenuItem("sanih elha mig");
        MethodMenuItem sleep = new MethodMenuItem("sleep");
        sleep.AttachObserver(new testingButton());
        shani.AddOption(sleep);

        main.AddOption(hello);
        main.AddOption(shani);


        main.ActivateMenuItem();
    }
}