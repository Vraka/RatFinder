package com.smokegod.cs2340.m3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Vraka on 12/5/2017.
 */

public class FilterBoroughActivity extends Activity{
    private Spinner boroughSpinner;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_filter_borough);

        boroughSpinner = (Spinner) findViewById(R.id.boroughSpinner);
        String[] boroughSpinnerItems = new String[]{"MANHATTAN",
                "STATEN ISLAND",
                "QUEENS",
                "BROOKLYN",
                "BRONX"};
        ArrayAdapter<String> borughAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                boroughSpinnerItems);
        boroughSpinner.setAdapter(borughAdapter);
        boroughSpinner.setSelection(0);
    }

    public void sortBorough(View v) {

        Intent i = new Intent(FilterBoroughActivity.this, MapsActivity.class);
        i.putExtra("borough", boroughSpinner.getSelectedItem().toString());
        startActivity(i);
    }

    public void cancel(View v) {
        Intent i = new Intent(FilterBoroughActivity.this, MapsActivity.class);
        startActivity(i);
    }
}
