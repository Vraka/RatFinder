package com.smokegod.cs2340.m3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Vraka on 12/5/2017.
 */

public class FilterMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void sortByDate(View v) {
        Intent i = new Intent(FilterMainActivity.this, FilterDateActivity.class);
        startActivity(i);
    }

    public void sortByBorough(View v) {
        Intent i = new Intent(FilterMainActivity.this, FilterBoroughActivity.class);
        startActivity(i);
    }

    public void sortByZip(View v) {
        Intent i = new Intent(FilterMainActivity.this, FilterZipActivity.class);
        startActivity(i);
    }

    public void sortByLoc(View v) {
        Intent i = new Intent(FilterMainActivity.this, FilterLocActivity.class);
        startActivity(i);
    }

    public void cancel(View v) {
        Intent i = new Intent(FilterMainActivity.this, MapsActivity.class);
        startActivity(i);
    }

}
