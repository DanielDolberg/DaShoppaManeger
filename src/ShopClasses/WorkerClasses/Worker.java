package ShopClasses.WorkerClasses;

import org.json.JSONObject;

public class Worker
{
    protected String fullName;
    protected long ID;
    protected String phoneNumber;
    protected long accountNumber;
    protected String branchName;
    protected long workerNumber;
    protected JobRole jobRole;
    //empty constructor for use in derived classes
    public Worker()
    {

    }

    public Worker(JobRole jobRole, long workerNumber, String branchName, long accountNumber, String phoneNumber, long ID, String fullName) {
        this.jobRole = jobRole;
        this.workerNumber = workerNumber;
        this.branchName = branchName;
        this.accountNumber = accountNumber;
        this.phoneNumber = phoneNumber;
        this.ID = ID;
        this.fullName = fullName;
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

    public void setInfoFromJson(JSONObject json) {
        jobRole = convertTextToJobRole(json.getString("jobRole"));
        workerNumber = -1;
        branchName = json.getString("branchName");
        accountNumber = json.getLong("accountNumber");
        phoneNumber = json.getString("phoneNumber");
        ID = json.getLong("ID");
        fullName = json.getString("fullName");
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public long getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(long workerNumber) {
        this.workerNumber = workerNumber;
    }

    public JobRole getJobRole() {
        return jobRole;
    }

    public void setJobRole(JobRole jobRole) {
        this.jobRole = jobRole;
    }
}
