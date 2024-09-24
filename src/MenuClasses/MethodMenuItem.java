package MenuClasses;

import java.io.IOException;

public class MethodMenuItem extends MenuItem {

    //an interface that will hold a method that is assigned to this MenuClasses.MenuItem
    private IMethodObserver m_Observer;

    public MethodMenuItem(String i_Title)
    {
        super(i_Title);
    }

    public MethodMenuItem(String i_Title, IMethodObserver i_Function)
    {
        super(i_Title);
        m_Observer = i_Function;
    }

    //attach an MenuClasses.IMethodObserver to the MenuClasses.MenuItem
    public void AttachObserver(IMethodObserver i_Observer)
    {
        m_Observer = i_Observer;
    }

    //invokes the method via m_Observer and activates the Previous MenuClasses.MenuItem
    @Override
    public void ActivateMenuItem() throws IOException {
        m_Observer.Invoke();
        Previous.ActivateMenuItem();
    }
}
