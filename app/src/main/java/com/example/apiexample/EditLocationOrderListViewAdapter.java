package com.example.apiexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private class ViewHolder {
        ConstraintLayout editItemLayout;
        AppCompatImageView selectButton;
        TextView regionNameView;
        AppCompatImageView changeItemOrderView;
        TextView selectedLocationCountView;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_location_custom_view_item_edit, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.editItemLayout = convertView.findViewById(R.id.item_edit_layout);
            viewHolder.selectButton = (AppCompatImageView) convertView.findViewById(R.id.edit_select_button);
            viewHolder.regionNameView = (TextView) convertView.findViewById(R.id.region_name);
            viewHolder.changeItemOrderView = (AppCompatImageView) convertView.findViewById(R.id.change_item_order);
            viewHolder.selectedLocationCountView = (TextView) convertView.findViewById(R.id.selected_location_count);
            viewHolder.position = position;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LocationInfo locationInfo = (LocationInfo) getItem(viewHolder.position);

        if(viewHolder.position == 0) {
            viewHolder.regionNameView.setText("GPS 현재 위치");
            viewHolder.selectButton.setVisibility(View.GONE);
            viewHolder.changeItemOrderView.setVisibility(View.GONE);
        } else {
            if(parentActivity.isEditButtonClickedAtLeastOnce()) {
                viewHolder.regionNameView.setText(locationInfo.toString());
                TranslateAnimation anim = new TranslateAnimation
                        (0,   // fromXDelta
                                Utils.convertDpToPixel(Constant.EDIT_MODE_TEXT_TRANSITION_DISTANCE_DP, parentActivity),  // toXDelta
                                0,    // fromYDelta
                                0);// toYDelta
                anim.setDuration(Constant.EDIT_MODE_TEXT_TRANSITION_DURATION);
                anim.setFillAfter(true);
                viewHolder.regionNameView.startAnimation(anim);

                TranslateAnimation anim2 = new TranslateAnimation
                        (0,   // fromXDelta
                                Utils.convertDpToPixel(Constant.EDIT_MODE_BUTTON_TRANSITION_DISTANCE_DP, parentActivity),  // toXDelta
                                0,    // fromYDelta
                                0);// toYDelta
                anim2.setDuration(Constant.EDIT_MODE_TEXT_TRANSITION_DURATION);
                anim2.setFillAfter(true);
                viewHolder.selectButton.startAnimation(anim2);

                TranslateAnimation anim3 = new TranslateAnimation
                        (Utils.convertDpToPixel(Constant.EDIT_MODE_BUTTON_TRANSITION_DISTANCE_DP, parentActivity),   // fromXDelta
                                0,  // toXDelta
                                0,    // fromYDelta
                                0);// toYDelta
                anim3.setDuration(Constant.EDIT_MODE_TEXT_TRANSITION_DURATION);
                anim3.setFillAfter(true);
                viewHolder.changeItemOrderView.startAnimation(anim3);
            }
        }

        ConstraintLayout mButton = (ConstraintLayout)convertView.findViewById(R.id.touch_area);
        mButton.setTag(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.position == 0) {
                    return;
                }

                boolean isTouched = !(boolean) mButton.getTag();
                mButton.setTag(isTouched);
                System.out.println("my app test first position : " + viewHolder.position);
                if (isTouched) {
                    doCheckEvent(viewHolder.position);
                    viewHolder.editItemLayout.setBackgroundResource(R.drawable.radius_touched);
                    viewHolder.selectButton.setImageResource(R.drawable.ic_radio_button_checked_24px);
                    parentActivity.addDeletedLocationId(((LocationInfo) getItem(viewHolder.position)).getLocationId());
                } else {
                    doUncheckEvent(viewHolder.position);
                    viewHolder.editItemLayout.setBackgroundResource(R.drawable.radius_little);
                    viewHolder.selectButton.setImageResource(R.drawable.ic_radio_button_unchecked_24px);
                    parentActivity.removeDeletedLocationId(((LocationInfo) getItem(viewHolder.position)).getLocationId());
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

    public void slideRightLocationText() {

    }

//    private void doAfterDelete() {
//        for (ConstraintLayout button : mButtonList) {
//            button.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
//        }
//    }
}


