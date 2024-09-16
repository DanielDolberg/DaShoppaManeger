package ShopClasses.CustomerClasses;

public class ReturningCustomer extends Customer {
    public ReturningCustomer(String i_fullName, String i_phoneNumber, long i_ID) {
        super(i_fullName,i_phoneNumber,i_ID);
        status = CustomerStatus.Returning;
        discountPercent = 0.2;
    }

    @Override
    public void BuyItem()
    {
        System.out.println("do something later");
    }

}
