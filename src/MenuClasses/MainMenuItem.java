package MenuClasses;

import java.util.ArrayList;

public class MainMenuItem extends SubMenuItem {
    public MainMenuItem(String i_Title, MenuItem i_Previous, ArrayList<MenuItem> i_Options) {
        super(i_Title, i_Previous, i_Options);
        Previous = null; //null because this is the first menu that the user will see
        BackMessage = "Exit"; //this is set to "Exit" because when the user exits the main menu, they exit the whole program
    }

    public MainMenuItem(String i_Title) {
        super(i_Title);
        Previous = null; //null because this is the first menu that the user will see
        BackMessage = "Exit"; //this is set to "Exit" because when the user exits the main menu, they exit the whole program
    }
}
