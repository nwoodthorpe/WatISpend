package nathaniel.watispend;

import java.util.Date;

/**
 * Created by Nathaniel on 12/13/2015.
 */
//Simple class stores Transaction Date info in an organised manner.
public class TransactionDate {
    Date date;
    double total;
    public TransactionDate(Date date, double total){
        this.date = date;
        this.total = total;
    }

}
