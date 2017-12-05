package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
    }

    public void checkPassword(View v) {
        EditText newContactText = (EditText)findViewById(R.id.usernameEditTextRegister);
        String newContact = newContactText.getText().toString();
        EditText passwordtext = (EditText)findViewById(R.id.passwordEditTextRegister);
        String password = passwordtext.getText().toString();
        EditText confirmpasstext = (EditText)findViewById(R.id.password2EditTextRegister);
        String confirmpass = confirmpasstext.getText().toString();

        if(password.equalsIgnoreCase(confirmpass)) {
            if(HTTPPostReq.changePassword(newContact,password)) {
                Toast t = Toast.makeText(ChangePassActivity.this, "Successfully changed password to " + newContact,
                        Toast.LENGTH_LONG);
                t.show();
                Intent i = new Intent(ChangePassActivity.this, MapsActivity.class);
                startActivity(i);
            } else {
                Toast t = Toast.makeText(ChangePassActivity.this, "Error",
                        Toast.LENGTH_LONG);
                t.show();
            }
        } else {
            Toast t = Toast.makeText(ChangePassActivity.this, "Passwords much match",
                    Toast.LENGTH_LONG);
            t.show();
        }

    }
}
