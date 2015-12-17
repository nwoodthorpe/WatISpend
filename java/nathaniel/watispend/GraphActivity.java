package nathaniel.watispend;

import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
// Graph Activity java file.
public class GraphActivity extends AppCompatActivity {

    private void onSettingsClick(View v){
        Intent settingsClickIntent = new Intent(GraphActivity.this, PreferencesActivity.class);
        GraphActivity.this.startActivity(settingsClickIntent);
    }

    private void onTransactionsClick(View v){
        Intent transactionsClickIntent = new Intent(GraphActivity.this, TransactionsActivity.class);
        GraphActivity.this.startActivity(transactionsClickIntent);
    }

    private void setButtonListeners(){
        ImageView settingsButton = (ImageView)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSettingsClick(v);
            }
        });

        ImageView transactionsButton = (ImageView)findViewById(R.id.transactionsButton);
        transactionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onTransactionsClick(v);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setButtonListeners();
    }
}
