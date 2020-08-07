package com.example.apiexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter {

    public CustomListViewAdapter(Context context, ArrayList customItems) {
        super(context, 0, customItems);
    }

    public CustomListViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_view_item, parent, false);
        }

        LocationInfo customData = (LocationInfo) getItem(position);

        TextView regionNameView = (TextView) convertView.findViewById(R.id.region_name);

        regionNameView.setText(((LocationInfo)customData).toString());

        return convertView;
    }
}
