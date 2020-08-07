package com.example.apiexample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private int currentFragmentRegionStep = 1;
    private CSVFileReader mCSVFileReader = null;
    private ArrayList<LocationInfo> mRegionDataCollections = null;

    private SQLiteDatabaseManager mSQLiteDatabaseManager = null;
    private static boolean isCreateUserFinished = false;
//    private GPSInformation mGPSInformation = null;
    private ViewPager viewPager;
    private TextViewPagerAdapter pagerAdapter;
    private long backKeyPressedTime = 0;

    private static Toast mToast;

    private String mUserId = "";
    private int mMyRegionCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateVariable();
        initiateView();
//        checkPermissions();
        initiateFirstAppInstalled();
        Log.d("First App Installed", "My app test 8: end Initiation");
//        closeAll();
    }

    private void checkPermissions() {
//        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "위치 조회 권한1이 거부되었습니다. 허용해주세요.");
//        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "위치 조회 권한2가 거부되었습니다. 허용해주세요.");
//        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "위치 조회 권한이 거부되었습니다. 허용해주세요.");
    }

    private void initiateVariable() {
        new RestAPIInstance(this);
        SQLiteDatabaseManager.initializeInstance(this);
        mSQLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
        mUserId = PreferenceManager.getString(this, Constant.USER_ID, "NO_ID");
        mMyRegionCount = PreferenceManager.getInt(this, Constant.MY_REGION_COUNT, 0);
    }

    private void initiateView() {
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new TextViewPagerAdapter(this, this);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initiateFirstAppInstalled() {
        Log.d("First App Installed", "My app test 1: Start Initiation");
        PreferenceManager.setBoolean(this, Constant.IS_INITIATION_FINISHED, false);

        if (PreferenceManager.getBoolean(this, Constant.IS_INITIATION_FINISHED, false)) {
            Log.d("First App Installed", "My app test initiate Already Finish");
            return;
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_GET_USER_ID_FINISHED, false)) {
            String myUserId = getUniqueId();
            Log.d("First App Installed", "My app test 2: Start GET USER ID : " + myUserId);
            createUser(myUserId);
        } else {
            Log.d("First App Installed", "My app test : Already GET USER ID : " + PreferenceManager.getString(this, Constant.USER_ID));
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_CSV_CONVERTED_TO_DATABASE_FINISHED, false)) {
            Log.d("First App Installed", "My app test 4: Start CSV CONVERTED");
            convertFromCSVToDatabase();
            PreferenceManager.setBoolean(this, Constant.IS_CSV_CONVERTED_TO_DATABASE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test : Already CSV CONVERTED");
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, false)) {
            Log.d("First App Installed", "My app test Start 5: Create my region table Start");
            mSQLiteDatabaseManager.createMyRegionDataDB();
            PreferenceManager.setBoolean(this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test Already Finished : Create my region table ");
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, false)) {
            Log.d("First App Installed", "My app test Start 6: Create Location id table Start");
            mSQLiteDatabaseManager.createLocationIdTable();
            PreferenceManager.setBoolean(this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test Already Finished : Create Location id table");
        }

        if (PreferenceManager.getBoolean(MainActivity.this, Constant.IS_CSV_CONVERTED_TO_DATABASE_FINISHED, false)
                && PreferenceManager.getBoolean(MainActivity.this, Constant.IS_GET_USER_ID_FINISHED, false)
                && PreferenceManager.getBoolean(MainActivity.this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, false)
                && PreferenceManager.getBoolean(MainActivity.this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, false)
        ) {
            PreferenceManager.setBoolean(MainActivity.this, Constant.IS_INITIATION_FINISHED, true);
            Log.d("First App Installed", "My app test 7 sync: Now Initiation finished");
        } else {
            Log.d("First App Installed", "My app test 7 sync: not ok at initiation");
        }
    }

    private void checkPermission(String permissionName, String needPermissionMessage){
        if (ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("my app test grant alreday");
            return;
        }
//        "저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다."
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
            System.out.println("my app test else statement start");
            ActivityCompat.requestPermissions(this, new String[]{permissionName}, Constant.MY_PERMISSION_STORAGE);
            return;
        }

        System.out.println("my app test if statement start");
        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage(needPermissionMessage)
                .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }


    private void convertFromCSVToDatabase() {
        mCSVFileReader = new CSVFileReader(getResources());
        mRegionDataCollections = mCSVFileReader.getRegionDataCollectionsFromCSVFile();
        mSQLiteDatabaseManager.createRegionDataDB(mRegionDataCollections);
        mSQLiteDatabaseManager.showRegionDataDB();
    }

    private void createUser(String userId) {
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.CreateUser(userId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()) {
                    System.out.println("My app test : response Fail at my id not succssful");
                    return;
                }

                if (response.body().intValue() != 0) {
                    System.out.println("My app test : response Fail at my ID");
                    return;
                }

                PreferenceManager.setString(MainActivity.this, Constant.USER_ID, userId);
                PreferenceManager.setBoolean(MainActivity.this, Constant.IS_GET_USER_ID_FINISHED, true);
                mUserId = PreferenceManager.getString(MainActivity.this, Constant.USER_ID);
                System.out.println("My app test 3: response Success my ID : " + mUserId);

                if (PreferenceManager.getBoolean(MainActivity.this, Constant.IS_CSV_CONVERTED_TO_DATABASE_FINISHED, false)
                        && PreferenceManager.getBoolean(MainActivity.this, Constant.IS_GET_USER_ID_FINISHED, false)
                        && PreferenceManager.getBoolean(MainActivity.this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, false)
                        && PreferenceManager.getBoolean(MainActivity.this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, false)
                ) {
                    PreferenceManager.setBoolean(MainActivity.this, Constant.IS_INITIATION_FINISHED, true);
                    Log.d("First App Installed", "My app test 7 async: Now Initiation finished");
                    return;
                }

                Log.d("First App Installed", "My app test 7 async: not ok at initiation");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("My app test : response fail at onFailure, message : " + t.getMessage());
            }
        });
    }

    protected void deleteUser() {
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.deleteUser(mUserId).enqueue(removeUserCallback);
    }

    private Callback<Integer> removeUserCallback = new Callback<Integer>() {
        @Override
        public void onResponse(Call<Integer> call, Response<Integer> response) {
            if(response.isSuccessful()) {
                int responseCode = response.body().intValue();
                if (responseCode == 0) {
                    PreferenceManager.remove (MainActivity.this, Constant.USER_ID);
                    PreferenceManager.remove (MainActivity.this, Constant.IS_GET_USER_ID_FINISHED);
                    System.out.println("My app test remove user : response Success my ID : " + mUserId);
                } else {
                    System.out.println("My app test remove user : response Fail at my ID");
                }
            } else {
                System.out.println("My app test remove user : response Fail at my id not succssful");
            }
        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
            System.out.println("My app test remove user : response fail at onFailure, message : " + t.getMessage());
        }
    };

    private String getUniqueId() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        closeAll();
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

    private void closeAll() {
        if (mCSVFileReader != null) {
            mCSVFileReader.close();
        }

//        if (mSQLiteDatabaseManager != null) {
//            mSQLiteDatabaseManager.close();
//        }
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
