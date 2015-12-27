package nathaniel.watispend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// Transactions Activity java file.
public class TransactionsActivity extends AppCompatActivity {
    TreeMap<Calendar, ArrayList<Transaction>> transactions;
    private void onGraphClick(View v){
        Intent loggedInIntent = new Intent(TransactionsActivity.this, GraphActivity.class);
        TransactionsActivity.this.startActivity(loggedInIntent);
    }

    private void setButtonListeners(){
        ImageView graphButton = (ImageView)findViewById(R.id.graphButton);
        graphButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onGraphClick(v);
            }
        });
    }

    private ArrayList<UniversalListInput> fillListView(HashMap<Calendar, ArrayList<Transaction>> transactions) throws ParseException {
        ArrayList<UniversalListInput> transactionsList = new ArrayList<>();
        Map<Calendar, ArrayList<Transaction>> sortedMap = new TreeMap<Calendar, ArrayList<Transaction>>(Collections.<Calendar>reverseOrder());
        sortedMap.putAll(transactions);
        Set set = sortedMap.entrySet();
        Iterator it = set.iterator();
        while ( it.hasNext() ) {
            Map.Entry entry = (Map.Entry) it.next();

            Calendar key = (Calendar) entry.getKey();
            Date keyDate = key.getTime();
            double dailySum = 0;
            ArrayList<Transaction> rawTransactions = (ArrayList<Transaction>) entry.getValue();
            ArrayList<UniversalListInput> dailyTransactionsList = new ArrayList<>();

            for(int i = 0; i<rawTransactions.size(); i++){
                Transaction current = rawTransactions.get(i);
                dailySum += current.amount;
                DateFormat originalFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("hh:mm a");
                dailyTransactionsList.add(new UniversalListInput(0.0, keyDate, current.location, targetFormat.format(originalFormat.parse(current.time)), current.amount, false));
            }

            transactionsList.add(new UniversalListInput(Math.abs(dailySum), keyDate, "", "", 0, true));
            for(int i = 0; i<dailyTransactionsList.size(); i++){
                transactionsList.add(dailyTransactionsList.get(i));
            }
        }
        return transactionsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setTitle(Html.fromHtml("<font color='#000000'> Transactions </font>"));
        setButtonListeners();

        ArrayList<UniversalListInput> mobileArray = null;
        try {
            mobileArray = fillListView(UserValues.getInstance().transactions);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new TransactionListAdapter(this, 0, mobileArray);
        // Link the data and our listview using the adapter.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
