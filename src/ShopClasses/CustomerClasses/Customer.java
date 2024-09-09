package ShopClasses.CustomerClasses;

public class Customer
{
    private String fullName;
    private long ID;
    private long phoneNumber;
    private CustomerStatus status;


    public Customer(String i_fullName, long i_phoneNumber, long i_ID) {
        fullName = i_fullName;
        phoneNumber = i_phoneNumber;
        ID = i_ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String i_fullName) {
        fullName = i_fullName;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long i_PhoneNumber) {
        phoneNumber = i_PhoneNumber;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public long getID() {
        return ID;
    }

    public void setID(long i_ID) {
        ID = i_ID;
    }

    public void setStatus(CustomerStatus i_Status)
    {
        status = i_Status;
    }
}
