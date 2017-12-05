package com.smokegod.cs2340.m3;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * Created by Vraka on 10/17/2017.
 */

public class InputActivity extends Activity {

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Spinner locationTypeSpinner, boroughSpinner;
    private EditText addressET, cityET, zipET;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_input);

        if (ContextCompat.checkSelfPermission(InputActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(InputActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(InputActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            checkLocation();
        }

        addressET = (EditText) findViewById(R.id.addressET);
        cityET = (EditText) findViewById(R.id.cityET);
        zipET = (EditText) findViewById(R.id.zipET);

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
        locationTypeSpinner.setSelection(0);
        boroughSpinner.setSelection(0);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Loc", "permGranted");
                    checkLocation();

                } else {
                    Log.d("Loc", "permFailed");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other "case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkLocation() throws SecurityException {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    Log.d("Loc", location.toString());
                } else {
                    Log.d("Loc", "Failed");
                }
            }
        });
    }

    public void goToMap(View v) {
        finish();
    }

    public void submit(View v) {
        if (!addressET.getEditableText().toString().isEmpty() ||
                !cityET.getEditableText().toString().isEmpty() ||
                !zipET.getEditableText().toString().isEmpty()) {
            String address = addressET.getEditableText().toString() + ", " +
                    cityET.getEditableText().toString() + zipET.getEditableText().toString();
            String[] latlong = getLatLong(address);
            if (latlong == null) {
                Toast t = Toast.makeText(this, "Cant find latitude or longitude using that address", Toast.LENGTH_LONG);
                t.show();
            }

            RatSighting rat = new RatSighting("", "", locationTypeSpinner.getSelectedItem().toString(),
                    zipET.getEditableText().toString(), addressET.getEditableText().toString(),
                    cityET.getEditableText().toString(), boroughSpinner.getSelectedItem().toString(),
                    latlong[0], latlong[1]);

            HTTPPostReq.addSighting(rat);
            finish();

        } else {
            Toast t = Toast.makeText(this, "None of the input should be empty", Toast.LENGTH_LONG);
            t.show();
        }
    }

    private String[] getLatLong(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        String[] result = new String[2];

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address == null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            result[0] = String.valueOf((double) (location.getLatitude() * 1E6));
            result[1] = String.valueOf((double) (location.getLongitude() * 1E6));

            return result;
        } catch (Exception e) {
            Log.d("InputActivity", e.toString());
        }
        return null;
    }
}
