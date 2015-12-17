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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
// Transactions Activity java file.
public class TransactionsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setTitle(Html.fromHtml("<font color='#000000'> Transactions </font>"));
        setButtonListeners();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        ArrayList<UniversalListInput> mobileArray = new ArrayList<>();
        mobileArray.add(new UniversalListInput(10.75, date, "", "", 0, true));
        mobileArray.add(new UniversalListInput(0.0, date, "SLC Tim Hortons", "9:29 AM", -10.00, false));
        mobileArray.add(new UniversalListInput(0.0, date, "MathSoc", "11:53 AM", -0.75, false));
        cal.add(Calendar.DATE, -2);
        date = cal.getTime();
        mobileArray.add(new UniversalListInput(49.21, date, "", "", 0, true));
        mobileArray.add(new UniversalListInput(0.0, date, "SCH Bookstore", "8:24 AM", -34.99, false));
        mobileArray.add(new UniversalListInput(0.0, date, "Refill", "9:29 AM", 100, false));
        mobileArray.add(new UniversalListInput(0.0, date, "Chopsticks", "12:38 PM", -9.80, false));
        mobileArray.add(new UniversalListInput(0.0, date, "Burger King", "4:59 PM", -6.12, false));
        mobileArray.add(new UniversalListInput(0.0, date, "MathSoc", "8:00 PM", -3.10, false));
        cal.add(Calendar.DATE, -1);
        date = cal.getTime();
        mobileArray.add(new UniversalListInput(10.0, date, "", "", 0, true));
        ArrayAdapter adapter = new TransactionListAdapter(this, 0, mobileArray);
        mobileArray.add(new UniversalListInput(0.0, date, "MathSoc", "9:00 AM", -0.75, false));
        mobileArray.add(new UniversalListInput(0.0, date, "Subway", "11:49 AM", -6.75, false));
        mobileArray.add(new UniversalListInput(0.0, date, "MathSoc", "12:38 PM", -0.75, false));
        mobileArray.add(new UniversalListInput(0.0, date, "V1 Caf", "4:59 PM", -4.44, false));
        // Link the data and our listview using the adapter.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
