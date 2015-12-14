package nathaniel.watispend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setTitle(Html.fromHtml("<font color='#000000'> Transactions </font>"));
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
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
