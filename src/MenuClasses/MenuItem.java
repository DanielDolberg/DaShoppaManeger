package MenuClasses;

public abstract class MenuItem {

    //every MenuClasses.MenuItem will have a title that will be displayed on screen.
    final protected String Title;
    //every MenuClasses.MenuItem(except MenuClasses.MainMenuItem) will have a father MenuClasses.MenuItem that links to it called "Previous"
    public MenuItem Previous;

    public MenuItem(String i_Title)
    {
        Title = i_Title;
        Previous = null;
    }

    public MenuItem()
    {
        Title = "";
        Previous = null;
    }

    public String getTitle() {
        return Title;
    }

    public MenuItem getPrevious() {
        return Previous;
    }

    public void setPrevious(MenuItem previous) {
        Previous = previous;
    }

    public void ActivateMenuItem()
    {

    }
}
