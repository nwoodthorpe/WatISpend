package nathaniel.watispend;

import java.util.Date;

/**
 * Created by Nathaniel on 12/13/2015.
 */
public class UniversalListInput {
    Double total;
    Date date;

    String location;
    String time;
    double amount;

    boolean isDate;
    // A Universal List Input that allows us to pass both Transactions and TransactionDates into our adapter.
    public UniversalListInput(Double total, Date date, String location, String time, double amount, boolean isDate){
        this.total = total;
        this.date = date;
        this.location = location;
        this.time = time;
        this.amount = amount;
        this.isDate = isDate;
    }
}
