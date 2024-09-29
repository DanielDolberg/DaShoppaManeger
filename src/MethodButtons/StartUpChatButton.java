package MethodButtons;

import MenuClasses.MethodMenuItem;
import ServerStuff.ConnectionToMainServer;

import java.io.IOException;

public class StartUpChatButton extends MethodMenuItem {
    private long IDofRecipiant;
    public StartUpChatButton(String name, long ID)
    {
        super(name);
        IDofRecipiant = ID;
    }

    @Override
    public void ActivateMenuItem() throws IOException
    {
        if(!ConnectionToMainServer.isInChat) {
            ConnectionToMainServer.StartChatWith(IDofRecipiant);
        }
        else
        {
            System.out.println("we detected user is already in chat, please close before attempting new chat");
        }
        //Previous.ActivateMenuItem();
    }
}
