package Chat;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import MainClass.Main;


public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change this to server IP if needed
    private static final int SERVER_PORT = 12345;
    private static final String name = Main.loggedInUsersName;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static ChatClient chatClient = null;
    // GUI Components
    private static JFrame frame;
    private static JTextArea messageArea;
    private static JTextField messageField;
    private static JButton sendButton;

    private ChatClient() {
        setupGUI();
    }

    public static void StartChat()
    {
        setupGUI();
        start();
    }

    private static void setupGUI() {
        // Create frame
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    private static void sendMessage() {
        String namePreFix = "{" + name +"}";
        String message = messageField.getText();
        if (message != null && !message.trim().isEmpty()) {
            out.println(namePreFix + message); // Send message to server
            messageField.setText(""); // Clear input field
        }
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

}
