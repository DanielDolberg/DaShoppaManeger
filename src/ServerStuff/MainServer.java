package ServerStuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import Utilities.JSONHandler;
import org.json.*;

public class MainServer {
    private static final Set<PrintWriter> clientWriters = new HashSet<>();
    private static JSONArray customers;
    private static JSONArray sales;
    private static JSONArray stock;
    private static JSONArray workers;

    public static void main(String[] args) throws Exception {
        loadAllJsons();


        System.out.println("Chat server started...");
        ServerSocket serverSocket = new ServerSocket(12345); // Use a specific port for connection
        while (true) {
            new ClientHandler(serverSocket.accept()).start(); // Accept incoming client connections
        }
    }

    private static void loadAllJsons() throws IOException
    {
        // Load the JSON data from the file
        JSONObject jsonDataCustomers = JSONHandler.readFrom(JSONHandler.CustomersJsonFilePath);
        customers = jsonDataCustomers.getJSONArray("clients");

        JSONObject jsonDataWorkers = JSONHandler.readFrom(JSONHandler.WorkersJsonFilePath);
        workers = jsonDataWorkers.getJSONArray("workers");

        //JSONObject jsonDataStock = JSONHandler.readFrom(JSONHandler.StockJsonFilePath);
        //stock = jsonDataStock.getJSONArray("stock");
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;
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
                    handleIncomingMessage(message);
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

        private void handleIncomingMessage(String message)
        {
            JSONObject json = new JSONObject(message);

            System.out.println(json);

            String typeOfMessage = json.getString("type");

            switch (typeOfMessage)
            {
                case "CHECK_IF_VALID_CRED" :
                    handleAuthenticationRequest(json);
                    break;
                case "CHAT_MESSAGE":
                    handleChatMessage(json);
                    break;
            }
        }

        private void handleChatMessage(JSONObject message)
        {
            System.out.println("Received: " + message);
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(extractMessageFromGivenJson(message)); // Broadcast message to all clients
                }
            }
        }


        private String extractMessageFromGivenJson(JSONObject clientInput)
        {
            String[] jsonInArr =  new String[]{
                    clientInput.getString("name"),
                    clientInput.getString("message"),
                    clientInput.getString("time_sent")
            };

            return "["+jsonInArr[2]+": " + jsonInArr[0] + "]: " + jsonInArr[1];
        }

        private void handleAuthenticationRequest(JSONObject json)
        {
            String userName = json.getString("username");
            String password = json.getString("password");

            JSONObject foundUser = null;

            for (int i = 0; i < workers.length(); i++) {
                JSONObject user = workers.getJSONObject(i);
                String userNameInJson = user.getString("username");
                String passwordInJson = user.getString("password");

                if (userName.equals(userNameInJson) && password.equals(passwordInJson)) {
                    foundUser = user;
                    break;
                }
            }

            if(foundUser != null)
            {
                JSONObject response = new JSONObject();
                response.put("type","CRED_VALID");
                response.put("workerinfo", foundUser);
                out.println(response);
            }
            else
            {
                out.println("{ 'type': 'CRED_INVALID' }");
            }
        }
    }
}