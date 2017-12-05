package com.smokegod.cs2340.m3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yoyor on 12/5/2017.
 */

public class UsersListAdapter extends BaseAdapter {
    private List<User> users;
    private LayoutInflater inflater;
    private Context context;

    public UsersListAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.user_control_item_layout, null);

        final User curr = users.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.userControlNameTV);
        final ImageView lock = (ImageView) convertView.findViewById(R.id.userControlLockIV);

            name.setText(curr.get_contact());
        if (!curr.is_isLocked()) {
            lock.setImageDrawable(context.getResources().getDrawable(R.drawable.unlocked_icon));
        }

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr.is_isLocked()) {
                    lock.setImageDrawable(context.getResources().getDrawable(R.drawable.unlocked_icon));
                    HTTPPostReq.unlockUser(curr.get_id());
                } else {
                    lock.setImageDrawable(context.getResources().getDrawable(R.drawable.locked_icon));
                    HTTPPostReq.lockUser(curr.get_id());
                }
            }
        });

        return convertView;
    }
}
