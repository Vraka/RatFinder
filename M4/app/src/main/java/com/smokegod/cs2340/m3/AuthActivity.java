package com.smokegod.cs2340.m3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private Thread loginThread;
    private Handler loginHandler;
    private Runnable loginHandlerRunnable;
    private TextView usernameTV;
    private TextView passwordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        usernameTV = (TextView) findViewById(R.id.usernameEditTextAuth);
        passwordTV = (TextView) findViewById(R.id.passwordEditTextAuth);

        progress = new ProgressDialog(this);
        loginHandler = new Handler();
        loginHandlerRunnable = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (usernameTV.getEditableText().toString().equals("shane") &&
                                passwordTV.getEditableText().toString().equals("password")) {
                            Intent i = new Intent(AuthActivity.this, MapsActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            progress.hide();
                            Toast t = Toast.makeText(AuthActivity.this, "Wrong login information",
                                    Toast.LENGTH_LONG);
                            t.show();
                        }
                    }
                });
            }
        };

        loginThread = new Thread(new Runnable() {
            @Override
            public void run() {
                loginHandler.postDelayed(loginHandlerRunnable, 5000);
            }
        });
    }

    public void goToRegistration(View v) {
        Intent i = new Intent(AuthActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void attemptLogin(View v) {
        progress.setTitle("Attempting login");
        progress.setMessage("Logging-in is in-progress. Press your back button to cancel.");
        progress.setCancelable(true);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                loginHandler.removeCallbacksAndMessages(null);
//                            loginHandler.removeCallbacks(loginHandlerRunnable);
            }
        });
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        loginThread.run();
    }
}
