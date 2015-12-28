package nathaniel.watispend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SettingsActivity extends AppCompatActivity {

    private void termBeginClicked(){

    }

    private void termEndClicked(){

    }

    private void commonQuestionsClicked(){
        Intent intent = new Intent(SettingsActivity.this, CommonQuestionsActivity.class);
        SettingsActivity.this.startActivity(intent);
    }

    private void contactClicked(){
        Intent intent = new Intent(SettingsActivity.this, WebViewActivity.class);
        intent.putExtra("title", "Contact");
        intent.putExtra("domain", "http://watispend.com/#contact");
        SettingsActivity.this.startActivity(intent);
    }

    private void privacyClicked(){
        Intent intent = new Intent(SettingsActivity.this, WebViewActivity.class);
        intent.putExtra("title", "Privacy");
        intent.putExtra("domain", "http://watispend.com/privacy.html");
        SettingsActivity.this.startActivity(intent);
    }

    private void graphButtonClicked(){
        Intent intent = new Intent(SettingsActivity.this, GraphActivity.class);
        SettingsActivity.this.startActivity(intent);
    }

    private void setOnClickListeners(){
        RelativeLayout termBegin = (RelativeLayout) findViewById(R.id.termBeginLayout);
        termBegin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                termBeginClicked();
            }
        });
        RelativeLayout termEnd = (RelativeLayout) findViewById(R.id.termBeginLayout);
        termEnd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                termEndClicked();
            }
        });
        RelativeLayout commonQuestions = (RelativeLayout) findViewById(R.id.commonQuestionsLayout);
        commonQuestions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                commonQuestionsClicked();
            }
        });
        RelativeLayout contact = (RelativeLayout) findViewById(R.id.contactLayout);
        contact.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                contactClicked();
            }
        });
        RelativeLayout privacy = (RelativeLayout) findViewById(R.id.privacyLayout);
        privacy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                privacyClicked();
            }
        });
        ImageView graphButton = (ImageView) findViewById(R.id.graphButton);
        graphButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                graphButtonClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setOnClickListeners();
    }
}
