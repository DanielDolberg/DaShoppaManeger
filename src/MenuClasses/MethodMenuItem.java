package MenuClasses;

public class MethodMenuItem extends MenuItem {
    //an interface that will hold a method that is assigned to this MenuClasses.MenuItem
    private IMethodObserver m_Observer;

    public MethodMenuItem(String i_Title, MenuItem i_Previous)
    {
        super(i_Title, i_Previous);
    }

    public MethodMenuItem(String i_Title)
    {
        super(i_Title);
    }

    //attach an MenuClasses.IMethodObserver to the MenuClasses.MenuItem
    public void AttachObserver(IMethodObserver i_Observer) {
        m_Observer = i_Observer;
    }

    //invokes the method via m_Observer and activates the Previous MenuClasses.MenuItem
    @Override
    public void ActivateMenuItem() {
        m_Observer.Invoke();
        Previous.ActivateMenuItem();
    }
}
