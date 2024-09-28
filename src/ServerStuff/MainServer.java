package ServerStuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import Utilities.JSONHandler;
import org.json.*;

public class MainServer {
    private static final Set<WorkerInNet> clientWriters = new HashSet<>();
    private static final Set<ChatRoom> chatRooms = new HashSet<>();
    private static Map<WorkerInNet, ChatRoom> whichWorkerInWhichRoom = new HashMap<>();
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
        private WorkerInNet worker;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            worker = new WorkerInNet();
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                worker.responseFromServer = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientWriters) {
                    clientWriters.add(worker); // Add this client's writer to the set
                }

                String message;
                while ((message = in.readLine()) != null) {
                    handleIncomingMessage(message);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                synchronized (clientWriters) {
                    clientWriters.remove(worker); // Remove client on disconnect
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
                case "JOIN_CHAT":
                    handleRequestToJoinChat(json);
                    break;
                case "REQUEST_LIST_OF_ACTIVE_USERS":
                    handleRequestOfActiveUsers(json);
                    break;
            }
        }

        private void handleChatMessage(JSONObject message)
        {
            System.out.println("Received: " + message);
            synchronized (clientWriters) {
                for (WorkerInNet writer : clientWriters) {
                    writer.responseFromServer.println(extractMessageFromGivenJson(message)); // Broadcast message to all clients
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
                boolean isUserAlreadyConnected = isFoundUserAlreadyConnected(foundUser);

                if(isUserAlreadyConnected)
                {
                    worker.responseFromServer.println("{ 'type': 'USER_ALREADY_LOGGED_IN' }");
                }
                else {
                    worker.setInfo(foundUser);
                    JSONObject response = new JSONObject();
                    response.put("type", "CRED_VALID");
                    response.put("workerinfo", foundUser);
                    worker.responseFromServer.println(response);
                }
            }
            else
            {
                worker.responseFromServer.println("{ 'type': 'CRED_INVALID' }");
            }
        }

        private boolean isFoundUserAlreadyConnected(JSONObject foundUser)
        {
            long IDofNewUser = foundUser.getLong("ID");

            for (WorkerInNet worker : clientWriters)
            {
                if(worker.getID() == IDofNewUser)
                {
                    return  true;
                }
            }

            return false;
        }

        public void handleRequestToJoinChat(JSONObject json)
        {

        }

        public void handleRequestOfActiveUsers(JSONObject json)
        {
            JSONObject response = new JSONObject();
            response.put("type","LIST_OF_USERS");

            JSONArray IDAndUser = new JSONArray();

            for (WorkerInNet worker : clientWriters)
            {
                JSONObject userEntry = new JSONObject();
                userEntry.put("ID", worker.networkID);
                userEntry.put("name", worker.getFullName());

                IDAndUser.put(userEntry);
                //IDAndUser.put(worker.networkID, worker.getFullName());
            }

            response.put("users",IDAndUser);

            System.out.println(response);
            worker.responseFromServer.println(response);
        }
    }

    private static class ChatRoom
    {
        LinkedList<String> conversation;
        LinkedList<WorkerInNet> chatterBugs;
        public static long numberOfRooms = 0;
        public long roomID;

        public ChatRoom()
        {
            conversation = new LinkedList<>();
            chatterBugs = new LinkedList<>();

            roomID = numberOfRooms;
            numberOfRooms++;
        }

        private void notifyServerThatPlaceHasBeenGiven()
        {

        }
    }


}