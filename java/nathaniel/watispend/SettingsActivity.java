package nathaniel.watispend;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class SettingsActivity extends AppCompatActivity {
    UserValues vals = UserValues.getInstance();

    private void syncTermDates(){
        UploadTermDate task = new UploadTermDate();
        task.execute();
    }

    private void termBeginClicked(){
        final Calendar termBeginCalendar = Calendar.getInstance();
        Calendar beginCalendar = vals.termStart;
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                termBeginCalendar.set(Calendar.YEAR, year);
                termBeginCalendar.set(Calendar.MONTH, monthOfYear);
                termBeginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (termBeginCalendar.before(vals.termEnd)) {
                    vals.termStart = termBeginCalendar;
                    syncTermDates();
                    Context context = getApplicationContext();
                    CharSequence text = "'Term Begin' set!";

                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    CharSequence text = "'Term Begin' must be before 'Term End'!";

                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(SettingsActivity.this, date, termBeginCalendar
                .get(Calendar.YEAR), termBeginCalendar.get(Calendar.MONTH),
                termBeginCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.updateDate(beginCalendar.get(Calendar.YEAR), beginCalendar.get(Calendar.MONTH), beginCalendar.get(Calendar.DAY_OF_MONTH));
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
                if(termEndCalendar.after(vals.termStart)){
                    vals.termEnd = termEndCalendar;
                    syncTermDates();
                    Context context = getApplicationContext();
                    CharSequence text = "'Term End' set!";

                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    CharSequence text = "'Term Begin' must be before 'Term End'!";

                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
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

    private void addFundsClicked(){
        String url = "https://account.watcard.uwaterloo.ca/addfunds.asp";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
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
        graphButton.setOnClickListener(new View.OnClickListener() {

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
        RelativeLayout addFunds = (RelativeLayout) findViewById(R.id.addFundsLayout);
        addFunds.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addFundsClicked();
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

    private class UploadTermDate extends AsyncTask<String, String, String> {
        private String resp;
        private boolean error;
        int num = 0;

        public String makeRequest(String path, JSONObject params) throws Exception {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(path);
            JSONObject holder = params;
            StringEntity se = new StringEntity(holder.toString());

            String inputLine ;
            BufferedReader br = new BufferedReader(new InputStreamReader(se.getContent()));
            try {
                while ((inputLine = br.readLine()) != null) {
                    //System.out.println(inputLine);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpost.setEntity(se);
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("accept", "application/json");
            httpost.setHeader("Content-Type", "application/json");

            //Handles what is returned from the page
            ResponseHandler responseHandler = new BasicResponseHandler();
            return (String) httpclient.execute(httpost, responseHandler);
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Connecting..."); // Calls onProgressUpdate()
            try {
                JSONObject json = new JSONObject();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int autoNum = settings.getInt("usernum", 0);
                num = autoNum;
                int pin = settings.getInt("pin", 0);
                json.put("student_number", String.format("%08d", num));
                json.put("pin", String.format("%04d", pin));

                makeRequest("https://watispend.herokuapp.com/waterloo/userinfo", json);

                return makeRequest("https://watispend.herokuapp.com/waterloo/token", json);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resp = "error 1";
            } catch (IOException e) {
                error = true;
                resp = "error 2";
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject l1 = null;
            JSONObject l2 = null;
            try {
                l1 = new JSONObject(result);
                l2 = (JSONObject) l1.get("result");
                String token = (String) l2.get("token");
                Firebase reference = new Firebase("https://watispend.firebaseio.com/students/");

                Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Firebase reference = new Firebase("https://watispend.firebaseio.com/students/" + num);
                        Firebase termBeginReference = reference.child("user_info").child("time").child("term_start");
                        Firebase termEndReference = reference.child("user_info").child("time").child("term_finish");
                        Calendar termBegin = vals.termStart;
                        Calendar termEnd  = vals.termEnd;

                        String termBeginString = termBegin.get(Calendar.DAY_OF_MONTH) + "/" + (termBegin.get(Calendar.MONTH) + 1) + "/" + termBegin.get(Calendar.YEAR);
                        String termEndString = termEnd.get(Calendar.DAY_OF_MONTH) + "/" + (termEnd.get(Calendar.MONTH) + 1) + "/" + termEnd.get(Calendar.YEAR);

                        termBeginReference.setValue(termBeginString);
                        termEndReference.setValue(termEndString);

                    }
                     @Override
                     public void onAuthenticationError(FirebaseError firebaseError) {
                         System.out.println("Testing failed");
                     }
                };

                reference.authWithCustomToken(token, authResultHandler);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
