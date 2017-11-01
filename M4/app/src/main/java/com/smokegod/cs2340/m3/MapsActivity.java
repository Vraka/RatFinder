package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private long firstDate = -1;
    private long secondDate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String title = "U Dirty Rat";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Atlanta, Georgia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        log();
        InputStream inputStream = getResources().openRawResource(R.raw.rat_sightings);
        if (firstDate == -1 || secondDate == -1) {
            ArrayList<RatSighting> pointers = (new CSVFile(inputStream)).read(50);
            for (int i = 0; i < 50; i++) {
                // Add a marker
                try {
                    LatLng pointer = new LatLng(Double.parseDouble(pointers.get(i).getLatitude()),
                            Double.parseDouble(pointers.get(i).getLongitude()));
                    mMap.addMarker(new MarkerOptions().position(pointer)
                            .title(pointers.get(i).toString()));
                    if (i == 0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pointer));
                    }
                } catch (Exception e) {
                    Log.d("MapsActivity", e.toString());
                }
            }
        } else {
            loadSearch();
        }
    }

    public void attemptLogout(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MapsActivity.this, AuthActivity.class);
        startActivity(i);
        finish();
    }

    public void goToList(View v) {
        Intent i = new Intent(MapsActivity.this, ListActivity.class);
        startActivity(i);
    }

    public void goToInput(View v) {
        Intent i = new Intent(MapsActivity.this, InputActivity.class);
        startActivity(i);
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        Drawable drawable = menu.getItem(0).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            menu.getItem(0).setIcon(drawable);
        }
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            Intent i = new Intent(MapsActivity.this, FilterByDateActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getExtra(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            firstDate = getIntent().getLongExtra("FIRST_DATE", -1);
            secondDate = getIntent().getLongExtra("SECOND_DATE", -1);
        } else {
            firstDate = savedInstanceState.getLong("FIRST_DATE", -1);
            secondDate = savedInstanceState.getLong("SECOND_DATE", -1);
        }
        log();
        if (!(firstDate == -1) || !(secondDate == -1)) {
            if (mMap != null) {
                mMap.clear();
                loadSearch();
            }
        }


    }

    private void loadSearch() {
        new Thread() {
            @Override
            public void run() {
                InputStream inputStream = getResources().openRawResource(R.raw.rat_sightings);
                log();
                ArrayList<RatSighting> pointers = (new CSVFile(inputStream))
                        .search(firstDate, secondDate);
                if (!(pointers.size() > 200)) {
                    for (int i = 0; i < pointers.size(); i++) {
                        // Add a marker
                        try {
                            LatLng pointer = new LatLng(Double.parseDouble(pointers.get(i).getLatitude()),
                                    Double.parseDouble(pointers.get(i).getLongitude()));
                            mMap.addMarker(new MarkerOptions().position(pointer)
                                    .title(pointers.get(i).toString()));
                        } catch (Exception e) {
                            Log.d("MapsActivity", e.toString());
                        }
                    }
                } else {
                    for (int i = 0; i < 200; i++) {
                        // Add a marker
                        try {
                            LatLng pointer = new LatLng(Double.parseDouble(pointers.get(i).getLatitude()),
                                    Double.parseDouble(pointers.get(i).getLongitude()));
                            mMap.addMarker(new MarkerOptions().position(pointer)
                                    .title(pointers.get(i).toString()));
                        } catch (Exception e) {
                            Log.d("MapsActivity", e.toString());
                        }
                    }
                }
            }
        }.run();
    }

    private void log() {
        Log.d("MapsActivity", "First Date: " + firstDate
                + " Second Date: " + secondDate);
    }
}
