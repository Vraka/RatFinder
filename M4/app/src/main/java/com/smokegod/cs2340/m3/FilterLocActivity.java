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

public class FilterLocActivity extends Activity {
    private Spinner locationTypeSpinner;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_filter_loctype);

        locationTypeSpinner = (Spinner) findViewById(R.id.locationTypeSpinner);
        String[] locationTypeSpinnerItems = new String[]{"1-2 Family Dwelling",
                "3+ Family Apt. Building",
                "3+ Family Mixed Use Building",
                "Commercial Building",
                "Vacant Lot",
                "Construction Site",
                "Hospital",
                "Catch Basin/Sewer"};
        ArrayAdapter<String> locationTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                locationTypeSpinnerItems);
        locationTypeSpinner.setAdapter(locationTypeAdapter);
        locationTypeSpinner.setSelection(0);
    }

    public void sortLocType(View v) {
        Intent i = new Intent(FilterLocActivity.this, MapsActivity.class);
        i.putExtra("loc_type", locationTypeSpinner.getSelectedItem().toString());
        startActivity(i);
    }

    public void cancel(View v) {
        Intent i = new Intent(FilterLocActivity.this, MapsActivity.class);
        startActivity(i);
    }
}
