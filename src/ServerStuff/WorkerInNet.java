package ServerStuff;

import ShopClasses.WorkerClasses.JobRole;
import ShopClasses.WorkerClasses.Worker;
import org.json.JSONObject;

import java.io.PrintWriter;

public class WorkerInNet extends Worker {

    static long amountOfUsersInNet = 0;

    public long networkID;
    public PrintWriter responseFromServer;

    public WorkerInNet() {
        super();
        networkID = amountOfUsersInNet;

        amountOfUsersInNet++;//increment
    }

    private static JobRole convertTextToJobRole(String stringOfRole) {
        JobRole jobRole = null;

        switch (stringOfRole) {
            case "ShiftManager":
                jobRole = JobRole.ShiftManager;
                break;
            case "Cashier":
                jobRole = JobRole.Cashier;
                break;
            case "SalesPerson":
                jobRole = JobRole.SalesPerson;
                break;
            case "Admin":
                jobRole = JobRole.Admin;
                break;
        }

        return jobRole;
    }

    public void setInfo(JSONObject json) {
        jobRole = convertTextToJobRole(json.getString("jobRole"));
        workerNumber = -1;
        branchName = json.getString("branchName");
        accountNumber = json.getLong("accountNumber");
        phoneNumber = json.getString("phoneNumber");
        ID = json.getLong("ID");
        fullName = json.getString("fullName");
    }
}
