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


    private static JSONObject incomingMessage = null; //the string that will hold all of the incoming messages
    private static final Object MuteEx = new Object();

    public static void ConnectToServer() throws IOException {
        try {
            // Connect to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // Setup input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            Thread incomingListener = new Thread(() -> {
                try {
                    String incomingString;
                    while ((incomingString = in.readLine()) != null) {
                        JSONObject inToJson = new JSONObject(incomingString);
                        System.out.println(inToJson);
                        synchronized (MuteEx) {
                            switch (inToJson.getString("type")) {
                                case "RECEIVED_CHAT_MESSAGE":
                                    ReceiveTextMessage(inToJson);
                                    break;
                                default:
                                    incomingMessage = inToJson;
                                    MuteEx.notify();  // Notify waiting threads that a new message has arrived
                                    break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            incomingListener.start();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("connection to server failed");
        }
    }

    public static JSONObject checkIfCredentialsAreTrue(String userName, String password) throws IOException {
        String outMessage =
                "{" +
                        "'type':'CHECK_IF_VALID_CRED' ," +
                        "'username': '" + userName + "'," +
                        "'password': '" + password + "'" +
                        "}";

        out.println(outMessage);

        JSONObject response = waitForResponse();  // Get server response

        return (response);
    }

    public static String[] AskForConversationOfChat(long IDofRoom) throws IOException {
        LinkedList<String> convo = new LinkedList<>();

        String requestForConvo =
                "{" +
                        "'type' : 'REQUESTING_CONVERSATION'," +
                        "'roomID':" + IDofRoom +
                        "}";

        JSONObject response = waitForResponse();

        JSONArray chat = (response).getJSONArray("chat");

        for (int i = 0; i < chat.length(); i++) {
            JSONObject message = chat.getJSONObject(i);

            convo.add(message.getString("text"));
        }

        return (String[]) (convo.toArray());
    }

    public static Map<String, Long> AskForListOfConnectedUsers() throws IOException {
        Map<String, Long> userList = new HashMap<>();

        String requestUsers =
                "{" +
                        "'type' : 'REQUEST_LIST_OF_ACTIVE_USERS'" +
                        "}";

        out.println(requestUsers);

        JSONObject response = waitForResponse();

        JSONArray chat = response.getJSONArray("users");

        for (int i = 0; i < chat.length(); i++) {
            JSONObject message = chat.getJSONObject(i);

            userList.put(message.getString("name"), message.getLong("ID"));
        }

        return userList;
    }

    public static void StartChatWith(long ID) throws IOException {
        LinkedList<String> request = new LinkedList<>();

        String requestJson =
                "{" +
                        "'type' : 'REQUEST_TO_START_OR_JOIN_CHAT'," +
                        "'requestedUser':" + ID + "," +
                        "'requester' : " + Main.loggedInUser.getID() +
                        "}";

        out.println(requestJson);


        JSONObject response = waitForResponse();

        if (response.getString("type").equals("REQUEST_TO_JOIN_CHAT_ACCEPTED")) {
            ChatClient.StartChat(response.getLong("roomID"));
        }
    }

    public static void ReceiveTextMessage(JSONObject message) {

        ChatClient.AddMessage(message.getString("text"));
    }

    public static void SentTextMessage(String Message) {
        out.println(Message);
    }


    //waits until incomingMessage has a value and returns it(nullyfies it back afterwatrds)
    public static JSONObject waitForResponse() {
        synchronized (MuteEx) {
            while (incomingMessage == null) {
                try {
                    MuteEx.wait();  // Wait until notified
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
            }
            JSONObject tmp = incomingMessage;
            incomingMessage = null; // Nullify incoming message after reading
            return tmp;
        }
    }
}
