package com.example.apiexample;

import android.content.Context;
import android.os.AsyncTask;
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

    public AddLocationListViewAdapter(Context context, ArrayList<ForecastInformation> customItems) {
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

        ForecastInformation forecastInformation = (ForecastInformation) getItem(position);

        TextView regionNameView = (TextView) convertView.findViewById(R.id.region_name);

        ImageView locationStateImageView = (ImageView) convertView.findViewById(R.id.drawstate);

        TextView locationStateTextView = (TextView) convertView.findViewById(R.id.drawtext);

        int PTY = forecastInformation.getPrecipitationForm().charAt(0) - '0';
        if (PTY > 0) {
            locationStateTextView.setText("비 옴");
        } else {
            locationStateTextView.setText("비 안 옴");
        }

        int locationId = forecastInformation.getLocationId();

        new AsyncTask<Void,Void,String>() {
            @Override
            protected void onPostExecute(String result) {
                regionNameView.setText(result);
            }

            @Override
            protected String doInBackground(Void... voids) {
                return SQLiteDatabaseManager.getInstance().getMyRegionData(locationId).toString();
            }
        }.execute();

        return convertView;
    }
}
