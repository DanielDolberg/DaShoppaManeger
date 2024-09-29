package ServerStuff;

import ShopClasses.WorkerClasses.JobRole;
import ShopClasses.WorkerClasses.Worker;
import org.json.JSONObject;

import java.io.PrintWriter;

public class WorkerInNet extends Worker {

    static long amountOfUsersInNet = 0;
    public boolean isLoggedIn;

    public PrintWriter responseFromServer;

    public WorkerInNet() {
        super();
        isLoggedIn = false;
        amountOfUsersInNet++;//increment
    }
}
