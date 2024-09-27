package MenuClasses;

import java.io.IOException;
import Logs.LogManager;
import MainClass.Main;

import java.util.ArrayList;
import java.util.Scanner;

public class SubMenuItem extends MenuItem {

    //a list that contains all the selectable options in the menu
    private final ArrayList<MenuItem> Options;

    //a specific message for going back, will differ between different menu types
    protected String BackMessage;

    public SubMenuItem(String i_Title, ArrayList<MenuItem> i_Options) {
        super(i_Title);
        Options = i_Options;
        BackMessage = "Go Back";
    }

    public SubMenuItem(String i_Title) {
        super(i_Title);
        Options = new ArrayList<MenuItem>();
        BackMessage = "Go Back";
    }

    //displays the all the options in Options and allows the user to choose the one they want
    @Override
    public void ActivateMenuItem() throws IOException {
        System.out.println("==" + Title + "==");
        System.out.println("-------------------");

        //loops over every option and prints its title
        for (int i = 0; i < Options.size(); i++) {
            System.out.println(i + 1 + ". -> " + Options.get(i).Title);
        }

        System.out.println("0. -> " + BackMessage);
        System.out.println("Enter your request: (1 to " + Options.size() + " or press '0' to " + BackMessage + ").");

        int selection = getUserInput(); //requests the user to enter a number between 0 - Options.size()

        if (selection == 0) {
            if (Previous != null) //if there is a "previous" menu item, activate it. Do nothing otherwise
            {
                Previous.ActivateMenuItem();
            }
        } else {
            //write to a log file
            String title= Options.get(selection - 1).getTitle();
            Options.get(selection - 1).ActivateMenuItem(); //activate the Menu Item the user selected
        }
    }

    //requests the user to enter a number between 0 - Options.size()
    protected int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        int parsedInput = 0;


        //changed 03-slide20
        boolean isValidInput = false;
        while (!isValidInput) {
            try {
                userInput = scanner.next();
                parsedInput = Integer.parseInt(userInput);

                if (parsedInput > Options.size() || parsedInput < 0) {
                    throw new Exception("number out of bounds");
                }

                isValidInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input! Please try again!");
            }
        }

        return parsedInput;
    }

    //adds i_Option to the Options list and sets this as its "previous" MenuClasses.MenuItem
    //returns the newly created button
    public MenuItem AddOption(MenuItem i_Option) {
        Options.add(i_Option);
        i_Option.Previous = this;

        return i_Option;
    }
}
