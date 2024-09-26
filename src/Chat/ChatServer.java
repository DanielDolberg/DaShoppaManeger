package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import MainClass.Main;

public class ChatServer {
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Chat server started...");
        ServerSocket serverSocket = new ServerSocket(12345); // Use a specific port for connection
        while (true) {
            new ClientHandler(serverSocket.accept()).start(); // Accept incoming client connections
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientWriters) {
                    clientWriters.add(out); // Add this client's writer to the set
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    synchronized (clientWriters) {
                        Date currentTime = new Date();

                        // Define the desired time format (HH:mm:ss)
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

                        // Format the current time
                        String formattedTime = formatter.format(currentTime);
                        String nameOfSender = ExtractNameFromMessage(message);
                        String messageWithOutName = ReturnStringWithoutName(message);


                        for (PrintWriter writer : clientWriters) {
                            writer.println("["+formattedTime+": " + nameOfSender + "]: " +messageWithOutName); // Broadcast message to all clients
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out); // Remove client on disconnect
                }
            }
        }

        public static String ExtractNameFromMessage(String str)
        {
            StringBuilder name = new StringBuilder();
            char[] strToChars = str.toCharArray();

            for (int i = 1; strToChars[i] != '}' ; i++) {
                name.append(strToChars[i]);
            }

            return name.toString();
        }

        public static String ReturnStringWithoutName(String str)
        {
            StringBuilder message = new StringBuilder();
            char[] strToChars = str.toCharArray();
            int i = str.indexOf('}') + 1;


            for (; i < str.length(); i++) {
                message.append(strToChars[i]);
            }

            return message.toString();
        }

    }
}