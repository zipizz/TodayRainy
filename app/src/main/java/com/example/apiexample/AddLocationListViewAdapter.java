package com.example.apiexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AddLocationListViewAdapter extends ArrayAdapter {

    public AddLocationListViewAdapter(Context context, ArrayList<LocationInfo> customItems) {
        super(context, 0, customItems);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_location_custom_view_item, parent, false);
        }

        LocationInfo locationInfo = (LocationInfo) getItem(position);
        TextView regionNameView = (TextView) convertView.findViewById(R.id.region_name);
        ImageView rainyStateImageView = (ImageView) convertView.findViewById(R.id.drawstate);
        TextView rainyStateTextView = (TextView) convertView.findViewById(R.id.drawtext);
        int locationId = locationInfo.getLocationId();

        if (position == 0) {
            regionNameView.setText("GPS 현재 위치");
        } else {
            regionNameView.setText(locationInfo.toString());
        }

        ForecastInformation forecastInformation = SQLiteDatabaseManager.getInstance().getForcastInformation(locationId);
        if (forecastInformation != null) {
            if (forecastInformation.isRainy()){
                rainyStateTextView.setText("비 옴");
                rainyStateImageView.setImageResource(R.drawable.ic_launcher_foreground);
            } else{
                rainyStateTextView.setText("비 안 옴");
                rainyStateImageView.setImageResource(R.drawable.ic_check_black_24dp);
            }
        }

        return convertView;
    }
}
