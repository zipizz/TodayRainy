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
    public static long getDelayTimeForGetForecastInformationServiceDongneStart() {

//        // date format
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        // two dates
//        java.util.Date scheduledDate;
//        Calendar current = Calendar.getInstance();
//        java.util.Date currentDate;
//        String current_date = format.format(current.getTime());
//        try {
//            scheduledDate = format.parse(scheduled_date);
//            currentDate = format.parse(current_date);
//            long diffInMillies = scheduledDate.getTime() - currentDate.getTime();
//            long diffence_in_minute = TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
//            System.out.println(diffence_in_minute);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        Date date = new Date();   // given date
//        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//        calendar.setTime(date);   // assigns calendar to given date
//        calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
//        calendar.get(Calendar.MINUTE);
//        calendar.get(Calendar.)
//
//        SimpleDateFormat sdf = new SimpleDateFormat("HH");
//        int currentHour = Integer.parseInt(sdf.format(new Date(System.currentTimeMillis())));
//
//
      return  System.currentTimeMillis();
        
    }
}
