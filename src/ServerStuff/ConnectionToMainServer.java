package ServerStuff;

import MainClass.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ConnectionToMainServer {
    private static final String SERVER_ADDRESS = "localhost"; // Change this to server IP if needed
    private static final int SERVER_PORT = 12345;
    private static final String name = Main.loggedInUsersName;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void ConnectToServer() throws IOException {
        try {
            // Connect to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // Setup input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("connection to server failed");
        }
    }

    public static JSONObject checkIfCredentialsAreTrue(String userName, String password) throws IOException
    {
        String outMessage =
                "{" +
                        "'type':'CHECK_IF_VALID_CRED' ," +
                        "'username': '" + userName + "'," +
                        "'password': '" + password + "'" +
                        "}";

        out.println(outMessage);

        System.out.println();
        String response = in.readLine();  // Get server response

        return new JSONObject(response);
    }

    public static String[] AskForConversationOfChat(long IDofRoom) throws IOException
    {
        LinkedList<String> convo = new LinkedList<>();

        String requestForConvo =
                "{" +
                        "'type' : 'REQUESTING_CONVERSATION'," +
                        "'roomID':" + IDofRoom +
                "}";

        String response = in.readLine();

        JSONArray chat = new JSONObject(response).getJSONArray("chat");

        for (int i=0; i < chat.length(); i++)
        {
            JSONObject message = chat.getJSONObject(i);

            convo.add(message.getString("text"));
        }

        return (String[])(convo.toArray());
    }

    public static Map<String, Long> AskForListOfConnectedUsers() throws IOException
    {
        Map<String, Long> userList = new HashMap<>();

        String requestUsers=
                "{" +
                        "'type' : 'REQUEST_LIST_OF_ACTIVE_USERS'" +
                        "}";

        out.println(requestUsers);

        String response = in.readLine();

        JSONArray chat = new JSONObject(response).getJSONArray("users");

        for (int i=0; i < chat.length(); i++)
        {
            JSONObject message = chat.getJSONObject(i);

            userList.put(message.getString("name"), message.getLong("ID"));
        }

        return userList;
    }

    public static void StartChatWith(long ID) throws IOException
    {
        LinkedList<String> request = new LinkedList<>();

        String requestJson =
                "{" +
                        "'type' : 'REQUEST_TO_START_OR_JOIN_CHAT'," +
                        "'requestedUser':"+ ID + "," +
                        "'requester' : " + Main.loggedInUser.getID() +
                        "}";

        out.println(requestJson);


        String response = in.readLine();

        JSONObject responseJson = new JSONObject(response);

        if(responseJson.getString("type").equals("REQUEST_TO_JOIN_CHAT_ACCEPTED"))
        {
            ChatClient.StartChat(responseJson.getLong("roomID"));
        }
    }

    public static void SentTextMessage(String Message)
    {
        out.println(Message);
    }
}
