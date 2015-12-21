package nathaniel.watispend;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.Map;

// Login Activity java file.
public class LoginActivity extends AppCompatActivity {
    protected String numText;
    protected String pinText;

    //Android changes to a monospaced font when you use a password field.
    //This fixes to keep fonts and styles consistant.
    private void fixPasswordFont(){
        EditText password = (EditText) findViewById(R.id.studentPIN);
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
            task.execute();

            /*
            Intent loggedInIntent = new Intent(LoginActivity.this, TransactionsActivity.class);
            LoginActivity.this.startActivity(loggedInIntent);
            LoginActivity.this.finish();*/
        }else{
            System.out.println("Nah");
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

    private void processData(String key, Object value) throws JSONException {
        switch(key){
            case "password":
                System.out.println("PASSWORD: " + (String)value);
                break;
            case "user_info":
                JSONObject x = new JSONObject((HashMap<String, String>) value);
                System.out.println("USERINFO");
        }
        //System.out.println(conv.values());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fixPasswordFont();
        setButtonListeners();
        Firebase.setAndroidContext(this);
    }

    private class LoginTask extends AsyncTask<String, String, String> {

        private String resp;

        public String makeRequest(String path, JSONObject params) throws Exception {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(path);
            JSONObject holder = params;
            StringEntity se = new StringEntity(holder.toString());

            System.out.println("SE:");
            String inputLine ;
            BufferedReader br = new BufferedReader(new InputStreamReader(se.getContent()));
            try {
                while ((inputLine = br.readLine()) != null) {
                    System.out.println(inputLine);
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
                json.put("student_number", "20562549");
                json.put("pin", "1234");
                System.out.println(json.toString());
                return makeRequest("https://watispend.herokuapp.com/waterloo/token", json);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resp = "error 1";
            } catch (IOException e) {
                e.printStackTrace();
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
                System.out.println(token);

                Firebase reference = new Firebase("https://watispend.firebaseio.com/students/");

                Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("Testing Authenticated");
                        Firebase reference = new Firebase("https://watispend.firebaseio.com/students/" + "20562549");
                        Query queryRef = reference.orderByValue();

                        queryRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                                try {
                                    processData(snapshot.getKey(), snapshot.getValue());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
