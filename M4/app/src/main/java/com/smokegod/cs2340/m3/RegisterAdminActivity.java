package com.smokegod.cs2340.m3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Vraka on 12/5/2017.
 */

public class RegisterAdminActivity extends Activity {

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register_admin);
    }

    public void registerAdmin() {
        EditText usernametext = (EditText)findViewById(R.id.login_name);
        EditText contacttext = (EditText)findViewById(R.id.contact_info);
        EditText passwordtext = (EditText)findViewById(R.id.password);
        EditText confirmpasstext = (EditText)findViewById(R.id.confirmpassword);

        String login_name = usernametext.getText().toString();
        String contact_info = contacttext.getText().toString();
        String password = passwordtext.getText().toString();
        String confirmpassword = confirmpasstext.getText().toString();

        if(password.equals(confirmpassword)) {
            if(password.length() > 5) {
                HTTPPostReq.createAdmin(login_name, password, contact_info, true);
            } else {
                Toast t = Toast.makeText(RegisterAdminActivity.this, "Password must be greater than 5 characters",
                        Toast.LENGTH_LONG);
                t.show();
            }
        } else {
            Toast t = Toast.makeText(RegisterAdminActivity.this, "Passwords do not match",
                    Toast.LENGTH_LONG);
            t.show();
        }

    }
    public void cancel(View v) {
        finish();
    }

}
