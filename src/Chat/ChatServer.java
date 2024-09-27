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
import org.json.*;

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
                        for (PrintWriter writer : clientWriters) {
                            writer.println(extractMessageFromGivenJson(message)); // Broadcast message to all clients
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

        public static String extractMessageFromGivenJson(String messageInJsonForm)
        {
            JSONObject json = new JSONObject(messageInJsonForm);

            System.out.println(json);

            String[] jsonInArr =  new String[]{
                    json.getString("name"),
                    json.getString("message"),
                    json.getString("time_sent")
            };

            return "["+jsonInArr[2]+": " + jsonInArr[0] + "]: " + jsonInArr[1];
        }
    }
}