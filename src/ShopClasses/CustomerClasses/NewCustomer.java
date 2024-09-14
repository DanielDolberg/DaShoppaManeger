package ShopClasses.CustomerClasses;

public class NewCustomer extends Customer {

    public NewCustomer(String i_fullName, long i_phoneNumber, long i_ID) {
        super(i_fullName,i_phoneNumber,i_ID);
        status = CustomerStatus.New;
        discountPercent = 0.0;
    }

    @Override
    public void BuyItem()
    {
        System.out.println("do something later");
    }
}
