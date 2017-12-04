package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import java.util.Calendar;

public class FilterByDateActivity extends AppCompatActivity {

    private boolean firstChange;
    private CalendarView calendar;
    private long firstDate, secondDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_date);
        calendar = (CalendarView) findViewById(R.id.filterByDateCalendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                Log.d("MapsActivity", "onSelectedDayChange: " + c.getTimeInMillis());
                collectDate(c.getTimeInMillis());
            }
        });
        firstDate = calendar.getDate();
        secondDate = firstDate;
        firstChange = true;
        changeHeader();
    }

    private void collectDate(long l) {
        if (firstChange) {
            firstDate = l;
        } else {
            secondDate = l;
        }
    }

    private void changeHeader() {
        if (firstChange) {
            String title = "From Date";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
        } else {
            String title = "To Date";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
            invalidateOptionsMenu();
            calendar.setDate(System.currentTimeMillis(), true, true);
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (firstChange) {
            getMenuInflater().inflate(R.menu.filter_by_date_menu1, menu);
        } else {
            getMenuInflater().inflate(R.menu.filter_by_date_menu2, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        collectDate();
        if (id == R.id.action_next) {
            firstChange = false;
            changeHeader();
        } else {
            Intent i = new Intent(FilterByDateActivity.this, MapsActivity.class);
            Log.d("MapsActivity", "Filter: " + firstDate + " " + secondDate);
            i.putExtra("FIRST_DATE", firstDate);
            i.putExtra("SECOND_DATE", secondDate);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
