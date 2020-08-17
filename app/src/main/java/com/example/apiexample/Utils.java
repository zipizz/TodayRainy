package com.example.apiexample;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.LinkedHashMap;

public class Utils {

    public static String getUniqueId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public String precipitationFormDn;
    public String probabilityOfPrecipitation;
    public String precipitationDn;
    public String updatedDate;
    public String precipitationForm;
    public String humidity;
    public String precipitation;
    public String uid;
    public int locationId;

    public static ForecastInformationTown getForecastInformation(LinkedHashMap responseElement) {
        String precipitationFormDn = responseElement.get("precipitationFormDn").toString();
        String probabilityOfPrecipitation = responseElement.get("probabilityOfPrecipitation").toString();
        String precipitationDn = responseElement.get("precipitationDn").toString();
        String updatedDate = responseElement.get("updatedDate").toString();
        String precipitationForm = responseElement.get("precipitationForm").toString();
        String humidity = responseElement.get("humidity").toString();
        String precipitation = responseElement.get("precipitation").toString();
        String uid = responseElement.get("uid").toString();
        int locationId = responseElement.get("locationId").toString().charAt(0) - '0';
        return new ForecastInformationTown(precipitationFormDn, probabilityOfPrecipitation, precipitationDn, updatedDate, precipitationForm,
                humidity, precipitation, uid, locationId);
    }

    public static String getFormattedLocationNameFromFullName(String fullName) {
        int lastSpaceIndex = fullName.lastIndexOf(' ');
        return fullName.substring(0, lastSpaceIndex).replaceAll("대한민국 ", "");
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
