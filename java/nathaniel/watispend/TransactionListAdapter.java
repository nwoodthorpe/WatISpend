package nathaniel.watispend;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Nathaniel on 12/13/2015.
 */
// Adapter that links our data to the ListView on Transactions Activity
public class TransactionListAdapter extends ArrayAdapter<UniversalListInput> {
    private ArrayList<UniversalListInput> users;
    final int dateLayoutXML = R.layout.listitem;
    final int transactionLayoutXML = R.layout.transaction_item;
    public TransactionListAdapter(Context context, int textViewResourceId, ArrayList<UniversalListInput> users) {
        super(context, textViewResourceId, users);
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UniversalListInput user = users.get(position);
        boolean isDate = users.get(position).isDate;
        View v = null;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = 0;
            if(isDate){
                layout = R.layout.listitem;
            }else{
                layout = R.layout.transaction_item;
            }
            v = vi.inflate(layout, parent, false);

        }


        if (user != null) {
            if(isDate) {
                TextView total = (TextView) v.findViewById(R.id.total);
                TextView date = (TextView) v.findViewById(R.id.date);
                DecimalFormat df = new DecimalFormat("0.00");
                String userTotal = df.format(user.total);
                if (total != null) {
                    total.setText("Total: $" + userTotal);
                }
                DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                if (date != null) {
                    date.setText(dateFormat.format(user.date));
                }
            }else{
                TextView location = (TextView) v.findViewById(R.id.location);
                TextView amount = (TextView) v.findViewById(R.id.amount);
                TextView time = (TextView) v.findViewById(R.id.time);

                if (location != null) {
                    location.setText(user.location);
                }
                if (amount != null) {
                    double userAmount = user.amount;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String stringAmount = df.format(userAmount);
                    // Change color depending on sign of amount.
                    if(userAmount > 0){
                        amount.setTextColor(Color.parseColor("#009933"));
                        amount.setText("+" + stringAmount);
                    }else{
                        amount.setTextColor(Color.parseColor("#cc0000"));
                        amount.setText(stringAmount);
                    }
                }
                if (time != null) {
                    time.setText(user.time);
                }
            }
        }
        return v;
    }
}
