package ServerStuff;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import MainClass.Main;
import org.json.JSONObject;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change this to server IP if needed
    private static final int SERVER_PORT = 12345;
    private static final String name = Main.loggedInUsersName;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static final ChatClient chatClient = null;
    private static long IDofRoom;
    // GUI Components
    private static JFrame frame;
    private static JTextArea messageArea;
    private static JTextField messageField;
    private static JButton sendButton;

    private ChatClient() {
        setupGUI();
    }

    public static void StartChat(long RoomID)
    {
        new Thread(()->{
            IDofRoom = RoomID;
            setupGUI();
            askServerForConvo();
        }).start();
    }

    private static void setupGUI() {
        // Create frame
        frame = new JFrame(name + " ClientAndServerSideClasses");

        // Override the default close behavior
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Add WindowListener for custom closing behavior
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    ConnectionToMainServer.CloseConnectionOfChat(IDofRoom, Main.loggedInUser.getID()); // Custom method to close socket or cleanup
                    frame.dispose(); // Close the window and release resources
            }
        });

        frame.setSize(400, 400);

        // Create message area (where chat messages appear)
        messageArea = new JTextArea();
        messageArea.setEditable(false); // Users should not edit this area
        messageArea.setLineWrap(true);

        // Create a scroll pane for messageArea
        JScrollPane scrollPane = new JScrollPane(messageArea);

        // Create message field (where users type their message)
        messageField = new JTextField();
        messageField.addActionListener(e -> sendMessage());

        // Create send button
        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        // Layout the components
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER); // Chat area in the center
        frame.add(messageField, BorderLayout.SOUTH); // Input at the bottom
        frame.add(sendButton, BorderLayout.EAST); // Send button on the right

        frame.setVisible(true);
    }

    private static void askServerForConvo()
    {
        try {
            String[] fullConvo = ConnectionToMainServer.AskForConversationOfChat(IDofRoom);

            for (String message : fullConvo)
            {
                messageArea.append(message);
            }
        }
        catch (Exception ex)
        {
            messageArea.append("<<previou messages could not be loaded>> \n");
        }
    }

    private static void sendMessage() {

        String message = messageField.getText();

        if (message != null && !message.trim().isEmpty()) {
            ConnectionToMainServer.SentTextMessage(returnStringAsMessageJson(message)); // Send message to server
            messageField.setText(""); // Clear input field
        }
    }

    public static void AddMessage(String message)
    {
        messageArea.append(message + "\n");
    }

    private static void start() {
        try {
            // Connect to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // Setup input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Start a thread to read incoming messages from the server
            new Thread(() -> {
                try {
                    String incomingMessage;
                    while ((incomingMessage = in.readLine()) != null) {
                        messageArea.append(incomingMessage + "\n"); // Display message
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public static String returnStringAsMessageJson(String message)
    {
        Date currentTime = new Date();

        // Define the desired time format (HH:mm:ss)
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        // Format the current time
        String formattedTime = formatter.format(currentTime);

        String finalMessage =  "{" +
                "'type':'CHAT_MESSAGE'," +
                "'roomID':" + IDofRoom +"," +
                "'name':" + '"' + name + '"' + "," +
                "'message':" + '"' + message + '"' + "," +
                "time_sent:" + '"' + formattedTime + '"' +
                "}";

        return finalMessage;

    }
}
