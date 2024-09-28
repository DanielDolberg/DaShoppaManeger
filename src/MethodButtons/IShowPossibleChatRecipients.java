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
        Map<String, Long> users = ConnectionToMainServer.AskForListOfConnectedUsers();
        MainMenuItem pickAUserToChatWith = new MainMenuItem("pick A User To Chat With");

        for (String name : users.keySet())
        {
            if(name.equals(Main.loggedInUsersName)) //skip this user
                continue;

            String title = String.format("%s [network id: %d]", name, users.get(name));

            //MethodMenuItem callUpChat = new MethodMenuItem();

            pickAUserToChatWith.AddOption(new StartUpChatButton(title,users.get(name)));
        }


        pickAUserToChatWith.ActivateMenuItem();
        //ChatClient.StartChat();
    }


}
