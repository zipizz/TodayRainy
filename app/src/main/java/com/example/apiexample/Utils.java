package com.example.apiexample;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

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

    public static ForecastInformationTown getForecastInformation(LinkedHashMap responseElement, String updatedDate) {
        responseElement.put("updatedDate", updatedDate);
        return getForecastInformation(responseElement);
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

    // 2 5 8 11 14 17 20 23
    public static long getDelayTimeForGetForecastInformationServiceDongNeStart() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(currentDate);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int remainHour = getRemainHour(currentHour);

        if (!isNoNeedToAddDelayHour(currentHour, currentMinute)) {
            calendar.add(Calendar.HOUR_OF_DAY, remainHour);
        }
        calendar.set(Calendar.MINUTE, Constant.GET_FORECAST_INFORMATION_DELAY_MINUTE_AFTER_TIME_POINT_FOR_DONGNE);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() - currentTimeMillis;
    }

    public static String getFormattedCurrentTimeString() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE) % 5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return sdf.format(calendar.getTime());
    }

    static int getRemainHour(int currentHour) {
        int calculateHour = currentHour - Constant.GET_FORECAST_INFORMATION_BASE_TIME_HOUR_FOR_DONGNE + Constant.GET_FORECAST_INFORMATION_DURATION_HOUR_FOR_DONGNE;
        calculateHour = calculateHour % Constant.GET_FORECAST_INFORMATION_DURATION_HOUR_FOR_DONGNE;
        return Constant.GET_FORECAST_INFORMATION_DURATION_HOUR_FOR_DONGNE - calculateHour;
    }

    // 23.00 ~ 23.25 2.00 ~ 2.25
    private static boolean isNoNeedToAddDelayHour(int currentHour, int currentMinute) {
        return currentHour % Constant.GET_FORECAST_INFORMATION_DURATION_HOUR_FOR_DONGNE == Constant.GET_FORECAST_INFORMATION_BASE_TIME_HOUR_FOR_DONGNE
                && currentMinute <= Constant.GET_FORECAST_INFORMATION_DELAY_MINUTE_AFTER_TIME_POINT_FOR_DONGNE;
    }
}
