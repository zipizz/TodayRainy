package com.example.apiexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class EditLocationOrderListViewAdapter extends ArrayAdapter {

    private boolean isDeleteLayoutVisible = false;
    private int mTouchCount = 0;
    private Context mContext = null;
    private AddLocationActivity parentActivity = null;

    public EditLocationOrderListViewAdapter(Context context, ArrayList<LocationInfo> customItems, AddLocationActivity parentActivity) {
        super(context, 0, customItems);
        mContext = context;
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_location_custom_view_item_edit, parent, false);
        }

        ConstraintLayout editItemLayout = convertView.findViewById(R.id.item_edit_layout);
        LocationInfo locationInfo = (LocationInfo) getItem(position);

        AppCompatImageView selectButton = (AppCompatImageView) convertView.findViewById(R.id.edit_select_button);
        TextView regionNameView = (TextView) convertView.findViewById(R.id.region_name);
        AppCompatImageView changeItemOrderView = (AppCompatImageView) convertView.findViewById(R.id.change_item_order);
        TextView selectedLocationCountView = (TextView) convertView.findViewById(R.id.selected_location_count);

        if(position == 0) {
            regionNameView.setText("GPS 현재 위치");
        } else {
            regionNameView.setText(locationInfo.toString());
        }

        ConstraintLayout mButton = (ConstraintLayout)convertView.findViewById(R.id.touch_area);
        mButton.setTag(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    return;
                }

                boolean isTouched = !(boolean) mButton.getTag();
                mButton.setTag(isTouched);
                System.out.println("my app test first position : " + position);
                if (isTouched) {
                    doCheckEvent(position);
                    editItemLayout.setBackgroundResource(R.drawable.radius_touched);
                    parentActivity.addDeletedLocationId(((LocationInfo) getItem(position)).getLocationId());
                } else {
                    doUncheckEvent(position);
                    editItemLayout.setBackgroundResource(R.drawable.radius_little);
                    parentActivity.removeDeletedLocationId(((LocationInfo) getItem(position)).getLocationId());
                }

                parentActivity.setSelectedCountText(mTouchCount);

                if (!isDeleteLayoutVisible && mTouchCount == 1) {
                     parentActivity.slideUp();
                     isDeleteLayoutVisible = !isDeleteLayoutVisible;
                } else if (mTouchCount == 0 && isDeleteLayoutVisible) {
                    parentActivity.slideDown();
                    isDeleteLayoutVisible = !isDeleteLayoutVisible;
                }
            }
        });

        return convertView;
    }

    private void doCheckEvent(int position) {
        System.out.println("my app test touched, position : " + position);
        mTouchCount++;
//        ((View)getItem(position)).findViewById(R.id.item_edit_layout).setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey));
    }

    private void doUncheckEvent (int position) {
        System.out.println("my app test touch released, position : " + position);
        mTouchCount--;
//        ((View)getItem(position)).findViewById(R.id.item_edit_layout).setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
    }

    protected void doAfterEditFinished() {
        System.out.println("my app test editfinished");
        parentActivity.slideClear();
        parentActivity.clearDeletedLocationIdList();
        isDeleteLayoutVisible = false;
        mTouchCount = 0;
    }
//    private void doAfterDelete() {
//        for (ConstraintLayout button : mButtonList) {
//            button.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
//        }
//    }
}


