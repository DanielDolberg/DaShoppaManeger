package ShopClasses.CustomerClasses;

public abstract class Customer
{
    // values
    protected String fullName;
    protected long ID;
    protected String phoneNumber;
    protected CustomerStatus status;
    protected double discountPercent;

    // constructor
    public Customer(String i_fullName, String i_phoneNumber, long i_ID)
    {
        fullName = i_fullName;
        phoneNumber = i_phoneNumber;
        ID = i_ID;
    }

    public void BuyItem()
    {

    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String i_fullName) {
        fullName = i_fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String i_PhoneNumber) {
        phoneNumber = i_PhoneNumber;
    }

    public long getID() {
        return ID;
    }

    public void setID(long i_ID) {
        ID = i_ID;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus i_Status)
    {
        status = i_Status;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double i_DiscountPercent) {
        discountPercent = i_DiscountPercent;
    }
}
