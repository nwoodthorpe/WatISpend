package nathaniel.watispend;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.Crashlytics;

// Login Activity java file.
public class LoginActivity extends AppCompatActivity {
    protected String numText;
    protected String pinText;
    protected int loadCount = 0;
    final int LOGINTIMEOUT = 60;//Time until login times out in seconds
    final FinalInt timeoutCount = new FinalInt(LOGINTIMEOUT);

    //Android changes to a monospaced font when you use a password field.
    //This fixes to keep fonts and styles consistant.
    private void fixPasswordFont(){
        EditText password = (EditText) findViewById(R.id.studentPIN);
        EditText studentNum = (EditText) findViewById(R.id.studentNumber);
        studentNum.setTransformationMethod(new ClickableSpan());
        studentNum.setTypeface(Typeface.DEFAULT);

        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void hideActionBar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void onLoginClick(View view) throws Exception {
        //Constants for credential restrictions
        final int STUDENT_NUM_LENGTH = 8;
        final int STUDENT_PIN_LOWER = 4;
        final int STUDENT_PIN_UPPER = 6;
        boolean errorFlag = false;

        EditText studentNumberEdit = (EditText) findViewById(R.id.studentNumber);
        EditText studentPinEdit = (EditText) findViewById(R.id.studentPIN);

        final int studentNumLengthActual = studentNumberEdit.getText().length();
        if(studentNumLengthActual != STUDENT_NUM_LENGTH){
            studentNumberEdit.setError("Invalid student number.");
            errorFlag = true;
        }

        final int pinLength = studentPinEdit.getText().length();
        if(pinLength < 4 || pinLength > 6){ //Pin length must be 4-6 numbers
            studentPinEdit.setError("Invalid WATCard Pin.");
            errorFlag = true;
        }

        if(!errorFlag) {
            numText = studentNumberEdit.getText().toString();
            pinText = studentPinEdit.getText().toString();
            LoginTask task = new LoginTask();
            loadCount = 0;
            timeoutCount.val = LOGINTIMEOUT;
            task.execute();

            final ProgressDialog progress = ProgressDialog.show(this, "Logging In...",
                    "Please wait...", true);

            final Handler handler = new Handler();

            final Runnable r = new Runnable() {
                boolean emptyFlag = false;
                int count = 0;
                public void run() {
                    if(loadCount == 5) { //Data fully loaded
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("usernum", Integer.parseInt(numText));
                        editor.putInt("pin", Integer.parseInt(pinText));
                        editor.commit();
                        Intent successfulLogin = new Intent(LoginActivity.this, GraphActivity.class);
                        LoginActivity.this.startActivity(successfulLogin);
                        progress.dismiss();
                        LoginActivity.this.finish();
                    }else if(timeoutCount.val <= -5){
                        progress.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Login Error!");
                        alertDialog.setMessage("Make sure your student number and pin are correct!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }else if(timeoutCount.val <= 0) {
                        progress.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Network Timeout!");
                        alertDialog.setMessage("Login took too long. Make sure you have an internet connection.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK.",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }else if(emptyFlag) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("usernum", Integer.parseInt(numText));
                        editor.putInt("pin", Integer.parseInt(pinText));
                        editor.commit();
                        Intent successfulLogin = new Intent(LoginActivity.this, TransactionsActivity.class);
                        LoginActivity.this.startActivity(successfulLogin);
                        progress.dismiss();
                        LoginActivity.this.finish();
                    }else if(loadCount==4) {
                        count++;
                        if(count > 4) emptyFlag = true;
                        handler.postDelayed(this, 1000);
                    }else{
                        handler.postDelayed(this, 1000);
                    }
                    timeoutCount.val = timeoutCount.val - 1;
                }
            };

            handler.postDelayed(r, 1000);
        }
    }

    private void setButtonListeners(){
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    onLoginClick(v);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processData(String key, Object value) throws JSONException, ParseException {
        UserValues vals = UserValues.getInstance();
        switch(key){
            case "password":
                break;
            case "user_info":
                JSONObject jsonObject = new JSONObject((HashMap<String, String>) value);
                Iterator<?> keys = jsonObject.keys();

                while( keys.hasNext() ) {
                    String val = (String)keys.next();
                    if ( jsonObject.get(val) instanceof JSONObject ) {
                        JSONObject innerObject = (JSONObject)jsonObject.get(val);
                        switch(val){
                            case "time":
                                Calendar beginCal = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                                beginCal.setTime(format.parse((String) innerObject.get("term_start")));
                                vals.termStart = beginCal;

                                Calendar endCal = Calendar.getInstance();
                                endCal.setTime(format.parse((String) innerObject.get("term_finish")));
                                vals.termEnd = endCal;
                                break;

                            case "balances":
                                vals.totalBalance = Double.parseDouble((String)innerObject.get("total"));

                                vals.mealPlan = Double.parseDouble((String) innerObject.get("mealplan"));

                                vals.flex = Double.parseDouble((String) innerObject.get("flex"));
                                break;

                            case "current":
                                vals.currentWeekly = Double.parseDouble((String) innerObject.get("weekly"));

                                vals.currentDaily = Double.parseDouble((String) innerObject.get("daily"));
                                break;

                            case "suggest":
                                vals.suggestWeekly = Double.parseDouble((String) innerObject.get("weekly"));

                                vals.suggestDaily = Double.parseDouble((String) innerObject.get("daily"));
                        }
                    }
                }
                break;
            case "chart_data":
                JSONObject chartJSON = new JSONObject((HashMap<String, String>) value);
                JSONArray chartArray = (JSONArray) chartJSON.get("bar");
                ArrayList<Double> chartData = new ArrayList<>();
                for(int i = 0; i<chartArray.length(); i++){
                    chartData.add(Double.parseDouble(chartArray.get(i).toString()));
                }
                vals.chartData = chartData;
                break;

            case "transactions":
                JSONObject transJSON = new JSONObject((HashMap<String, String>) value);

                Iterator<?> transKeys = transJSON.keys();
                while( transKeys.hasNext() ) {
                    String val = (String) transKeys.next();
                    if (transJSON.get(val) instanceof JSONArray) {
                        ArrayList<Transaction> transactions = new ArrayList<>();
                        JSONArray innerArray = (JSONArray) transJSON.get(val);
                        for(int i = 0; i<innerArray.length(); i++){
                            JSONObject innerObject = innerArray.getJSONObject(i);
                            transactions.add(new Transaction(
                                    (String) innerObject.get("location"),
                                    (String) innerObject.get("time"),
                                    Double.parseDouble(((String)innerObject.get("amount")).replaceAll(",", ""))));
                        }
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);
                        cal.setTime(format.parse(val));
                        vals.transactions.put(cal, transactions);
                    }
                }
                break;
        }
        loadCount += 1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fixPasswordFont();
        setButtonListeners();
        UserValues vals = UserValues.getInstance();
        vals.transactions = new HashMap<Calendar, ArrayList<Transaction>>();
        Firebase.setAndroidContext(this);
        Fabric.with(this, new Answers());
        Fabric.with(this, new Crashlytics());
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean autologin = settings.getBoolean("autologin", false);
        int autoNum = settings.getInt("usernum", 0);
        int pin = settings.getInt("pin", 0);
        if(autologin && (autoNum != 0) && (pin != 0)){
            //Auto login
            EditText studentNumberEdit = (EditText) findViewById(R.id.studentNumber);
            EditText studentPinEdit = (EditText) findViewById(R.id.studentPIN);
            studentNumberEdit.setText(String.format("%08d", autoNum));
            studentPinEdit.setText(String.format("%04d", pin));
            Button loginButton = (Button) findViewById(R.id.loginButton);
            loginButton.callOnClick();
        }

    }

    private class LoginTask extends AsyncTask<String, String, String> {

        private String resp;

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
                json.put("student_number", numText);
                json.put("pin", pinText);
                if(!numText.equals("99999999")) {
                    makeRequest("https://watispend.herokuapp.com/waterloo/transactions", json);
                    makeRequest("https://watispend.herokuapp.com/waterloo/userinfo", json);
                    JSONObject chartJSON = new JSONObject();
                    chartJSON.put("student_number", numText);
                    chartJSON.put("pin", pinText);
                    chartJSON.put("chart_data", "bar");
                    makeRequest("https://watispend.herokuapp.com/waterloo/chart", chartJSON);
                }
                return makeRequest("https://watispend.herokuapp.com/waterloo/token", json);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resp = "error 1";
            } catch (IOException e) {
                e.printStackTrace();
                timeoutCount.val = -5; //Set error to "LOGIN ERROR"
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
            if(timeoutCount.val > -5) {
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
                            UserValues.getInstance().chartChange = false;
                            Firebase ref = new Firebase("https://watispend.firebaseio.com/students/" + numText + "/transactions");
                            Query q = ref.orderByValue();
                            Firebase reference = new Firebase("https://watispend.firebaseio.com/students/" + numText);
                            Query queryRef = reference.orderByValue();

                            queryRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                                    try {
                                        processData(snapshot.getKey(), snapshot.getValue());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    UserValues.getInstance().chartChange = true;
                                    try {
                                        processData(dataSnapshot.getKey(), dataSnapshot.getValue());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                                // ....
                            });
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
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
