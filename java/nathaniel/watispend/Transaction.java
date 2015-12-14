package nathaniel.watispend;

/**
 * Created by Nathaniel on 12/13/2015.
 */
public class Transaction {
    String location;
    String time;
    double amount;
    public Transaction(String location, String time, double amount) {
        this.location = location;
        this.time = time;
        this.amount = amount;
    }
}
