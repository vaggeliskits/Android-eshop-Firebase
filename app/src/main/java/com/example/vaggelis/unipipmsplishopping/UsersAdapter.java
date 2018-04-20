package com.example.vaggelis.unipipmsplishopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vaggelis on 22/02/17.
 */
//Addapter from the user

public class UsersAdapter extends ArrayAdapter<User> {
    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }


    /**
     * Takes *ONE* User instance and draws the UI
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // connect addapter with user_item layout
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }
        // Lookup view for data population
        TextView firstname = (TextView) convertView.findViewById(R.id.firstname);
        TextView lastname = (TextView) convertView.findViewById(R.id.lastname);
        // Populate the data into the template view using the data object
        firstname.setText(user.getFirstname());
        lastname.setText(user.getLastname());
        // Return the completed view to render on screen
        return convertView;
    }
}