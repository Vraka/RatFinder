package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void attemptLogout(View v) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("TOKEN", null);
        editor.putString("USERNAME", null);
        editor.commit();
        HTTPPostReq.setToken(null);
        HTTPPostReq.setUsername(null);
        Intent i = new Intent(SettingsActivity.this, AuthActivity.class);
        startActivity(i);
        finish();
    }
}
