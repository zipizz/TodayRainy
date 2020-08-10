package com.example.apiexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class AddLocationListViewAdapter extends ArrayAdapter {

    private AddLocationActivity parentActivity = null;

    public AddLocationListViewAdapter(Context context, ArrayList<LocationInfo> customItems, AddLocationActivity parentActivity) {
        super(context, 0, customItems);
        this.parentActivity = parentActivity;
    }

    private class ViewHolder {
        TextView regionNameView;
        ImageView rainyStateImageView;
        TextView rainyStateTextView;
        ConstraintLayout rainyStateLayout;
        int position;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_location_custom_view_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.regionNameView = (TextView) convertView.findViewById(R.id.region_name);
            viewHolder.rainyStateImageView = (ImageView) convertView.findViewById(R.id.drawstate);
            viewHolder.rainyStateTextView = (TextView) convertView.findViewById(R.id.drawtext);
            viewHolder.rainyStateLayout = (ConstraintLayout) convertView.findViewById(R.id.custom_image_group);
            ConstraintLayout.LayoutParams cl = (ConstraintLayout.LayoutParams) viewHolder.rainyStateLayout.getLayoutParams();
            cl.rightMargin = (int) Utils.convertDpToPixel(Constant.EDIT_MODE_RAINY_STATE_MARGIN_RIGHT_AFTER_MOVE_DP, parentActivity);
            viewHolder.rainyStateLayout.setLayoutParams(cl);
            viewHolder.position = position;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LocationInfo locationInfo = (LocationInfo) getItem(viewHolder.position);
        int locationId = locationInfo.getLocationId();



        if (viewHolder.position == 0) {
            viewHolder.regionNameView.setText("GPS 현재 위치");
        } else {
            if(parentActivity.isEditButtonClickedAtLeastOnce()) {
                ConstraintLayout.LayoutParams cl2 = (ConstraintLayout.LayoutParams) viewHolder.rainyStateLayout.getLayoutParams();
                cl2.rightMargin = (int) Utils.convertDpToPixel(Constant.EDIT_MODE_RAINY_STATE_MARGIN_RIGHT_AFTER_MOVE_DP + Constant.EDIT_MODE_RAINY_STATE_LAYOUT_TRANSITION_DISTANCE_DP, parentActivity);
                viewHolder.rainyStateLayout.setLayoutParams(cl2);

                System.out.println("my app test position : " + viewHolder.position);
                viewHolder.regionNameView.setText(locationInfo.toString());
                TranslateAnimation anim = new TranslateAnimation
                        (Utils.convertDpToPixel(Constant.EDIT_MODE_TEXT_TRANSITION_DISTANCE_DP, parentActivity),   // fromXDelta
                                0,  // toXDelta
                                0,    // fromYDelta
                                0);// toYDelta
                anim.setDuration(Constant.EDIT_MODE_TEXT_TRANSITION_DURATION);
                anim.setFillAfter(true);
                viewHolder.regionNameView.startAnimation(anim);

                TranslateAnimation anim2 = new TranslateAnimation
                        (0,   // fromXDelta
                                Utils.convertDpToPixel(Constant.EDIT_MODE_RAINY_STATE_LAYOUT_TRANSITION_DISTANCE_DP, parentActivity),  // toXDelta
                                0,    // fromYDelta
                                0);// toYDelta
                anim2.setDuration(Constant.EDIT_MODE_TEXT_TRANSITION_DURATION);
                anim2.setFillAfter(true);
                viewHolder.rainyStateLayout.startAnimation(anim2);
            }
        }

        ForecastInformation forecastInformation = SQLiteDatabaseManager.getInstance().getForecastInformation(locationId);
        if (forecastInformation != null) {
            if (forecastInformation.isRainy()){
                viewHolder.rainyStateTextView.setText("비 옴");
                viewHolder.rainyStateImageView.setImageResource(R.drawable.ic_umbrella);
            } else{
                viewHolder.rainyStateTextView.setText("비 안 옴");
                viewHolder.rainyStateImageView.setImageResource(R.drawable.ic_sun);
            }
        }

        return convertView;
    }
}
