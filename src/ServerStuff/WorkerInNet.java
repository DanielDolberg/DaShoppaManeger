package ServerStuff;

import ShopClasses.WorkerClasses.Worker;

import java.io.PrintWriter;

public class WorkerInNet extends Worker {

    public boolean isLoggedIn;
    public PrintWriter writeToWorker;

    public WorkerInNet() {
        super();
        isLoggedIn = false;
    }
}
