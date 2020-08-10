package com.example.apiexample;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.LinkedHashMap;

public class Utils {

    public static String getUniqueId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static ForecastInformation getForecastInformation(LinkedHashMap responseElement) {
        int locationId = responseElement.get("locationId").toString().charAt(0) - '0';
        String precipitationForm = responseElement.get("precipitationForm").toString();
        String humidity = responseElement.get("humidity").toString();
        String precipitation = responseElement.get("precipitation").toString();
        String uid = responseElement.get("uid").toString();
        return new ForecastInformation(precipitationForm, humidity, precipitation, uid, locationId);
    }

    public static String getFormattedLocationNameFromFullName(String fullName) {

        int lastSpaceIndex = fullName.lastIndexOf(' ');
        return fullName.substring(0, lastSpaceIndex).replaceAll("대한민국 ", "");
//        for (int i = 0, spaceCount = 0; i < fullName.length(); i++) {
//
//            if (fullName.charAt(i) == ' ') {
//                spaceCount++;
//            }
//            if (spaceCount == 4) {
//                fullName = fullName.substring(0, i).replaceAll("대한민국 ", "");
//                break;
//            }
//        }
//        return fullName;
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
