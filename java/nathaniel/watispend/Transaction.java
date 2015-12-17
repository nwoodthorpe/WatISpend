package nathaniel.watispend;

/**
 * Created by Nathaniel on 12/13/2015.
 */
// Simple class stores Transaction information in an orginized manner.
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
