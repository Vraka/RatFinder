package com.smokegod.cs2340.m3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Vraka on 12/5/2017.
 */

public class FilterZipActivity extends Activity {

    private EditText ziptext;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_filter_zip);
        ziptext = (EditText) findViewById(R.id.zipsearch);
    }

    public void sortZip(View v) {
        Intent i = new Intent(FilterZipActivity.this, MapsActivity.class);
        i.putExtra("zip", ziptext.getEditableText().toString());
        startActivity(i);
    }

    public void cancel(View v) {
        Intent i = new Intent(FilterZipActivity.this, MapsActivity.class);
        startActivity(i);
    }

}
