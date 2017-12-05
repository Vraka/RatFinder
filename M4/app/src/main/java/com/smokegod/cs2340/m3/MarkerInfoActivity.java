package com.smokegod.cs2340.m3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MarkerInfoActivity extends AppCompatActivity {
    String key, date, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra(savedInstanceState);
        setContentView(R.layout.activity_marker_info);

        TextView keyTV = (TextView) findViewById(R.id.markerinfo_keyTV);
        TextView dateTV = (TextView) findViewById(R.id.markerinfo_dateTV);
        TextView addressTV = (TextView) findViewById(R.id.markerinfo_addressTV);

        addressTV.setText(address);
        dateTV.setText(date);
        keyTV.setText(key);
    }

    private void getExtra(Bundle savedInstanceState) {
        String markerString;
        if (savedInstanceState == null) {
            markerString = getIntent().getStringExtra("MARKER_STRING");
        } else {
            markerString = savedInstanceState.getString("MARKER_STRING");
        }
        if (markerString != null || !markerString.isEmpty()) {
            String[] arr = markerString.split("\\n");
            for (int i = 0; i < arr.length; i++) {
               String[] arr2 = arr[i].split(": ");
                switch(i) {
                    case (0):
                        key = arr2[1];
                        break;
                    case (1):
                        date = arr2[1];
                        break;
                    case (2):
                        address = arr2[1];
                        break;
                    default:
                        break;
                }
            }
        }
        if (date == null || key == null || address == null) {
            finish();
        }

    }
}
