package nathaniel.watispend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    UserValues vals = UserValues.getInstance();

    private void syncTermDates(){

    }

    private void termBeginClicked(){
        final Calendar termBeginCalendar = Calendar.getInstance();
        Calendar formerCalendar = vals.termStart;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                termBeginCalendar.set(Calendar.YEAR, year);
                termBeginCalendar.set(Calendar.MONTH, monthOfYear);
                termBeginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                vals.termStart = termBeginCalendar;
                syncTermDates();
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(SettingsActivity.this, date, termBeginCalendar
                .get(Calendar.YEAR), termBeginCalendar.get(Calendar.MONTH),
                termBeginCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.updateDate(formerCalendar.get(Calendar.YEAR), formerCalendar.get(Calendar.MONTH), formerCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void termEndClicked(){
        final Calendar termEndCalendar = Calendar.getInstance();
        Calendar formerCalendar = vals.termEnd;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                termEndCalendar.set(Calendar.YEAR, year);
                termEndCalendar.set(Calendar.MONTH, monthOfYear);
                termEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                vals.termEnd = termEndCalendar;
                syncTermDates();
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(SettingsActivity.this, date, termEndCalendar
                .get(Calendar.YEAR), termEndCalendar.get(Calendar.MONTH),
                termEndCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.updateDate(formerCalendar.get(Calendar.YEAR), formerCalendar.get(Calendar.MONTH), formerCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
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
        RelativeLayout termEnd = (RelativeLayout) findViewById(R.id.termEndLayout);
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
