package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText usernameET, passwordET, password2ET;
    private ToggleButton adminTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    if (adminTB.isChecked()) {
                        myRef.child(user.getUid()).child("Admin").setValue(true);
                    } else {
                        myRef.child(user.getUid()).child("Admin").setValue(false);
                    }
                    Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    //User is signed out
                }
            }
        };

        adminTB = (ToggleButton) findViewById(R.id.adminToggleRegister);
        usernameET = (EditText) findViewById(R.id.usernameEditTextRegister);
        passwordET = (EditText) findViewById(R.id.passwordEditTextRegister);
        password2ET = (EditText) findViewById(R.id.password2EditTextRegister);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void checkPassword(View v) {
        if (!passwordET.getEditableText().toString().equals(password2ET.getEditableText().toString())) {
            Toast t = Toast.makeText(RegisterActivity.this, "Passwords do not match",
                    Toast.LENGTH_LONG);
            t.show();
        } else if(!usernameET.getEditableText().toString().contains("@") ||
                !usernameET.getEditableText().toString().contains(".")) {
            Toast t = Toast.makeText(RegisterActivity.this, "Username/Email does not appear valid",
                    Toast.LENGTH_LONG);
            t.show();
        } else {
            attemptRegister();
        }
    }

    private void attemptRegister() {
        HttpURLConnection client = null;
        try {
            URL url = new URL("https://desolate-taiga-94108.herokuapp.com/api/register");
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("login_name","username");
            client.setRequestProperty("password","password");
            client.setRequestProperty("contact_info","test@test.com");
            client.setRequestProperty("isAdmin","true");
            client.setDoOutput(true);
            BufferedOutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            String output = "";
            outputPost.write(output.getBytes());
            System.out.println(output);
            outputPost.flush();
            outputPost.close();
        } catch (Exception e) {

        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }


        //mAuth.createUserWithEmailAndPassword(usernameET.getEditableText().toString(),
        //        passwordET.getEditableText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        //   @Override
        //    public void onComplete(@NonNull Task<AuthResult> task) {
        //        if (!task.isSuccessful()) {
        //            Toast t = Toast.makeText(RegisterActivity.this, "There seems to be an error " +
        //                            "with your registration information! Maybe try a longer password!",
        //                    Toast.LENGTH_LONG);
        //            t.show();
        //        }
        //    }
        //});
    }
}
