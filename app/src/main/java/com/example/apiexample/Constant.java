package com.example.apiexample;

import android.Manifest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constant {
    public static final String REGION_DATA_DATABASE_NAME = "REGION_DATA_DATABASE_NAME";
    public static final String REGION_DATA_FROM_CSV_TABLE_NAME = "REGION_DATA_FROM_CSV_TABLE_NAME";
    public static final String REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME = "REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME";
    public static final String REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME = "REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME";
    public static final String FORECAST_INFORMATION_TABLE_NAME = "FORECAST_INFORMATION_TABLE_NAME";
    public static final String INITIALIZE_FINISHED_LAST_STEP = "INITIALIZE_FINISHED_LAST_STEP";
    //    public static final String SERVER_BASE_URL = "https://192.168.6.36:8443/";
    public static final String SERVER_BASE_URL = "https://oneulbionya.ga:8443/";
//    public static final String SERVER_BASE_URL = "https://oneulbionya.ga:8443/";

    public static final String GET_FORECAST_INFORMATION_WORK_TAG = "GET_FORECAST_INFORMATION_WORK_TAG";

    public static final String SHARED_PREFERENCES_NAME = "OQERNGLKASNDFASEROIGSADF";
    public static final String IS_CSV_CONVERTED_TO_DATABASE_FINISHED = "IS_CSV_CONVERTED_TO_DATABASE_FINISHED";
    public static final String IS_INITIATION_FINISHED = "IS_INITIATION_FINISHED";
    public static final String IS_GET_USER_ID_FINISHED = "IS_GET_USER_ID_FINISHED";
    public static final String IS_CREATE_MY_REGION_TABLE_FINISHED = "IS_CREATE_MY_REGION_TABLE_FINISHED";
    public static final String IS_CREATE_LOCATION_ID_TABLE_FINISHED = "IS_CREATE_LOCATION_ID_TABLE_FINISHED";
    public static final String IS_CREATE_FORECAST_INFORMATION_TABLE_FINISHED = "IS_CREATE_FORECAST_INFORMATION_TABLE_FINISHED";
    public static final String IS_GRANT_PERMISSION_FINISHED = "IS_GRANT_PERMISSION_FINISHED";
    public static final String GRANT_PERMISSION_COUNT = "GRANT_PERMISSION_COUNT";
    public static final String USER_ID = "USER_ID";
    public static final String CHANNEL_ID = "CHANNEL_ID";

    public static final String MY_REGISTERED_LOCATION_COUNT_INCLUDING_CURRENT_LOCATION = "MY_REGISTERED_LOCATION_COUNT_INCLUDING_CURRENT_LOCATION";
    public static final int MAX_LOCATION_INCLUDING_CURRENT_LOCATION_COUNT = 6;
    public static final int MAX_MY_ONLY_ADDED_LOCATION_COUNT = 5;
    public static final Integer CREATE_ID_FAIL_NUMBER = -1234;
    public static final int LOCATION_ID_NOT_EXIST = -5764;
    public static final int MY_PERMISSION_STORAGE = 65487;
    public static final int ADD_LOCATION_REQUEST_CODE = 1;
    public static final int ADD_LOCATION_RESULT_SUCCESS_CODE = 2;
    public static final int CHANGE_LOCATION_REQUEST_CODE = 3;
    public static final int CHANGE_LOCATION_REQUEST_SUCCESS_CODE = 4;
    public static final int PERMISSION_SETTING_REQUEST_CODE = 1597;
    public static final int GRANTED_PERMISSION_NEEDED_COUNT = 1;
    public static final int CURRENT_LOCATION_ID = 0;
    public static final int NOT_EXIST_REGION_COORDINATE = -1;
    public static final long DUPLICATE_CLICK_IGNORE_DELAY_MILLIS = 500;
    public static final float EDIT_MODE_TEXT_TRANSITION_DISTANCE_DP = 25f;
    public static final float EDIT_MODE_BUTTON_TRANSITION_DISTANCE_DP = 45f;
    public static final float EDIT_MODE_RAINY_STATE_LAYOUT_TRANSITION_DISTANCE_DP = 50f;
    public static final int EDIT_MODE_TEXT_TRANSITION_DURATION = 300;
    public static final float EDIT_MODE_RAINY_STATE_MARGIN_RIGHT_AFTER_MOVE_DP = 10f;
    public static final float EDIT_MODE_RAINY_STATE_MARGIN_RIGHT_BEFORE_MOVE_DP = 50f;
    public static final int GET_FORECAST_INFORMATION_PERIOD_MINUTE = 15;
    public static final int NOTIFY_ID = 124521;

    public static final String INITIALIZE_STEP_PERMISSION = "INITIALIZE_STEP_PERMISSION";
    public static final String INITIALIZE_STEP_CONVERT_CSV = "INITIALIZE_STEP_CONVERT_CSV";
    public static final String INITIALIZE_STEP_CREATE_TABLE = "INITIALIZE_STEP_CREATE_TABLE";
    public static final String INITIALIZE_STEP_CREATE_USER = "INITIALIZE_STEP_CREATE_USER";
    public static final String INITIALIZE_STEP_CURRENT_LOCATION_REGISTER = "INITIALIZE_STEP_CURRENT_LOCATION_REGISTER";
    public static final String INITIALIZE_STEP_GET_FORECAST_INFORMATION = "INITIALIZE_STEP_GET_FORECAST_INFORMATION";

    public static final List<String> INITIALIZE_STEP_WORKS = Collections.unmodifiableList(
                    new ArrayList<String>() {
                        {
                            add(INITIALIZE_STEP_PERMISSION);
                            add(INITIALIZE_STEP_CONVERT_CSV);
                            add(INITIALIZE_STEP_CREATE_TABLE);
                            add(INITIALIZE_STEP_CREATE_USER);
                            add(INITIALIZE_STEP_CURRENT_LOCATION_REGISTER);
                            add(INITIALIZE_STEP_GET_FORECAST_INFORMATION);
                        }
                    });

    // R.string 에 추가?
    public static final String[] MANIFEST_PERMISSION_NAMES = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // R.string 에 추가?
    public static final String[] PERMISSION_DENIED_MESSAGES = {
            "위치 조회 권한이 거부되었습니다. 허용해주세요."
    };
}
