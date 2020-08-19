package com.example.apiexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private int currentFragmentRegionStep = 1;
    private ViewPager viewPager;
    private TextViewPagerAdapter pagerAdapter;
    private long backKeyPressedTime = 0;

    private static Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateVariable();
        initiateView();
        startGetForecastInformationService();
        Log.d("First App Installed", "My app test 8: end Initiation");
    }

    private void initiateVariable() {
        SQLiteDatabaseManager.initializeInstance(this);
    }

    private void initiateView() {
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new TextViewPagerAdapter(this, this);
        viewPager.setAdapter(pagerAdapter);
    }

    private void startGetForecastInformationService() {
        System.out.println("my app test start getForcastInformation Service");
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        System.out.println("my app test start getForcastInformation Service constraint build finish");
        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(GetForecastInformationWorker.class, Constant.GET_FORECAST_INFORMATION_PERIOD_MINUTE, TimeUnit.MINUTES)
                        .setInitialDelay(Utils.getDelayTimeForGetForecastInformationServiceDongneStart(), TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .build();
        System.out.println("my app test start getForcastInformation Service workrequest build finish");
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(Constant.GET_FORECAST_INFORMATION_WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, workRequest);
        System.out.println("my app test start getForcastInformation Service enqueue finish.");
    }

//    protected void deleteUser() {
//        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
//        restAPI.deleteUser(mUserId).enqueue(removeUserCallback);
//    }
//
//    private Callback<Integer> removeUserCallback = new Callback<Integer>() {
//        @Override
//        public void onResponse(Call<Integer> call, Response<Integer> response) {
//            if(response.isSuccessful()) {
//                int responseCode = response.body().intValue();
//                if (responseCode == 0) {
//                    PreferenceManager.remove (MainActivity.this, Constant.USER_ID);
//                    PreferenceManager.remove (MainActivity.this, Constant.IS_GET_USER_ID_FINISHED);
//                    System.out.println("My app test remove user : response Success my ID : " + mUserId);
//                } else {
//                    System.out.println("My app test remove user : response Fail at my ID");
//                }
//            } else {
//                System.out.println("My app test remove user : response Fail at my id not succssful");
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Integer> call, Throwable t) {
//            System.out.println("My app test remove user : response fail at onFailure, message : " + t.getMessage());
//        }
//    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("My app test change location on activity result");
        if (requestCode == Constant.CHANGE_LOCATION_REQUEST_CODE) {
            if (resultCode == Constant.CHANGE_LOCATION_REQUEST_SUCCESS_CODE) {
                if(!data.getExtras().getBoolean("locationChanged")) {
                    System.out.println("My app test change location fail");
                    return;
                };
                System.out.println("My app test change location success");
                pagerAdapter.setDataChanged();
            } else {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    private Callback<Integer> deleteLocationCallback = new Callback<Integer>() {
//        @Override
//        public void onResponse(Call<Integer> call, Response<Integer> response) {
//            if (response.isSuccessful()) {
//                int responseCode = response.body().intValue();
//                System.out.println("My app test delete location : response code : " + responseCode);
//                if(responseCode == 0) {
//                    System.out.println("My app test delete location : response code success");
//                    SQLiteDatabaseManager.getInstance().deleteMyRegionData(mSelectedLocationId);
//                    SQLiteDatabaseManager.getInstance().addLocationId(mSelectedLocationId);
//                    PreferenceManager.setInt(MainActivity.this,
//                            Constant.MY_REGION_COUNT,
//                            PreferenceManager.getInt(MainActivity.this,
//                                    Constant.MY_REGION_COUNT) - 1);
//                } else {
//                    System.out.println("My app test delete location : response code fail");
//                }
//            } else {
//                System.out.println("My app test delete location : response fail not successful");
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Integer> call, Throwable t) {
//            System.out.println("My app test delete location : location add Fail 3");
//        }
//    };

//    private Callback<Integer> asdf = new Callback<Integer>() {
//        @Override
//        public void onResponse(Call<Integer> call, Response<Integer> response) {
//            if (response.isSuccessful()) {
//                System.out.println("My app test : Success, responseBody : " + response.body());
//                getMethod(response.body().toString());
//            }
//            else {
//                System.out.println("My app test : fail, responseBody : " + response.body());
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Integer> call, Throwable t) {
//            System.out.println("My app test : Fail");
//            Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
//        }
//    };

    private LocationInfo getWeatherLocation(String regionStepOne, String regionStepTwo, String regionStepThree, boolean isGlobalRegion) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setRegionStep1(regionStepOne);
        locationInfo.setRegionStep2(regionStepTwo);
        locationInfo.setRegionStep3(regionStepThree);
        locationInfo.setIsGlobalRegion(isGlobalRegion);
        return locationInfo;
    }

    private LocationInfo getWeatherLocation(int latitudeHour, int latitudeMinute, int longitudeHour, int longitudeMinute) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLatitudeHour(latitudeHour);
        locationInfo.setLatitudeMin(latitudeMinute);
        locationInfo.setLongitudeHour(longitudeHour);
        locationInfo.setLongitudeMin(longitudeMinute);
        return locationInfo;
    }

    public void toastMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (currentFragmentRegionStep > 1) {
            currentFragmentRegionStep--;
            backKeyPressedTime = 0;
            super.onBackPressed();
            return;
        }

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toastMessage("'뒤로' 버튼을 한번 더 누르시면 종료됩니다.");
            return;
        }

        super.onBackPressed();
    }
}
