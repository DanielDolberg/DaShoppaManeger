package ServerStuff;

import ShopClasses.WorkerClasses.JobRole;
import ShopClasses.WorkerClasses.Worker;
import org.json.JSONObject;

import java.io.PrintWriter;

public class WorkerInNet extends Worker {

    static long amountOfUsersInNet = 0;

    public PrintWriter responseFromServer;

    public WorkerInNet() {
        super();
        amountOfUsersInNet++;//increment
    }
}
