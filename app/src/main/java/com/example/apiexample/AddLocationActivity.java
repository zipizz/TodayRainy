package com.example.apiexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddLocationActivity extends AppCompatActivity {

    private ListView mListView = null;
    private AddLocationListViewAdapter mAddLocationListViewAdapter = null;
    private EditLocationOrderListViewAdapter mEditLocationOrderListViewAdapter = null;
    private ArrayList<LocationInfo> mCustomArrayList = new ArrayList<LocationInfo>();
    private TextView mEditOrderButton = null;
    private Button mAddLocationButton = null;
    private ArrayList mSelectedDeleteLocationId = new ArrayList();
    private int mDeleteLocationId = -1;
    private TextView mSelectedLocationCountView = null;
    private boolean mIsEditMode = false;
    private boolean mIsEditButtonClickedAtLeastOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        mCustomArrayList = SQLiteDatabaseManager.getInstance().getMyRegionIncludingCurrentLocationList(false);
        if (mCustomArrayList.size() == 0) {
            Toast.makeText(this, "지역이 없네요. 추가해주세요", Toast.LENGTH_SHORT).show();
        }

        mListView = (ListView) findViewById(R.id.listofmyregion);
        mAddLocationListViewAdapter = new AddLocationListViewAdapter(this, mCustomArrayList, this);
        mEditLocationOrderListViewAdapter = new EditLocationOrderListViewAdapter(this, mCustomArrayList, this);
        mSelectedLocationCountView = (TextView) findViewById(R.id.selected_location_count);
        mListView.setAdapter(mAddLocationListViewAdapter);
//        getRequestGetLocalWeather();

        ConstraintLayout view = (ConstraintLayout)findViewById(R.id.delete_constraint);
        view.setVisibility(View.INVISIBLE);
        mAddLocationButton = ((Button) findViewById(R.id.add_location_btn));
        mEditOrderButton = ((TextView) findViewById(R.id.thirdImage));
        mEditOrderButton.setText("편집");
        mEditOrderButton.setTag(R.drawable.ic_edit_24px);

        findViewById(R.id.btn_back_at_add_location_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddLocationActivity.super.onBackPressed();
            }
        });
        mEditOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsEditButtonClickedAtLeastOnce = true;
                if ((int) mEditOrderButton.getTag() == R.drawable.ic_edit_24px) {
                    mEditOrderButton.setText("완료");
                    mEditOrderButton.setTag(R.drawable.ic_check_black_24dp);
                    mListView.setAdapter(mEditLocationOrderListViewAdapter);
                    mAddLocationButton.setVisibility(View.INVISIBLE);
                    mIsEditMode = true;
                } else {
                    doAfterEditFinished();
                }
            }
        });

        mAddLocationButton.setOnClickListener(new OnCustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (PreferenceManager.getInt(AddLocationActivity.this, Constant.MY_REGISTERED_LOCATION_COUNT_INCLUDING_CURRENT_LOCATION) < Constant.MAX_LOCATION_INCLUDING_CURRENT_LOCATION_COUNT) {
                    Intent intent = new Intent(AddLocationActivity.this, SelectLocationActivity.class);
                    startActivityForResult(intent, Constant.ADD_LOCATION_REQUEST_CODE);
//                    startActivity(intent);
                } else {
                    Toast.makeText(AddLocationActivity.this, "지역을 더 이상 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((ConstraintLayout) findViewById(R.id.delete_touch_area)).setOnClickListener(new OnCustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                deleteLocation();
            }
        });
    }

    private void doAfterEditFinished() {
        mEditLocationOrderListViewAdapter.doAfterEditFinishedAtAdapter();
        doAfterEditFinishedAtAddLocationActivity();
    }

    public void doAfterEditFinishedAtAddLocationActivity() {
        slideClear();
        clearDeletedLocationIdList();
        mEditOrderButton.setText("편집");
        mEditOrderButton.setTag(R.drawable.ic_edit_24px);
        mListView.setAdapter(mAddLocationListViewAdapter);
        mAddLocationButton.setVisibility(View.VISIBLE);
        mIsEditMode = false;
    }

    protected boolean isEditButtonClickedAtLeastOnce () {
        return mIsEditButtonClickedAtLeastOnce;
    }

    public void slideClear() {
        System.out.println("my app test slide clear");

        View view = (View)findViewById(R.id.delete_constraint);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight() * 2,                 // fromYDelta
                view.getHeight() * 2); // toYDelta
        animate.setDuration(1);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideUp(){
        System.out.println("my app test slideup");
        View view = (View)findViewById(R.id.delete_constraint);
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight()  * 2,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(){
        System.out.println("my app test slide down");
        View view = (View)findViewById(R.id.delete_constraint);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight() * 2); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    protected void clearDeletedLocationIdList () {
        mSelectedDeleteLocationId = new ArrayList();
        mDeleteLocationId = -1;
    }

    @Override
    public void onBackPressed() {
        if(mIsEditMode) {
            doAfterEditFinished();
        } else {
            super.onBackPressed();
        }

    }

    private void deleteLocation() {

        System.out.println("My app test delete function in size : " + mSelectedDeleteLocationId.size());
        for (int i = 0; i < mSelectedDeleteLocationId.size(); i++) {
            System.out.println("My app test delete function array[" + i + "] = " + mSelectedDeleteLocationId.get(i));
        }

        if (mSelectedDeleteLocationId.size() == 0) {
            mCustomArrayList = SQLiteDatabaseManager.getInstance().getMyRegionIncludingCurrentLocationList(false);
            mAddLocationListViewAdapter = new AddLocationListViewAdapter(this, mCustomArrayList, this);
            mAddLocationListViewAdapter.notifyDataSetChanged();
            mEditLocationOrderListViewAdapter = new EditLocationOrderListViewAdapter(this, mCustomArrayList, this);
            mEditLocationOrderListViewAdapter.notifyDataSetChanged();

            doAfterEditFinished();
            Intent intent = new Intent();
            intent.putExtra("locationChanged", true);
            setResult(Constant.CHANGE_LOCATION_REQUEST_SUCCESS_CODE, intent);
            return;
        }

        mDeleteLocationId = (int) mSelectedDeleteLocationId.get(0);
        mSelectedDeleteLocationId.remove(0);
        RestAPI restAPI = RestAPIInstance.getInstance().create(RestAPI.class);
        restAPI.deleteLocation(PreferenceManager.getString(AddLocationActivity.this, Constant.USER_ID), mDeleteLocationId).enqueue(deleteLocationCallback);
    }

    private Callback<Integer> deleteLocationCallback = new Callback<Integer>() {
        @Override
        public void onResponse(Call<Integer> call, Response<Integer> response) {
            if (response.isSuccessful()) {
                int responseCode = response.body().intValue();
                if(responseCode == 0) {
                    System.out.println("My app test delete location : response code success");
                    SQLiteDatabaseManager.getInstance().deleteMyRegionDataFromRegionIncludingCurrentLocationTable(mDeleteLocationId);
                    SQLiteDatabaseManager.getInstance().addLocationIdIntoRemainLocationIdTable(mDeleteLocationId);
                    PreferenceManager.decrementMyLocationCount(AddLocationActivity.this);
                    deleteLocation();
                } else {
                    System.out.println("My app test delete location : response code fail");
                }
            } else {
                System.out.println("My app test delete location : response fail not successful");
            }
        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
            System.out.println("My app test delete location : fail at onFailure");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.ADD_LOCATION_REQUEST_CODE) {
            if (resultCode == Constant.ADD_LOCATION_RESULT_SUCCESS_CODE) {
                if(!data.getExtras().getBoolean("hasAddedRegion")) {
                    System.out.println("my app test : hasAddedRegion not");
                    return;
                };
                System.out.println("my app test : hasAddedRegion yes!");

                mCustomArrayList = SQLiteDatabaseManager.getInstance().getMyRegionIncludingCurrentLocationList(false);

                System.out.println("my app test : array start size : " + mCustomArrayList.size());
                for (int i = 0; i < mCustomArrayList.size(); i++) {
                    System.out.println("my app test [" + i + "] : " + mCustomArrayList.get(i).toString());
                }

                mAddLocationListViewAdapter = new AddLocationListViewAdapter(this, mCustomArrayList, this);
                mEditLocationOrderListViewAdapter = new EditLocationOrderListViewAdapter(this, mCustomArrayList, this);
                mListView.setAdapter(mAddLocationListViewAdapter);

                Intent intent = new Intent();
                intent.putExtra("locationChanged", true);
                setResult(Constant.CHANGE_LOCATION_REQUEST_SUCCESS_CODE, intent);
            } else {

            }
        }
    }

    public void addDeletedLocationId(int locationId) {
        mSelectedDeleteLocationId.add(locationId);
    }

    public void removeDeletedLocationId(int locationId) {
        mSelectedDeleteLocationId.remove(new Integer(locationId));
    }

    public void setSelectedCountText(int touchCount) {
        System.out.println("my app test touchCount : " + mSelectedLocationCountView.getText());
        if (touchCount == 0) {
            return;
        }
        mSelectedLocationCountView.setText(touchCount + "개 선택됨");
    }
}