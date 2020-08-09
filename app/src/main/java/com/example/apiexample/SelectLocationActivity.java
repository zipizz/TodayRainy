package com.example.apiexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLocationActivity extends AppCompatActivity {

    private int mSelectedLocationId = Constant.LOCATION_ID_NOT_EXIST;
    private LocationInfo mSelectedLocationInfo = null;
    private SQLiteDatabaseManager mSQLiteDatabaseManager = null;
    private String mUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
//        mListView = (ListView)findViewById(R.id.select_location_list_view);

        mSQLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
        mUserId = PreferenceManager.getString(this, Constant.USER_ID);
        FragmentList fragmentRegionStepOne = new FragmentList(1, "", this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.select_location_fragment_container, fragmentRegionStepOne).commit();
    }


//    public void deleteRequestDeleteLocation(int locationId) {
//        mSelectedLocationId = locationId;
//        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
//        restAPI.deleteLocation(mUserId, locationId).enqueue(deleteLocationCallback);
//    }

    protected void setSelectedLocationInfo(LocationInfo selectedLocationInfo) {
        System.out.println("My app test setSelectedLocationInfo function in");
        this.mSelectedLocationInfo = selectedLocationInfo;
    }

    public void postRequestAddLocation() {
        System.out.println("My app test postRequestAddLocation function in");
        mSelectedLocationId = mSQLiteDatabaseManager.getLocationIdMin();
        if (mSelectedLocationId == Constant.LOCATION_ID_NOT_EXIST) {
            System.out.println("My app test add location fail because not exist usable location id any more");
            return;
        }

        if (mSelectedLocationInfo == null) {
            System.out.println("My app test add location fail because selected region not exist");
            return;
        }

        if (mSQLiteDatabaseManager.hasLocation(mSelectedLocationInfo)) {
            System.out.println("My app test add location fail because Already region exist");
            Toast.makeText(this, "이미 추가된 지역입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("My app test add location : userid : " + mUserId + ", locationId : " + mSelectedLocationId + ", locationInfo : " + mSelectedLocationInfo.toString());
        LocationInfoForServer a = new LocationInfoForServer();
        a.setRegionStep1(mSelectedLocationInfo.getRegionStep1());
        a.setRegionStep2(mSelectedLocationInfo.getRegionStep2());
        a.setRegionStep3(mSelectedLocationInfo.getRegionStep3());

        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.AddLocation(mUserId, mSelectedLocationId, a).enqueue(addLocationCallbackFunction);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.select_location_fragment_container, fragment).addToBackStack(null).commit();
        setCurrentFragmentRegionStep(((FragmentList)fragment).getRegionStep());
    }

    private Callback<Integer> addLocationCallbackFunction = new Callback<Integer>() {
        @Override
        public void onResponse(Call<Integer> call, Response<Integer> response) {
            if (response.isSuccessful()) {
                int responseCode = response.body().intValue();
                if(responseCode == 0) {
                    System.out.println("My app test add location : response code success");
                    mSelectedLocationInfo.setLocationId(mSelectedLocationId);
                    mSQLiteDatabaseManager.addMyRegionData(mSelectedLocationInfo);
                    mSQLiteDatabaseManager.deleteLocationId(mSelectedLocationId);
                    PreferenceManager.incrementMyRegionCount(SelectLocationActivity.this);
                    Intent intent = new Intent();
                    intent.putExtra("hasAddedRegion", true);
                    setResult(Constant.ADD_LOCATION_RESULT_SUCCESS_CODE, intent);
                    finish();
                } else {
                    System.out.println("My app test add location : response code fail");
                }
            } else {
                System.out.println("My app test add location : response fail not successful");
            }

            System.out.println("My app test add location here response.body() is : " + response.body());
            System.out.println("My app test add location here response is : " + response.toString());
        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
            System.out.println("My app test add location : location add Fail 3");
        }
    };

    private int currentFragmentRegionStep = 1;

    public void setCurrentFragmentRegionStep(int currentFragmentRegionStep) {
        this.currentFragmentRegionStep = currentFragmentRegionStep;
    }
}