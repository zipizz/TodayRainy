package com.example.apiexample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private String mUserId = "";
    private CSVFileReader mCSVFileReader = null;
    private SQLiteDatabaseManager mSQLiteDatabaseManager = null;
    private String mCurrentLocationName = "";
    private ArrayList<LocationInfo> mRegionDataCollections = null;
    private LocationInfoForServer mCurrentLocationInfo = null;
    private static Context mContext;


    private ArrayList<String> mManifestPermissionName = null;
    private ArrayList<String> mPermissionDeniedMessage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initiateVariable();
        processInitilize();
    }

    private void initiateVariable() {
        new RestAPIInstance(this);
        SQLiteDatabaseManager.initializeInstance(this);
        mSQLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
        mUserId = PreferenceManager.getString(this, Constant.USER_ID, "NO_ID");
        mContext = this;
        initPermissionVariables();
        for (int i = 0; i < Constant.INITIALIZE_STEP_WORKS.size(); i++) {
            int stepNumber = i + 1;
            PreferenceManager.setInt(this, Constant.INITIALIZE_STEP_WORKS.get(i), stepNumber);
        }
    }

    private void initPermissionVariables() {
        int permissionStep = Constant.INITIALIZE_STEP_WORKS.indexOf(Constant.INITIALIZE_STEP_PERMISSION) + 1;
        int lastFinishedStep = PreferenceManager.getInt(this, Constant.INITIALIZE_FINISHED_LAST_STEP, 0);
        if (lastFinishedStep < permissionStep) {
            mManifestPermissionName = new ArrayList<>();
            mPermissionDeniedMessage = new ArrayList<>();
            for (int i = 0; i < Constant.MANIFEST_PERMISSION_NAMES.length; i++) {
                mManifestPermissionName.add(Constant.MANIFEST_PERMISSION_NAMES[i]);
                mPermissionDeniedMessage.add(Constant.PERMISSION_DENIED_MESSAGES[i]);
            }
        }
    }

    private void processInitilize() {
        processStep(PreferenceManager.getNextProcessStep(this));
    }

    private void processStep(int currentProcessStep) {
        String currentStepProcessName = Constant.INITIALIZE_STEP_WORKS.get(currentProcessStep - 1);
        System.out.println("my app test processStep : " + currentStepProcessName);

        if (currentStepProcessName.equalsIgnoreCase(Constant.INITIALIZE_STEP_PERMISSION)) {
            // 비동기
            checkPermissions();
        } else if (currentStepProcessName.equalsIgnoreCase(Constant.INITIALIZE_STEP_CONVERT_CSV)) {
            // 동기
            convertFromCSVToDatabase();
            processNextStep();
        } else if (currentStepProcessName.equalsIgnoreCase(Constant.INITIALIZE_STEP_CREATE_TABLE)) {
            // 동기
            createTable();
            processNextStep();
        } else if (currentStepProcessName.equalsIgnoreCase(Constant.INITIALIZE_STEP_CREATE_USER)) {
            // 비동기
            createUser(Utils.getUniqueId());
        } else if (currentStepProcessName.equalsIgnoreCase(Constant.INITIALIZE_STEP_CURRENT_LOCATION_REGISTER)) {
            // 비동기
            registerCurrentLocation();
        } else if (currentStepProcessName.equalsIgnoreCase(Constant.INITIALIZE_STEP_GET_FORECAST_INFORMATION)) {
            // 비동기
            RestAPIFunction.getInstance(SplashActivity.this).getAllLocalWeather(mUserId, true);
        } else {
            Log.d("First app installed", "my app test not exist step of initilized process");
        }
    }

    private void processNextStep() {
        PreferenceManager.incrementInitilizeStep(this);
        processStep(PreferenceManager.getNextProcessStep(this));
    }

    private void checkPermissions() {
        int remainManifestPermissionSize = mManifestPermissionName.size();
        if (remainManifestPermissionSize == 0) {
            processNextStep();
            return;
        }

        checkPermission(mManifestPermissionName.get(0), mPermissionDeniedMessage.get(0));
    }

    private void checkPermission(String permissionName, String needPermissionMessage) {
        if (ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("my app test " + permissionName + " already granted");
            if (mManifestPermissionName != null && mManifestPermissionName.size() > 0) {
                if (mManifestPermissionName.contains(permissionName)) {
                    mManifestPermissionName.remove(permissionName);
                }
            }
            checkPermissions();
            return;
        }
//        "권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다."
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
            // 처음 시작하면 여기 들어감.
            System.out.println("my app test else statement start");
            ActivityCompat.requestPermissions(this, new String[]{permissionName}, Constant.MY_PERMISSION_STORAGE);
            // 허용시 ==> onRequestPermissionsResult 함수로 들어감.
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
                        startActivityForResult(intent, Constant.PERMISSION_SETTING_REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.PERMISSION_SETTING_REQUEST_CODE) {
            checkPermissions();
        } else {
            appExit(R.string.permission_denied_exit);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (isPermissionDenied(requestCode, permissions, grantResults)) {
            Log.d("First App Installed", "My app test Permission Denied");
            // TODO 뷰처리
            appExit(R.string.permission_denied_exit);
            return;
        }

        Log.d("First App Installed", "My app test Permission Success");

        if (mManifestPermissionName.size() > 0) {
            mManifestPermissionName.remove(0);
            mPermissionDeniedMessage.remove(0);
        }

        checkPermissions();
    }

    private boolean isPermissionDenied(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != Constant.MY_PERMISSION_STORAGE) {
            Log.d("First App Installed", "My app test requestCode != my_permission_storage");
            return true;
        }

        if (grantResults.length <= 0) {
            Log.d("First App Installed", "My app test grantResults.length <= 0");
            return true;
        }

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.d("First App Installed", "My app test grantresults[0] != packagemanager.permission_granted");
            Log.d("First App Installed", "My app test grantresults[0] = " + grantResults[0] + ", packagemanager.permission_granted = " + PackageManager.PERMISSION_GRANTED);
            return true;
        }

        return false;
    }

    // TODO 뷰 처리
    private void appExit(int exitStringId) {
        Toast.makeText(this, exitStringId, Toast.LENGTH_LONG).show();
        finish();
    }

    private void convertFromCSVToDatabase() {
        mCSVFileReader = new CSVFileReader(getResources());
        mRegionDataCollections = mCSVFileReader.getLocationDataCollectionsFromCSVFile();
        mSQLiteDatabaseManager.createLocationCSVTable(mRegionDataCollections);
        mSQLiteDatabaseManager.showLocationDataListFromCSVTable();
        if (mCSVFileReader != null) {
            mCSVFileReader.close();
        }
        System.out.println("My app test : convertFromCSVToDatabase finish");
    }

    private void createTable() {
        mSQLiteDatabaseManager.createRemainLocationIdTable();
        mSQLiteDatabaseManager.createForecastInformationTable();
        mSQLiteDatabaseManager.createMyRegionIncludingCurrentLocationTable();
        System.out.println("My app test : createTable finish");
    }

    private void createUser(String userId) {
        mUserId = userId;
        System.out.println("My app test createUsr function in unique user id : " + mUserId);
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.createUser(mUserId).enqueue(createUserCallbackFunction);
    }

    private Callback<Integer> createUserCallbackFunction = new Callback<Integer>() {
        @Override
        public void onResponse(Call<Integer> call, Response<Integer> response) {
            if (!response.isSuccessful()) {
                System.out.println("My app test : response Fail at my id not succssful");
                return;
            }

            if (response.body().intValue() != 0) {
                System.out.println("My app test : response Fail at my ID");
                return;
            }

            PreferenceManager.setString(SplashActivity.this, Constant.USER_ID, mUserId);
            System.out.println("My app test : response Success my ID : " + mUserId);
            processNextStep();
        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
            System.out.println("My app test : response fail at onFailure, message : " + t.getMessage());
        }
    };

    private void registerCurrentLocation() {
        NetworkLocationInformation currentLocationInformationUsingNetwork = new NetworkLocationInformation(this);

        mCurrentLocationInfo = new LocationInfoForServer();
        String addressString = "";
        if (currentLocationInformationUsingNetwork.getNetworkAddress() == null) {
            addressString += "위치 정보 null";
            mCurrentLocationInfo = null;
        } else {
            Address currentAddress = currentLocationInformationUsingNetwork.getNetworkAddress();
            String fullBody = "";
            String fullAddressName = "";
            if (currentAddress == null) {
                fullBody = "current Address Full Body : No Name";
                fullAddressName = "full Address Name : No Name";
            } else {
                fullBody = currentAddress.toString();
                fullAddressName = currentAddress.getAddressLine(0);
            }
            mCurrentLocationInfo.setLongitudeHour(currentLocationInformationUsingNetwork.getLongitudeHour());
            mCurrentLocationInfo.setLongitudeMin(currentLocationInformationUsingNetwork.getLongitudeMinute());
            mCurrentLocationInfo.setLatitudeHour(currentLocationInformationUsingNetwork.getLatitudeHour());
            mCurrentLocationInfo.setLatitudeMin(currentLocationInformationUsingNetwork.getLatitudeMinute());
            mCurrentLocationName = Utils.getFormattedLocationNameFromFullName(fullAddressName);

            addressString += "fullBody : " + fullBody + ", fullAddressName : " + fullAddressName + ", longh : " + currentLocationInformationUsingNetwork.getLongitudeHour()
            + ", longm : " + currentLocationInformationUsingNetwork.getLongitudeMinute()
            + ", lath : " + currentLocationInformationUsingNetwork.getLatitudeHour()
            + ", latm : " + currentLocationInformationUsingNetwork.getLatitudeMinute()
            + ", formattedString : " + mCurrentLocationName;

//        step1 : 도 / 시
//        step2 : 구 / 군 / 시
//        step3 : 동 / 면 / 읍
//        예외 : 경상북도 울릉군 독도, 제주특별자치도 서귀포시 대정읍/마라도포함
        }

        Log.d("First App Installed", "My app test current address : " + addressString);

        postRequestRegisterCurrentLocation();
    }

    public void postRequestRegisterCurrentLocation() {
        if(mCurrentLocationInfo == null) {
            Log.d("First App Installed", "My app test 현재 지역 정보를 받아 올 수 없습니다.");
            startMainActivity();
            return;
        } else {
            RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
            restAPI.registerLocation(PreferenceManager.getString(this, Constant.USER_ID, mUserId), Constant.CURRENT_LOCATION_ID, mCurrentLocationInfo).enqueue(registerCurrentLocationCallbackFunction);
        }
    }

    private Callback<Integer> registerCurrentLocationCallbackFunction = new Callback<Integer>() {
        @Override
        public void onResponse(Call<Integer> call, Response<Integer> response) {
            Log.d("First App Installed", "My app test Start register location current here : " + response.toString());
            if(response.isSuccessful()) {
                if (response.body().intValue() == 0) {
                    Log.d("First App Installed", "My app test Start register location current success");
                    mCurrentLocationInfo.setRegionStep1(mCurrentLocationName);
                    mCurrentLocationInfo.setRegionStep2("");
                    mCurrentLocationInfo.setRegionStep3("");
                    mSQLiteDatabaseManager.addMyLocationDataIntoLocationIncludingCurrentLocationTable(new LocationInfo(mCurrentLocationInfo));
                    PreferenceManager.incrementMyLocationCount(SplashActivity.this);
                    processNextStep();
                } else {
                    Log.d("First App Installed", "My app test Start register location current successful but fail");
                }
            } else {
                Log.d("First App Installed", "My app test Start register location current not successful");
            }
        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
            Log.d("First App Installed", "My app test Start register location current fail at onfailure");
        }
    };

    public void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        ((Activity)mContext).finish();
        Log.d("asdf", "my app test finish splachactivity");
    }
}
