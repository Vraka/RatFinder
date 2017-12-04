package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText usernameET, passwordET, password2ET;
//    private ToggleButton adminTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Old Firebase code
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    //User is signed in
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference myRef = database.getReference();
//                    if (adminTB.isChecked()) {
//                        myRef.child(user.getUid()).child("Admin").setValue(true);
//                    } else {
//                        myRef.child(user.getUid()).child("Admin").setValue(false);
//                    }
//                    Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
//                    startActivity(i);
//                    finish();
//                } else {
//                    //User is signed out
//                }
//            }
//        };

//        adminTB = (ToggleButton) findViewById(R.id.adminToggleRegister);
        usernameET = (EditText) findViewById(R.id.usernameEditTextRegister);
        passwordET = (EditText) findViewById(R.id.passwordEditTextRegister);
        password2ET = (EditText) findViewById(R.id.password2EditTextRegister);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
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
        int result = HTTPPostReq.register(usernameET.getEditableText().toString(),
                passwordET.getEditableText().toString(),
                usernameET.getEditableText().toString(),
                false);
        Toast t = null;

        switch (result) {
            case (0):
                Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
                break;
            case (1):
                t = Toast.makeText(RegisterActivity.this, "Username/Email already taken",
                        Toast.LENGTH_LONG);
                t.show();
                break;
            default:
                t = Toast.makeText(RegisterActivity.this, "Server Error",
                        Toast.LENGTH_LONG);
                t.show();
        }

        //Old Firebase code
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
