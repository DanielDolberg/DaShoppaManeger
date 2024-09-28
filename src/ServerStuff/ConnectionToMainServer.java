package ServerStuff;

import MainClass.Main;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class ConnectionToMainServer {
    private static final String SERVER_ADDRESS = "localhost"; // Change this to server IP if needed
    private static final int SERVER_PORT = 12345;
    private static final String name = Main.loggedInUsersName;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static final ChatClient chatClient = null;

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



}
