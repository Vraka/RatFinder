package com.smokegod.cs2340.m3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Vraka on 10/10/2017.
 */

public class SightingActivity extends Activity {

    RatSighting sighting;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_sighting);
        sighting = (RatSighting)this.getIntent().getSerializableExtra("RatSighting");
        Log.d("Sighting", sighting.getKey());
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.details);
        TextView tvkey = (TextView) rl.findViewById(R.id.uniquekey);
        TextView tvdate = (TextView) rl.findViewById(R.id.date);
        TextView tvloctype = (TextView) rl.findViewById(R.id.loctype);
        TextView tvzip = (TextView) rl.findViewById(R.id.zip);
        TextView tvaddress = (TextView) rl.findViewById(R.id.address);
        TextView tvcity = (TextView) rl.findViewById(R.id.city);
        TextView tvborough = (TextView) rl.findViewById(R.id.borough);
        TextView tvlat = (TextView) rl.findViewById(R.id.latitude);
        TextView tvlong = (TextView) rl.findViewById(R.id.longitude);

        tvkey.append(" " + sighting.getKey());
        tvdate.append(" " + sighting.getDate());
        tvloctype.append(" " + sighting.getLoc_type());
        tvzip.append(" " + sighting.getZip());
        tvaddress.append(" " + sighting.getAddress());
        tvcity.append(" " + sighting.getCity());
        tvborough.append(" " + sighting.getBorough());
        tvlat.append(" " + sighting.getLatitude());
        tvlong.append(" " + sighting.getLongitude());
    }
}
