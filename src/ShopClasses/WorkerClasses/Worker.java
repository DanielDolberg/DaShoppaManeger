package ShopClasses.WorkerClasses;

public class Worker
{
    protected String fullName;
    protected long ID;
    protected String phoneNumber;
    protected long accountNumber;
    protected String branchName;

    //empty constructor for use in derived classes
    protected Worker()
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

    protected long workerNumber;
    protected JobRole jobRole;
}
