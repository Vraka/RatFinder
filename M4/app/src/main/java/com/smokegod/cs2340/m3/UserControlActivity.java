package com.smokegod.cs2340.m3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class UserControlActivity extends AppCompatActivity {

    List<User> users;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_control);

        users = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.userControlLV);
        adapter = new UsersListAdapter(this, users);
        listView.setAdapter(adapter);
        ImageView addIV = (ImageView) findViewById(R.id.userControlAddAdminIV);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserControlActivity.this, RegisterAdminActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        users.clear();
        users.addAll(HTTPPostReq.getUsers());
        adapter.notifyDataSetChanged();
    }


}
