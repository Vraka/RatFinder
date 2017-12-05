package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HTTPPostReq();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String token = pref.getString("TOKEN", null);
                String username = pref.getString("LOGIN_NAME", null);
                if (token != null && username != null) {
                    HTTPPostReq.setToken(token);
                    HTTPPostReq.setUsername(username);
                    Intent i = new Intent(SplashActivity.this, MapsActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    HTTPPostReq.setToken(null);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(SplashActivity.this, AuthActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                        }
                    }, 5000);
                }
            }
        }).run();
    }
}
