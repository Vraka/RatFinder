package com.smokegod.cs2340.m3;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Vraka on 10/10/2017.
 */

public class RatArrayAdapter extends ArrayAdapter<RatSighting> {

    private Context mContext;
    int mResource;
    int size;



    public RatArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<RatSighting> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        size = objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String key = getItem(position).getKey();
        String date = getItem(position).getDate();
        String loc_type = getItem(position).getLoc_type();
        String zip = getItem(position).getZip();
        String address = getItem(position).getAddress();
        String city = getItem(position).getCity();
        String borough = getItem(position).getBorough();
        String latitude = getItem(position).getLatitude();
        String longitude = getItem(position).getLongitude();

        RatSighting sighting = new RatSighting(key,date,loc_type,zip,address,city,borough,latitude,longitude);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvId = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvDate = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvCity = (TextView) convertView.findViewById(R.id.textView3);

        tvId.setText(key);
        tvDate.setText(date);
        tvCity.setText(city);

        return convertView;
    }
}
