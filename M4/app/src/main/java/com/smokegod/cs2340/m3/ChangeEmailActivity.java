package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
    }

    public void checkPassword(View v) {
        EditText newContactText = (EditText)findViewById(R.id.emailEditTextRegister);
        String newContact = newContactText.getText().toString();
        EditText passwordtext = (EditText)findViewById(R.id.passwordEditTextRegister);
        String password = passwordtext.getText().toString();
        EditText confirmpasstext = (EditText)findViewById(R.id.password2EditTextRegister);
        String confirmpass = confirmpasstext.getText().toString();

        if(password.equalsIgnoreCase(confirmpass)) {
            if(HTTPPostReq.changeContactInfo(newContact,password)) {
                Toast t = Toast.makeText(ChangeEmailActivity.this, "Successfully changed contact info to " + newContact,
                        Toast.LENGTH_LONG);
                t.show();
                Intent i = new Intent(ChangeEmailActivity.this, MapsActivity.class);
                startActivity(i);
            } else {
                Toast t = Toast.makeText(ChangeEmailActivity.this, "Error",
                        Toast.LENGTH_LONG);
                t.show();
            }
        } else {
            Toast t = Toast.makeText(ChangeEmailActivity.this, "Passwords much match",
                    Toast.LENGTH_LONG);
            t.show();
        }

    }
}
