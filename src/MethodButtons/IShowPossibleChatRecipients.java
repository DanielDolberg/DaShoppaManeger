package MethodButtons;

import MainClass.Main;
import MenuClasses.MainMenuItem;
import MenuClasses.IMethodObserver;
import MenuClasses.MethodMenuItem;
import ServerStuff.ConnectionToMainServer;

import java.io.IOException;
import java.util.Map;

public class IShowPossibleChatRecipients implements IMethodObserver {
    public void Invoke() throws IOException
    {
        if(!ConnectionToMainServer.isInChat)
        {
            Map<String, Long> users = ConnectionToMainServer.AskForListOfConnectedUsers();
            MainMenuItem pickAUserToChatWith = new MainMenuItem("pick A User To Chat With");

            for (String name : users.keySet())
            {
                if(name.equals(Main.loggedInUser.getFullName())) //skip this user
                    continue;

                String title = String.format("%s [network id: %d]", name, users.get(name));

                pickAUserToChatWith.AddOption(new StartUpChatButton(title,users.get(name)));
            }

            pickAUserToChatWith.ActivateMenuItem();
        }
        else {
            System.out.println("please exit existing chat before starting new one");
        }
    }


}
