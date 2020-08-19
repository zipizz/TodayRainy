package com.example.apiexample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ListenableWorker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestAPIFunction {

    private static RestAPIFunction instance = null;
    private static Context mContext = null;
    private static SplashActivity parentActivity = null;
    private static boolean isCalledFromSplashActivity = false;

    public RestAPIFunction() {};

    public static RestAPIFunction getInstance(Context context) {
        if (instance == null) {
            instance = new RestAPIFunction();
            mContext = context;
        }
        return instance;
    }

    public static RestAPIFunction getInstance(SplashActivity splashActivity) {
        if (instance == null) {
            parentActivity = splashActivity;
            mContext = parentActivity;
            instance = new RestAPIFunction();
        }
        return instance;
    }

    protected ListenableWorker.Result getAllLocalWeatherBackground(String userId) {
        try {
            RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
            Response<List<LinkedHashMap>> response = restAPI.getLocalWeather(userId).execute();
            return processGetAllLocalWeatherResponse(response);
        } catch (IOException e) {
            System.out.println("my app test : ioexception userid : " + userId + ", message : " + e.getMessage());
            return ListenableWorker.Result.failure();
        }
    }

//    public void getAllLocalWeather(String userId) {
//        Log.d("First App Installed", "My app test user id : " + userId);
//        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
//        restAPI.getLocalWeather(userId).enqueue(getAllLocalWeatherOnServiceCallbackFunction);
//    }

    public void getAllLocalWeather(String userId, boolean isCalledFromSplashActivity) {
        Log.d("First App Installed", "My app test user id : " + userId);
        Log.d("First App Installed", "My app test from splash Activity ? : " + isCalledFromSplashActivity);
        this.isCalledFromSplashActivity = isCalledFromSplashActivity;
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.getLocalWeather(userId).enqueue(getAllLocalWeatherCallbackFunction);
    }

    private Callback<List<LinkedHashMap>> getAllLocalWeatherCallbackFunction = new Callback<List<LinkedHashMap>>() {
        @Override
        public void onResponse(Call<List<LinkedHashMap>> call, Response<List<LinkedHashMap>> response) {
            if (response.isSuccessful()) {
                boolean isRainy = false;
                ArrayList<String>rainyLocation = new ArrayList<>();
                Log.d("First App Installed", "My app test Start get all local weather success toString : " + response.toString());
                for (LinkedHashMap responseElement : response.body()) {
                    Log.d("First App Installed", "My app test Start get all local weather success body toString : " + responseElement.toString());
                    ForecastInformationTown forecastInformationTown = Utils.getForecastInformation(responseElement);
                    SQLiteDatabaseManager.getInstance().updateForecastInformationTable(forecastInformationTown);
                    if(forecastInformationTown.isRainy()) {
                        isRainy = true;
                        rainyLocation.add(SQLiteDatabaseManager.getInstance().getMyRegionDataFromRegionIncludingCurrentLocationTable(forecastInformationTown.getLocationId()).toString());
                    }
                    Log.d("First App Installed", "My app test : location forecast update success at " + forecastInformationTown.getLocationId());
                }
                if (isCalledFromSplashActivity) {
                    Log.d("First App Installed", "My app test : startMainActivity at RestAPIFunction");
                    parentActivity.startMainActivity();
                }
            } else {
                Log.d("First App Installed", "My app test Start get all local weather not successful");
            }
        }

        @Override
        public void onFailure(Call<List<LinkedHashMap>> call, Throwable t) {
            Log.d("First App Installed", "My app test Start get all local weather fail at onFailure");
        }
    };

//    private Callback<List<LinkedHashMap>> getAllLocalWeatherOnServiceCallbackFunction = new Callback<List<LinkedHashMap>>() {
//        @Override
//        public void onResponse(Call<List<LinkedHashMap>> call, Response<List<LinkedHashMap>> response) {
//            processGetAllLocalWeatherResponse(response);
//        }
//
//        @Override
//        public void onFailure(Call<List<LinkedHashMap>> call, Throwable t) {
//            Log.d("First App Installed", "My app test Start get all local weather fail at onFailure");
//        }
//    };

    private ListenableWorker.Result processGetAllLocalWeatherResponse(Response<List<LinkedHashMap>> response) {
        if (!response.isSuccessful()) {
            Log.d("First App Installed", "My app test Start get all local weather not successful");
            return ListenableWorker.Result.failure();
        }

        boolean isRainy = false;
        ArrayList<String>rainyLocation = new ArrayList<>();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String updatedTime = formatter.format(date);

        Log.d("First App Installed", "My app test Start get all local weather success toString : " + response.toString());
        for (LinkedHashMap responseElement : response.body()) {
            Log.d("First App Installed", "My app test Start get all local weather success body toString : " + responseElement.toString());
            ForecastInformationTown forecastInformationTown = Utils.getForecastInformation(responseElement);
            SQLiteDatabaseManager.getInstance().updateForecastInformationTable(forecastInformationTown);
            if(forecastInformationTown.isRainy()) {
                isRainy = true;
                rainyLocation.add(SQLiteDatabaseManager.getInstance().getMyRegionDataFromRegionIncludingCurrentLocationTable(forecastInformationTown.getLocationId()).toString());
            }
            Log.d("First App Installed", "My app test : location forecast update success at " + forecastInformationTown.getLocationId());
        }

        String CHANNEL_ID = Constant.CHANNEL_ID;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

//        Intent intent = new Intent(mContext, SplashActivity.class);
        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        if (!isRainy) {
            builder.setSmallIcon(R.drawable.ic_sun)
                    .setContentTitle("오늘 비 안 온다. " + updatedTime);
        } else {
            String rainyLocationList = "";
            for (String rainyLocationElement : rainyLocation) {
                rainyLocationList += rainyLocationElement + ", ";
            }
            rainyLocationList = rainyLocationList.substring(0, rainyLocationList.length() - 1);
            builder.setSmallIcon(R.drawable.ic_umbrella)
                    .setContentTitle("오늘 비 온다. " + updatedTime)
                    .setContentText("장소는 " + rainyLocationList);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(parentActivity);
        notificationManager.notify(Constant.NOTIFY_ID, builder.build());

        return ListenableWorker.Result.success();
    }
}
