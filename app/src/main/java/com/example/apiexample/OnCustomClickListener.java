package com.example.apiexample;

import android.os.SystemClock;
import android.view.View;

public abstract class OnCustomClickListener implements  View.OnClickListener {
    private long mLastClickTime;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        if(elapsedTime < Constant.DUPLICATE_CLICK_IGNORE_DELAY_MILLIS) {
            return;
        }

        onSingleClick(v);
    }
}