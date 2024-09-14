package ShopClasses.WorkerClasses;

public class Worker
{
    protected String fullName;
    protected long ID;
    protected long phoneNumber;
    protected long accountNumber;
    protected String branchName;

    public Worker(JobRole jobRole, long workerNumber, String branchName, long accountNumber, long phoneNumber, long ID, String fullName) {
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

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
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
