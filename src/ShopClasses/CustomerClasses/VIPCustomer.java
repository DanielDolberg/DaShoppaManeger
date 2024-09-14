package ShopClasses.CustomerClasses;

public class VIPCustomer extends Customer {
    public VIPCustomer(String i_fullName, long i_phoneNumber, long i_ID) {
        super(i_fullName,i_phoneNumber,i_ID);
        status = CustomerStatus.VIP;
        discountPercent = 0.5;
    }

    @Override
    public void BuyItem()
    {
        System.out.println("do something later");
    }
}
