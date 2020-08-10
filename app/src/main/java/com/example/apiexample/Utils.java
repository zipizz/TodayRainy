package com.example.apiexample;

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
        for (int i = 0, spaceCount = 0; i < fullName.length(); i++) {
            if (fullName.indexOf(i) == ' ') {
                spaceCount++;
            }
            if (spaceCount == 4) {
                fullName = fullName.substring(0, i).replaceAll("대한민국 ", "");
                break;
            }
        }
        return fullName;
    }
}
