package nathaniel.watispend;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
// Login Activity java file.
public class LoginActivity extends AppCompatActivity {

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

    private void onLoginClick(View view){
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
            Intent loggedInIntent = new Intent(LoginActivity.this, TransactionsActivity.class);
            LoginActivity.this.startActivity(loggedInIntent);
            LoginActivity.this.finish();
        }else{
            System.out.println("Nah");
        }
    }

    private void setButtonListeners(){
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onLoginClick(v);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fixPasswordFont();
        setButtonListeners();
    }
}
