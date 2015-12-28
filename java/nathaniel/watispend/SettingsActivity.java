package nathaniel.watispend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

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
        Switch autoLoginSwitch = (Switch) findViewById(R.id.autoLoginSwitch);
        autoLoginSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("autologin", isChecked);
                editor.commit();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setOnClickListeners();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Switch autoLoginSwitch = (Switch) findViewById(R.id.autoLoginSwitch);
        autoLoginSwitch.setChecked(settings.getBoolean("autologin", false));
    }
}
