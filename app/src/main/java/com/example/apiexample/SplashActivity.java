package com.example.apiexample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
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
    private String mCurrentLocationName = "";
    private ArrayList<LocationInfo> mRegionDataCollections = null;
    private LocationInfoForServer mCurrentLocationInfo = null;

    private ArrayList<String> mManifestPermissionName = null;
    private ArrayList<String> mPermissionDeniedMessage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initiateVariable();

//        mCSVFileReader = new CSVFileReader(getResources());
//        mRegionDataCollections = mCSVFileReader.getRegionRealDataCollectionsFromCSVFile();
//
//        Geocoder g = new Geocoder(this);
//
//        for (LocationInfo locationInfo : mRegionDataCollections) {
//            Address realAddress = null;
//            try {
//                List<Address> a = g.getFromLocation(locationInfo.getLatitudeReal(), locationInfo.getLongitudeReal(), 1);
//                String bb = "";
//                if (a.size() > 0) {
//                    realAddress = a.get(0);
//                    bb += "inter : " + realAddress.getAddressLine(0);
////                    System.out.println("inter : " + realAddress.getAddressLine(0));
//                } else {
//                    bb += "not exist : " + locationInfo.toString();
////                    System.out.println("not exist : " + locationInfo.toString());
//                }
//
//                bb += ", excel : " + locationInfo.toString();
////                System.out.println("excel : " + locationInfo.toString());
//                System.out.println(bb + "\n");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        CurrentLocationInformation currentLocationInformation = new CurrentLocationInformation(this);
//        String a = "";
//        String b = "";
//        if (currentLocationInformation.getGPSAddress() == null) {
//            a = "null 나왔다.";
//            b = "위도경도없다.";
//        } else {
//            a = currentLocationInformation.getGPSAddress().toString();
//            b = "current longhour : " + (currentLocationInformation.getLongitudeHour())
//                    + ", longmin : " + (currentLocationInformation.getLongitudeMinute())
//                    + ", latHOur : " + (currentLocationInformation.getLatitudeHour())
//                    + ", latMin : " + (currentLocationInformation.getLongitudeMinute());
//
//            String fullAddressName = currentLocationInformation.getGPSAddress().getAddressLine(0);
//        }
//        System.out.println("fulladdress : " + a);
//        System.out.println("위도경도 : " + b);

        // cur : 127 61 / 37 6
//        for (LocationInfo locationInfo : mRegionDataCollections) {
//            if (locationInfo.getLongitudeHour() != currentLocationInformation.getLatitudeHour()) {
//                continue;
//            }
//
//            if (locationInfo.getLongitudeMin() != currentLocationInformation.getLongitudeMinute()) {
//                continue;
//            }
//
//            if (locationInfo.getLatitudeHour() != currentLocationInformation.getLatitudeHour()) {
//                continue;
//            }
//
//            if (locationInfo.getLatitudeMin() != currentLocationInformation.getLatitudeMinute()) {
//                continue;
//            }
//
//            System.out.println("locinfo : " + locationInfo.toString() + ", curentLocInfo : " + currentLocationInformation.getGPSAddress().getAddressLine(0));
//            break;
//        }


//        step1 : 도 / 시
//        step2 : 구 / 군 / 시
//        step3 : 동 / 면 / 읍
//        예외 : 경상북도 울릉군 독도, 제주특별자치도 서귀포시 대정읍/마라도포함

//            mCurrentLocationInfo.setRegionStep1();
//
//        int doCount = 0;
//        int siCount = 0;
//        int guCount = 0;
//        int goonCount = 0;
//        int step2SiCount = 0;
//        int step2EmptyCount = 0;
//        int step3EmptyCount = 0;
//        int dongCount = 0;
//        int myeonCount = 0;
//        int eupCount = 0;
//        int elseCount = 0;

//        for (LocationInfo locationInfo : mRegionDataCollections) {
//            String step1 = locationInfo.getRegionStep1();
//            String step2 = locationInfo.getRegionStep2();
//            String step3 = locationInfo.getRegionStep3();
//            if (step1.endsWith("도")) {
//                doCount++;
////                guCount : 450, goonCount : 860, step2SiCount : 1189
//            } else if (step1.endsWith("시")) {
////                guCount : 1176, goonCount : 51, step2SiCount : 20
//            } else {
//                System.out.println("step1 : " + locationInfo.toString());
//                elseCount++;
//            }
//        }

//        siCount++;
//        if (step2.isEmpty()) {
//
//        } else if (step2.endsWith("구")) {
//            guCount++;
//        } else if (step2.endsWith("군")) {
//            goonCount++;
//        } else if (step2.endsWith("시")) {
//            step2SiCount++;
//        } else {
//            System.out.println("step1 도 case : " + locationInfo.toString());
//            elseCount++;
//        }

//        for (LocationInfo locationInfo : mRegionDataCollections) {
//            String step1 = locationInfo.getRegionStep1();
//            String step2 = locationInfo.getRegionStep2();
//            String step3 = locationInfo.getRegionStep3();
//            if (step2.isEmpty()) {
//                step2EmptyCount++;
//            } else if (step2.endsWith("구")) {
//                guCount++;
//            } else if (step2.endsWith("군")) {
//                goonCount++;
//            } else if (step2.endsWith("시")) {
//                step2SiCount++;
//            } else {
//                System.out.println("step2 : " + locationInfo.toString());
//                elseCount++;
//            }
//        }
//
//        for (LocationInfo locationInfo : mRegionDataCollections) {
//            String step1 = locationInfo.getRegionStep1();
//            String step2 = locationInfo.getRegionStep2();
//            String step3 = locationInfo.getRegionStep3();
//            if (step3.isEmpty()) {
//                step3EmptyCount++;
//            } else if (step3.endsWith("동")) {
//                dongCount++;
//            } else if (step3.endsWith("면")) {
//                myeonCount++;
//            } else if (step3.endsWith("읍")) {
//                eupCount++;
//            } else {
//                System.out.println("step3 : " + locationInfo.toString());
//                elseCount++;
//            }
//        }

//        System.out.println("doCount : " + doCount + ", siCount : " +siCount);
//        System.out.println("guCount : " + guCount + ", goonCount : " +goonCount + ", step2SiCount : " +step2SiCount);
//        System.out.println("dongCount : " + dongCount + ", myeonCount : " +myeonCount + ", eupCount : " +eupCount);
//        System.out.println("step2EmptyCount : " + step2EmptyCount + ", step3EmptyCount : " +step3EmptyCount + ", elseCount : " +elseCount);

        boolean isAlreadyInitiateFinished = initiateFirstAppInstalled();
        startMainActivity(isAlreadyInitiateFinished);
    }

    private void initiateVariable() {
        new RestAPIInstance(this);
        SQLiteDatabaseManager.initializeInstance(this);
        mSQLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
        mUserId = PreferenceManager.getString(this, Constant.USER_ID, "NO_ID");
        mManifestPermissionName = new ArrayList<>();
        mPermissionDeniedMessage = new ArrayList<>();
        mManifestPermissionName.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        mPermissionDeniedMessage.add("위치 조회 권한이 거부되었습니다. 허용해주세요.");
    }

    private boolean initiateFirstAppInstalled() {
        Log.d("First App Installed", "My app test 1: Start Initiation");

        if (PreferenceManager.getBoolean(this, Constant.IS_INITIATION_FINISHED, false)) {
            Log.d("First App Installed", "My app test initiate Already Finish");
            return true;
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_GRANT_PERMISSION_FINISHED, false)) {
            Log.d("First App Installed", "My app test request permissioned start");
            checkPermissions();
        } else {
            Log.d("First App Installed", "My app test request permissioned alreday finish");
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
            mSQLiteDatabaseManager.createRemainLocationIdTable();
            PreferenceManager.setBoolean(this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test Already Finished : Create Location id table");
        }

        setIsFinished();
        return false;
    }

    private void setIsFinished() {
        if (PreferenceManager.getBoolean(this, Constant.IS_CSV_CONVERTED_TO_DATABASE_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_GET_USER_ID_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_CREATE_LOCATION_ID_TABLE_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_CREATE_FORECAST_INFORMATION_TABLE_FINISHED, false)
                && PreferenceManager.getBoolean(this, Constant.IS_GRANT_PERMISSION_FINISHED, false)
        ) {
            PreferenceManager.setBoolean(this, Constant.IS_INITIATION_FINISHED, true);
            Log.d("First App Installed", "My app test 7 sync: Now Initiation finished");
        } else {
            Log.d("First App Installed", "My app test 7 sync: not ok at initiation");
        }
    }

    private void checkPermissions() {
        int remainManifestPermissionSize = mManifestPermissionName.size();
        if (remainManifestPermissionSize == 0) {
            return;
        }

        checkPermission(mManifestPermissionName.get(0), mPermissionDeniedMessage.get(0));
    }

    private void checkPermission(String permissionName, String needPermissionMessage){
        if (ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("my app test grant alreday");
            return;
        }
//        "권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다."
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
            // 처음 시작하면 여기 들어감.
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("First App Installed", "My app test Constant.requestCode : " + Constant.MY_PERMISSION_STORAGE + ", received requestCode : " + requestCode);
        Log.d("First App Installed", "My app test permissions.length : " + permissions.length);
        for (String permission : permissions) {
            Log.d("First App Installed", "My app test permission : " + permission);
        }

        Log.d("First App Installed", "My app test permission_granted grantResult : " + PackageManager.PERMISSION_GRANTED);
        Log.d("First App Installed", "My app test grantResults.length : " + grantResults.length);
        for (int grantResult : grantResults) {
            Log.d("First App Installed", "My app test grantResult : " + grantResult);
        }

        if (requestCode != Constant.MY_PERMISSION_STORAGE) {
            Log.d("First App Installed", "My app test requestCode != my_permission_storage");
            return;
        }

        if (grantResults.length <= 0) {
            Log.d("First App Installed", "My app test grantResults.length <= 0");
            return;
        }

        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.d("First App Installed", "My app test grantresults[0] != packagemanager.permission_granted");
            return;
        }

        Log.d("First App Installed", "My app test Permission Success");
        PreferenceManager.incrementGrantedPermissionCount(this);

        if (mManifestPermissionName.size() > 0) {
            mManifestPermissionName.remove(0);
            mPermissionDeniedMessage.remove(0);
        }

        if (mManifestPermissionName.size() > 0) {
            checkPermissions();
            return;
        }

        if (PreferenceManager.getInt(this, Constant.GRANT_PERMISSION_COUNT) == Constant.GRANTED_PERMISSION_NEEDED_COUNT) {
            PreferenceManager.setBoolean(this, Constant.IS_GRANT_PERMISSION_FINISHED, true);
        } else {
            PreferenceManager.setBoolean(this, Constant.IS_GRANT_PERMISSION_FINISHED, false);
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_GRANT_PERMISSION_FINISHED, false)) {
            return;
        }

        if (!PreferenceManager.getBoolean(this, Constant.IS_GET_USER_ID_FINISHED, false)) {
            String myUserId = Utils.getUniqueId();
            Log.d("First App Installed", "My app test 2: Start GET USER ID : " + myUserId);
            createUser(myUserId);
        } else {
            Log.d("First App Installed", "My app test : Already GET USER ID : " + PreferenceManager.getString(this, Constant.USER_ID));
        }
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
            mSQLiteDatabaseManager.createMyRegionIncludingCurrentLocationTable();
            PreferenceManager.setBoolean(this, Constant.IS_CREATE_MY_REGION_TABLE_FINISHED, true);
        } else {
            Log.d("First App Installed", "My app test Already Finished : Create my region table ");
        }
    }

    private void convertFromCSVToDatabase() {
        mCSVFileReader = new CSVFileReader(getResources());
        mRegionDataCollections = mCSVFileReader.getRegionDataCollectionsFromCSVFile();
        mSQLiteDatabaseManager.createRegionCSVTable(mRegionDataCollections);
        mSQLiteDatabaseManager.showRegionDataListFromCSVTable();
    }

    private void AddCurrentLocation() {
        CurrentLocationInformation currentLocationInformation = new CurrentLocationInformation(this);

        mCurrentLocationInfo = new LocationInfoForServer();
        String addressString = "";
        if (currentLocationInformation.getGPSAddress() == null) {
            addressString += "위치 정보 null";
            mCurrentLocationInfo.setLongitudeHour(Constant.NOT_EXIST_REGION_COORDINATE);
            mCurrentLocationInfo.setLongitudeMin(Constant.NOT_EXIST_REGION_COORDINATE);
            mCurrentLocationInfo.setLatitudeHour(Constant.NOT_EXIST_REGION_COORDINATE);
            mCurrentLocationInfo.setLongitudeMin(Constant.NOT_EXIST_REGION_COORDINATE);
            mCurrentLocationName = "대한민국";
//            mCurrentLocationInfo.setRegionStep1("경기도");
//            mCurrentLocationInfo.setRegionStep2("성남시분당구");
//            mCurrentLocationInfo.setRegionStep3("금곡동");
        } else {
            Address currentAddress = currentLocationInformation.getGPSAddress();
            String fullBody = "";
            String fullAddressName = "";
            if (currentAddress == null) {
                fullBody = "current Address Full Body No Name";
                fullAddressName = "full Address Name No Name";
            } else {
                fullBody = currentAddress.toString();
                fullAddressName = currentAddress.getAddressLine(0);
            }
//            mCurrentLocationInfo.setLongitudeHour(currentLocationInformation.getLongitudeHour());
//            mCurrentLocationInfo.setLongitudeMin(currentLocationInformation.getLongitudeMinute());
//            mCurrentLocationInfo.setLatitudeHour(currentLocationInformation.getLatitudeHour());
//            mCurrentLocationInfo.setLongitudeMin(currentLocationInformation.getLongitudeMinute());
            mCurrentLocationInfo.setRegionStep1("경기도");
            mCurrentLocationInfo.setRegionStep2("용인시수지구");
            mCurrentLocationInfo.setRegionStep3("죽전2동");

            mCurrentLocationName = Utils.getFormattedLocationNameFromFullName(fullAddressName);

            addressString += "fullBody : " + fullBody + ", fullAddressName : " + fullAddressName + ", longh : " + currentLocationInformation.getLongitudeHour()
            + ", longm : " + currentLocationInformation.getLongitudeMinute()
            + ", lath : " + currentLocationInformation.getLatitudeHour()
            + ", latm : " + currentLocationInformation.getLatitudeMinute()
            + ", formattedString : " + mCurrentLocationName;


//        step1 : 도 / 시
//        step2 : 구 / 군 / 시
//        step3 : 동 / 면 / 읍
//        예외 : 경상북도 울릉군 독도, 제주특별자치도 서귀포시 대정읍/마라도포함

//            mCurrentLocationInfo.setRegionStep1();
        }

        Log.d("First App Installed", "My app test current address : " + addressString);

        //TODO 서버 복구 후 아래 한 줄 주석 풀기
        postRequestAddCurrentLocation();




        //TODO 서버 복구후 아래 코드 삭제
//        mCurrentLocationInfo.setRegionStep1("경기도");
//        mCurrentLocationInfo.setRegionStep2("성남시분당구");
//        mCurrentLocationInfo.setRegionStep3("금곡동");
//        mSQLiteDatabaseManager.addMyRegionDataIntoRegionIncludingCurrentLocationTable(new LocationInfo(mCurrentLocationInfo));
//        createForecastInformationTable();
//        SQLiteDatabaseManager.getInstance().updateForecastInformationTable(new ForecastInformation("1", "2.0", "2.0", mUserId, 0));
//        setIsFinished();
//        if (PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_INITIATION_FINISHED, false)) {
//            close();
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
        //TODO 서버 복구 후 위 코드 삭제
    }

    public void postRequestAddCurrentLocation() {
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
                    mCurrentLocationInfo.setRegionStep1(mCurrentLocationName);
                    mCurrentLocationInfo.setRegionStep2("");
                    mCurrentLocationInfo.setRegionStep3("");
                    mSQLiteDatabaseManager.addMyRegionDataIntoRegionIncludingCurrentLocationTable(new LocationInfo(mCurrentLocationInfo));
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

                setIsFinished();
                if (PreferenceManager.getBoolean(SplashActivity.this, Constant.IS_INITIATION_FINISHED, false)) {
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
