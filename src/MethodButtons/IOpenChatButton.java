package MethodButtons;

import Chat.ChatClient;
import MenuClasses.IMethodObserver;

public class IOpenChatButton implements IMethodObserver {
    public void Invoke()
    {
        ChatClient.StartChat();
    }
}
