package com.example.apiexample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import java.io.IOException;

public class NetworkLocationInformation {

    private Context mContext;
    private Location mLocation = null;
    private double mLatitude, mLongititude, mLatitudeSecond, mLongitudeSecond;
    private int mLatitudeHour, mLatitudeMinute, mLongitudeHour, mLongitudeMinute;
    private Address mNetworkAddress = null;

    public NetworkLocationInformation(Context context) {
        mContext = context;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocation = ((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        mLatitude = mLocation.getLatitude();
        mLongititude = mLocation.getLongitude();

        mLatitudeHour = (int) mLatitude;
        mLatitudeMinute = (int) ((mLatitude - mLatitudeHour) * 60);
        mLatitudeSecond = ((mLatitude - mLatitudeHour) * 60 - mLatitudeMinute) * 60;

        mLongitudeHour = (int) mLongititude;
        mLongitudeMinute = (int) ((mLongititude - mLongitudeHour) * 60);
        mLongitudeSecond = ((mLongititude - mLongitudeHour) * 60 - mLongitudeMinute) * 60;

        Geocoder g = new Geocoder(mContext);

        try {
            mNetworkAddress = g.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLatitudeHour() {
        return mLatitudeHour;
    }

    public int getLatitudeMinute() {
        return mLatitudeMinute;
    }

    public int getLongitudeHour() {
        return mLongitudeHour;
    }

    public int getLongitudeMinute() {
        return mLongitudeMinute;
    }

    public Address getNetworkAddress() {
        return mNetworkAddress;
    }
}
