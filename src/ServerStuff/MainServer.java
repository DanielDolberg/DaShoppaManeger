package ServerStuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import ShopClasses.WorkerClasses.JobRole;
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

    public static WorkerInNet getWorkerById(long id)
    {
        for (WorkerInNet worker : clientWriters)
        {
            if(worker.getID() == id)
            {
                return worker;
            }
        }

        return null;
    }

    public static void NotifyWorkerTheyJoinedTheChat(WorkerInNet workerToNotif, ChatRoom chatRoom)
    {
        JSONObject notification = new JSONObject();

        notification.put("type", "NOTIFY_USER_JOIN_CHAT");
        notification.put("roomID", chatRoom.roomID);

        workerToNotif.responseFromServer.println(notification);
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
                case "REQUEST_LIST_OF_ACTIVE_USERS":
                    handleRequestOfActiveUsers(json);
                    break;
                case "REQUEST_TO_START_OR_JOIN_CHAT":
                    handleRequestToJoinChat(json);
                    break;
                case "NOTIFY_USER_LEFT_CHAT":
                    handleUserLeavingChat(json);
                    break;
            }
        }

        private void handleChatMessage(JSONObject message)
        {
            ChatRoom room = null;
            long roomID = message.getLong("roomID");
            for (ChatRoom r : chatRooms)
            {
                if(r.roomID == roomID)
                {
                    room = r;
                    break;
                }
            }

            if(room != null)
            {
                room.TakeMessage(message);
            }


        }

        private void handleUserLeavingChat(JSONObject json)
        {
            long workerID = json.getLong("workerID");
            long roomID = json.getLong("roomID");

            WorkerInNet leavingWorker = getWorkerById(workerID);
            ChatRoom room = whichWorkerInWhichRoom.get(leavingWorker);

            room.chatterBugs.remove(leavingWorker);

            room.letNextWorkerInQueueIn();
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
                    worker.setInfoFromJson(foundUser);
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
            WorkerInNet requestee =  getWorkerById(json.getLong("requestedUser"));
            WorkerInNet requester =  getWorkerById(json.getLong("requester"));

            checkIfWorkerAlreadyisWaitingForRoomAndRemoveIfDoes(requestee);

            ChatRoom room =  whichWorkerInWhichRoom.get(requestee);

            if(room == null)
            {
                room = createNewRoom();
            }

            if(room.chatterBugs.size() < 2 || requester.getJobRole() == JobRole.ShiftManager || requester.getJobRole() == JobRole.Admin)
            {
                whichWorkerInWhichRoom.put(requester,room);
                room.chatterBugs.add(requester);
                String response = "{ " +
                        "'type':'REQUEST_TO_JOIN_CHAT_ACCEPTED'," +
                        "'roomID':" + room.roomID +
                        "}";

                worker.responseFromServer.println(response);
            }
            else
            {
                room.peopleInQueue.add(requester);
                String response = "{ 'type':'REQUEST_TO_JOIN_CHAT_PUT_ON_HOLD', 'position' :" +room.peopleInQueue.size() +"}";
                worker.responseFromServer.println(response);
            }
        }

        public static void checkIfWorkerAlreadyisWaitingForRoomAndRemoveIfDoes(WorkerInNet worker)
        {
            for (ChatRoom room : chatRooms)
            {
                if(room.peopleInQueue.contains(worker))
                {
                    room.peopleInQueue.remove(worker);
                    break;
                }
            }
        }

        public void handleRequestOfActiveUsers(JSONObject json)
        {
            JSONObject response = new JSONObject();
            response.put("type","LIST_OF_USERS");

            JSONArray IDAndUser = new JSONArray();

            for (WorkerInNet worker : clientWriters)
            {
                JSONObject userEntry = new JSONObject();
                userEntry.put("ID", worker.getID());
                userEntry.put("name", worker.getFullName());

                IDAndUser.put(userEntry);
                //IDAndUser.put(worker.getID(), worker.getFullName());
            }

            response.put("users",IDAndUser);

            System.out.println(response);
            worker.responseFromServer.println(response);
        }
    }

    public static class ChatRoom
    {
        LinkedList<String> conversation;
        LinkedList<WorkerInNet> chatterBugs;
        Queue<WorkerInNet> peopleInQueue;

        public static long numberOfRooms = 0;
        public long roomID;

        public ChatRoom()
        {
            conversation = new LinkedList<>();
            chatterBugs = new LinkedList<>();

            roomID = numberOfRooms;
            numberOfRooms++;
            peopleInQueue = new LinkedList<>();
        }

        public void TakeMessage(JSONObject message)
        {
            System.out.println("Received: " + message);
            String extractedMessage = extractMessageFromGivenJson(message);
            synchronized (chatterBugs) {
                for (WorkerInNet writer : chatterBugs) {
                    JSONObject sentObject = new JSONObject();
                    sentObject.put("type","RECEIVED_CHAT_MESSAGE");
                    sentObject.put("text",extractedMessage);

                    writer.responseFromServer.println(sentObject); // Broadcast message to all clients
                    conversation.add(extractedMessage);
                }
            }
        }

        public void letNextWorkerInQueueIn()
        {
            if(!peopleInQueue.isEmpty())
            {
                 MainServer.NotifyWorkerTheyJoinedTheChat(peopleInQueue.poll(), this);
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
    }

    private static ChatRoom createNewRoom()
    {
        ChatRoom newRoom = new ChatRoom();
        chatRooms.add(newRoom);

        return newRoom;
    }

}