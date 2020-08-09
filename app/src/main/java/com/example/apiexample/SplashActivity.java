package com.example.apiexample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private String mUserId = "";
    private CSVFileReader mCSVFileReader = null;
    private SQLiteDatabaseManager mSQLiteDatabaseManager = null;

    private ArrayList<LocationInfo> mRegionDataCollections = null;
    private LocationInfoForServer mCurrentLocationInfo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initiateVariable();
        boolean isAlreadyInitiateFinished = initiateFirstAppInstalled();
        startMainActivity(isAlreadyInitiateFinished);
    }

    private void initiateVariable() {
//        checkPermissions();
        new RestAPIInstance(this);
        SQLiteDatabaseManager.initializeInstance(this);
        mSQLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
        mUserId = PreferenceManager.getString(this, Constant.USER_ID, "NO_ID");
    }

    private void checkPermissions() {
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "위치 조회 권한이 거부되었습니다. 허용해주세요.");
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "위치 조회 권한2가 거부되었습니다. 허용해주세요.");
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


    private boolean initiateFirstAppInstalled() {
        Log.d("First App Installed", "My app test 1: Start Initiation");

        if (PreferenceManager.getBoolean(this, Constant.IS_INITIATION_FINISHED, false)) {
            Log.d("First App Installed", "My app test initiate Already Finish");

            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }.start();

            return true;
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_GET_USER_ID_FINISHED, false)) {
            String myUserId = Utils.getUniqueId();
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

        if (!PreferenceManager.getBoolean(this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, false)) {
            Log.d("First App Installed", "My app test Start 6: Create Location id table Start");
            mSQLiteDatabaseManager.createLocationIdTable();
            PreferenceManager.setBoolean(this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test Already Finished : Create Location id table");
        }

        if (PreferenceManager.getBoolean(this, Constant.IS_CSV_CONVERTED_TO_DATABASE_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_GET_USER_ID_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_CREATE_FORECAST_INFORMATION_TABLE_FINISHED, false)
        ) {
            PreferenceManager.setBoolean(this, Constant.IS_INITIATION_FINISHED, true);
            Log.d("First App Installed", "My app test 7 sync: Now Initiation finished");
        } else {
            Log.d("First App Installed", "My app test 7 sync: not ok at initiation");
        }
        return false;
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

                PreferenceManager.setString(SplashActivity.this, Constant.USER_ID, userId);
                PreferenceManager.setBoolean(SplashActivity.this, Constant.IS_GET_USER_ID_FINISHED, true);
                mUserId = PreferenceManager.getString(SplashActivity.this, Constant.USER_ID);
                System.out.println("My app test 3: response Success my ID : " + mUserId);

                createMyLocationTable();
                AddCurrentLocation(); // 현재 지역 서버에 추가, 현재 지역 정보 디비에 저장, 현재 지역의 날씨 정보 얻기, 현재 지역의 날씨 정보를 DB에 업데이트
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("My app test : response fail at onFailure, message : " + t.getMessage());
            }
        });
    }

    private void createMyLocationTable() {
        if (!PreferenceManager.getBoolean(this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, false)) {
            Log.d("First App Installed", "My app test Start 5: Create my region table Start");
            mSQLiteDatabaseManager.createMyRegionDataDB();
            PreferenceManager.setBoolean(this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test Already Finished : Create my region table ");
        }
    }

    private void convertFromCSVToDatabase() {
        mCSVFileReader = new CSVFileReader(getResources());
        mRegionDataCollections = mCSVFileReader.getRegionDataCollectionsFromCSVFile();
        mSQLiteDatabaseManager.createRegionDataDB(mRegionDataCollections);
        mSQLiteDatabaseManager.showRegionDataDB();
    }

    private void AddCurrentLocation() {
//        GPSInformation gpsInformation = new GPSInformation(SplashActivity.this);

        mCurrentLocationInfo = new LocationInfoForServer();
        String a = "";
//        if (gpsInformation.getGPSAddress() == null) {
//            a = "null 나왔다.";
            mCurrentLocationInfo.setRegionStep1("경기도");
            mCurrentLocationInfo.setRegionStep2("성남시분당구");
            mCurrentLocationInfo.setRegionStep3("금곡동");
//        } else {
//            a = gpsInformation.getGPSAddress().toString();
//            mCurrentLocationInfo.setLongitudeHour(gpsInformation.getLongitudeHour());
//            mCurrentLocationInfo.setLongitudeMin(gpsInformation.getLongitudeMinute());
//            mCurrentLocationInfo.setLatitudeHour(gpsInformation.getLatitudeHour());
//            mCurrentLocationInfo.setLongitudeMin(gpsInformation.getLongitudeMinute());
//        }

        Log.d("First App Installed", "My app test GPS : " + a);
        postRequestAddCurrentLocation();
    }

    public void postRequestAddCurrentLocation() {
        System.out.println("My app test postRequestAddCurrentLocation function in + id : " + mUserId + ", loc : " + Constant.CURRENT_LOCATION_ID );
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.AddLocation(mUserId, Constant.CURRENT_LOCATION_ID, mCurrentLocationInfo).enqueue(addCurrentLocationCallbackFunction);
    }

    private Callback<Integer> addCurrentLocationCallbackFunction = new Callback<Integer>() {
        @Override
        public void onResponse(Call<Integer> call, Response<Integer> response) {
            Log.d("First App Installed", "My app test Start add location current here : " + response.toString());
            if(response.isSuccessful()) {
                if (response.body().intValue() == 0) {
                    Log.d("First App Installed", "My app test Start add location current success");
                    mCurrentLocationInfo.setRegionStep1("경기도");
                    mCurrentLocationInfo.setRegionStep2("성남시분당구");
                    mCurrentLocationInfo.setRegionStep3("금곡동");
                    mSQLiteDatabaseManager.addMyRegionData(new LocationInfo(mCurrentLocationInfo));
                    createForecastInformationTable();
                    getCurrentLocalWeather();
                } else {
                    Log.d("First App Installed", "My app test Start add location current successful but fail");
                }
            } else {
                Log.d("First App Installed", "My app test Start add location current not successful");
            }
        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
            Log.d("First App Installed", "My app test Start add location current fail at onfailure");
        }
    };

    private void createForecastInformationTable() {
        if (!PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_CREATE_FORECAST_INFORMATION_TABLE_FINISHED, false)) {
            Log.d("First App Installed", "My app test Start 6: Create FORECAST_INFORMATION_TABLE Start");
            mSQLiteDatabaseManager.createForecastInformationTable();
            PreferenceManager.setBoolean(SplashActivity.this, Constant.IS_CREATE_FORECAST_INFORMATION_TABLE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test Already Finished : Create FORECAST_INFORMATION_TABLE");
        }
    }

    private void getCurrentLocalWeather() {
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.getLocalWeather(mUserId).enqueue(getCurrentLocalWeatherCallbackFunction);
    }

    private Callback<List<LinkedHashMap>> getCurrentLocalWeatherCallbackFunction = new Callback<List<LinkedHashMap>>() {
        @Override
        public void onResponse(Call<List<LinkedHashMap>> call, Response<List<LinkedHashMap>> response) {
            if (response.isSuccessful()) {
                Log.d("First App Installed", "My app test Start get current local weather success toString : " + response.toString());
                for (LinkedHashMap responseElement : response.body()) {
                    ForecastInformation forecastInformation = Utils.getForecastInformation(responseElement);
                    if (forecastInformation.getLocationId() != Constant.CURRENT_LOCATION_ID) {
                        continue;
                    }
                    SQLiteDatabaseManager.getInstance().updateForecastInformationTable(forecastInformation);
                    Log.d("First App Installed", "My app test : current location forecast update success");
                    break;
                }

                if (PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_CSV_CONVERTED_TO_DATABASE_FINISHED, false)
                        && PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_GET_USER_ID_FINISHED, false)
                        && PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, false)
                        && PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, false)
                        && PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_CREATE_FORECAST_INFORMATION_TABLE_FINISHED, false)
                ) {
                    PreferenceManager.setBoolean(SplashActivity.this, Constant.IS_INITIATION_FINISHED, true);
                    Log.d("First App Installed", "My app test 7 async: Now Initiation finished");
                    close();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.d("First App Installed", "My app test Start get current local weather not successful");
            }
        }

        @Override
        public void onFailure(Call<List<LinkedHashMap>> call, Throwable t) {
            Log.d("First App Installed", "My app test Start get current local weather fail at onfailure");
        }
    };

    private void close() {
        if (mCSVFileReader != null) {
            mCSVFileReader.close();
        }
    }

    private void startMainActivity(boolean isAlreadyInitiateFinished) {
        if (isAlreadyInitiateFinished) {
            getAllLocalWeather();
        }
    }

    private void getAllLocalWeather() {
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.getLocalWeather(mUserId).enqueue(getCAllLocalWeatherCallbackFunction);
    }

    private Callback<List<LinkedHashMap>> getCAllLocalWeatherCallbackFunction = new Callback<List<LinkedHashMap>>() {
        @Override
        public void onResponse(Call<List<LinkedHashMap>> call, Response<List<LinkedHashMap>> response) {
            if (response.isSuccessful()) {
                Log.d("First App Installed", "My app test Start get all local weather success toString : " + response.toString());
                for (LinkedHashMap responseElement : response.body()) {
                    ForecastInformation forecastInformation = Utils.getForecastInformation(responseElement);
                    SQLiteDatabaseManager.getInstance().updateForecastInformationTable(forecastInformation);
                    Log.d("First App Installed", "My app test : location forecast update success at " + forecastInformation.getLocationId());
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("First App Installed", "My app test Start get all local weather not successful");
            }
        }

        @Override
        public void onFailure(Call<List<LinkedHashMap>> call, Throwable t) {
            Log.d("First App Installed", "My app test Start get all local weather fail at onFailure");
        }
    };
}
