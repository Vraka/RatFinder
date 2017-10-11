package com.smokegod.cs2340.m3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Vraka on 10/10/2017.
 */

public class ListActivity extends Activity implements AbsListView.OnScrollListener{

    private ListView lv;
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    ArrayList<RatSighting> scoreList;
    CSVFile csvFile;
    RatArrayAdapter arrayAdapter;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_list);

        lv = (ListView) findViewById(R.id.ratlist);
        lv.setOnScrollListener(this);
        lv.setOnItemClickListener(LstListener);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        //List<String> your_array_list = new ArrayList<String>();
        //your_array_list.add("foo");
        //your_array_list.add("bar");
        InputStream inputStream = getResources().openRawResource(R.raw.rat_sightings);
        csvFile = new CSVFile(inputStream);
        scoreList = csvFile.read(20);


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        arrayAdapter = new RatArrayAdapter(
                this,
                R.layout.adapter_view_layout,
                scoreList);

        lv.setAdapter(arrayAdapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            Log.d("Page", "" + currentPage);
            InputStream inputStream = getResources().openRawResource(R.raw.rat_sightings);
            csvFile = new CSVFile(inputStream);
            arrayAdapter.addAll(csvFile.read(currentPage + 1, 20));
            loading = true;
        }
    }

    private AdapterView.OnItemClickListener LstListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            @SuppressWarnings("unchecked")
            Object ob= lv.getItemAtPosition(position);

            Log.i("RatList", "DisplayID:" + ob.toString());
            //calling the next activity
            Intent intent = new Intent(ListActivity.this, SightingActivity.class);
            intent.putExtra("RatSighting", scoreList.get(position));
            startActivity(intent);

        }
    };
}