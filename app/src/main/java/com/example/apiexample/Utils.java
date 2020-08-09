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
}
