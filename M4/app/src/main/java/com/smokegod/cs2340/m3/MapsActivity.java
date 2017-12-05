package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private long firstDate = -1;
    private long secondDate = -1;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HTTPPostReq.getToken() == null) {
            Intent i = new Intent(MapsActivity.this, AuthActivity.class);
            startActivity(i);
            finish();
        }
        //getExtra(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.maps_navigation);
        View v = navigationView.getHeaderView(0);
        TextView nav_displayName = (TextView) v.findViewById(R.id.nav_user_name_display1);
        nav_displayName.setText(HTTPPostReq.getUsername());

        drawer = (DrawerLayout) findViewById(R.id.maps_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        Log.d("TEXTVIEWE", "about to call");
        drawer.closeDrawers();
        navigationView.setNavigationItemSelectedListener(this);
        ArrayList<RatSighting> list = HTTPPostReq.getSightings(20,0);
        Log.d("DATABASE", "" + list.size());
        for(int i = 0; i < list.size(); i++) {
            Log.d("DATABASE", list.get(i).toString());
        }
//        String title = "U Dirty Rat";
//        SpannableString s = new SpannableString(title);
//        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        getSupportActionBar().setTitle(s);
//        drawer = (DrawerLayout) findViewById(R.id.maps_drawer);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, getSupportActionBar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        navigToolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


//        toggle.syncState();

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
//        log();
//        InputStream inputStream = getResources().openRawResource(R.raw.rat_sightings);
        ArrayList<RatSighting> pointers = new ArrayList<>();
        if (getIntent().hasExtra("start_date") || getIntent().hasExtra("end_date")) {
            Log.d("FILTER", "Date: " + getIntent().getStringExtra("start_date") + " " +
                    getIntent().getStringExtra("end_date"));
            pointers = HTTPPostReq.sort("date", getIntent().getStringExtra("start_date") + ","
                    + getIntent().getStringExtra("end_date"), 50, 0);
        } else if(getIntent().hasExtra("borough")) {
            Log.d("FILTER", "Borough: " + getIntent().getStringExtra("borough"));
            pointers = HTTPPostReq.sort("borough", getIntent().getStringExtra("borough"), 50, 0);
        } else if(getIntent().hasExtra("zip")) {
            Log.d("FILTER", "Zip: " + getIntent().getStringExtra("zip"));
            pointers = HTTPPostReq.sort("zip", getIntent().getStringExtra("zip"), 50, 0);
        } else if(getIntent().hasExtra("loc_type")) {
            Log.d("FILTER", "Loc_type: " + getIntent().getStringExtra("loc_type"));
            pointers = HTTPPostReq.sort("loc_type", getIntent().getStringExtra("loc_type"), 50, 0);
        } else {
            Log.d("FILTER", "none");
            pointers = HTTPPostReq.getSightings(50, 0);
        }

//      (new CSVFile(inputStream)).read(50);
        for (int i = 0; i < pointers.size(); i++) {
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
        //} else {
        //    loadSearch();
        //}

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent i = new Intent(MapsActivity.this, MarkerInfoActivity.class);
                i.putExtra("MARKER_STRING", marker.getTitle());
                startActivity(i);
                return true;
            }

        });

        HashMap map1 = HTTPPostReq.countBorough();
        HashMap map2 = HTTPPostReq.countLocationType();
        for (Object key : map1.keySet()) {
            Log.d("Stats", key + ": " + map1.get(key));
        }

        for (Object key : map2.keySet()) {
            Log.d("Stats", key + ": " + map2.get(key));
        }
    }


    public void attemptLogout(View v) {
//        FirebaseAuth.getInstance().signOut();
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
            Intent i = new Intent(MapsActivity.this, FilterMainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getExtra(Bundle savedInstanceState) {

    }

//    private void getExtra(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
//            firstDate = getIntent().getLongExtra("FIRST_DATE", -1);
//            secondDate = getIntent().getLongExtra("SECOND_DATE", -1);
//        } else {
//            firstDate = savedInstanceState.getLong("FIRST_DATE", -1);
//            secondDate = savedInstanceState.getLong("SECOND_DATE", -1);
//        }
////        log();
//        if (!(firstDate == -1) || !(secondDate == -1)) {
//            if (mMap != null) {
//                loadSearch();
//            }
//        }
//
//
//    }

    private void loadSearch() {
        new Thread() {
            @Override
            public void run() {
                InputStream inputStream = getResources().openRawResource(R.raw.rat_sightings);
                log();
                final ArrayList<RatSighting> pointers = (new CSVFile(inputStream))
                        .search(firstDate, secondDate);
                mMap.clear();
                if (!(pointers.size() > 200)) {
                    for (int i = 0; i < pointers.size(); i++) {
                        // Add a marker
                        try {
                            final LatLng pointer = new LatLng(Double.parseDouble(pointers.get(i).getLatitude()),
                                    Double.parseDouble(pointers.get(i).getLongitude()));
                            final int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMap.addMarker(new MarkerOptions().position(pointer)
                                            .title(pointers.get(finalI).toString()));
                                }
                            });
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
        }
        .run();

        //Temp fix for m8 start
//        mMap.clear();
//        if (!(firstDate >= Long.valueOf("1487221200000"))) {
//            InputStream inputStream = getResources().openRawResource(R.raw.rat_sightings);
//            Random rand = new Random();
//            int n = (rand.nextInt(50) + 51) * 2;
//
//            ArrayList<RatSighting> pointers = (new CSVFile(inputStream)).read(n);
//            for (int i = 0; i < n; i++) {
//                // Add a marker
//                try {
//                    LatLng pointer = new LatLng(Double.parseDouble(pointers.get(i).getLatitude()),
//                            Double.parseDouble(pointers.get(i).getLongitude()));
//                    mMap.addMarker(new MarkerOptions().position(pointer)
//                            .title(pointers.get(i).toString()));
//                    if (i == 0) {
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pointer));
//                    }
//                } catch (Exception e) {
//                    Log.d("MapsActivity", e.toString());
//                }
//            }
//
//        }
        //Temp fix for m8 end
    }

    private void log() {
        Log.d("MapsActivity", "First Date: " + firstDate
                + " Second Date: " + secondDate);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.map_nav_menu_item_stats) {
            Intent i = new Intent(MapsActivity.this, StatsActivity.class);
            startActivity(i);
        } else if (id == R.id.map_nav_menu_item_uc) {
            Intent i = new Intent(MapsActivity.this, UserControlActivity.class);
            startActivity(i);
        } else if (id == R.id.map_nav_menu_item_home) {
            drawer.closeDrawer(Gravity.LEFT);
        } else if (id == R.id.map_nav_menu_item_settings) {
            Intent i = new Intent(MapsActivity.this, SettingsActivity.class);
            startActivity(i);
        }
        return true;
    }
}
