package com.example.apiexample;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TextViewPagerAdapter extends PagerAdapter {

    private Context mContext = null;
    private Retrofit mRetrofit = null;
    private MainActivity parentActivity = null;
    private View mView = null;
    private int mLocationCount = 0;
    private ArrayList<LocationInfo> mMyLocationInfoList = new ArrayList<LocationInfo>();

    public TextViewPagerAdapter () {
    }

    public TextViewPagerAdapter(Context context, MainActivity parentActivity) {
        this.mContext = context;
        this.parentActivity = parentActivity;
        this.mLocationCount = PreferenceManager.getInt(parentActivity, Constant.MY_REGION_COUNT) + 1;
        this.mMyLocationInfoList = SQLiteDatabaseManager.getInstance().getMyAddedRegionDataList(false);
        this.mRetrofit = RestAPIInstance.getInstance();
    }

    @Override
    public int getCount() {
        return this.mLocationCount;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    protected void setDataChanged() {
        this.mMyLocationInfoList = SQLiteDatabaseManager.getInstance().getMyAddedRegionDataList(false);
        this.mLocationCount = PreferenceManager.getInt(parentActivity, Constant.MY_REGION_COUNT) + 1;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflater.inflate(R.layout.page, container, false);

            mView.findViewById(R.id.hereThirdImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddLocationActivity.class);
                    parentActivity.startActivityForResult(intent, Constant.CHANGE_LOCATION_REQUEST_CODE);

//                    AddLocationFragment addLocationFragment = new AddLocationFragment(mContext);
//                    FragmentTransaction fragmentTransaction = parentActivity.getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.add(R.id.favorite_frame, addLocationFragment).commit();
//                    fragmentTransaction.replace(R.id.hello_fragment_frame, addLocationFragment).commit();
                }
            });

            LocationInfo locationInfo = mMyLocationInfoList.get(position);
            if (position == 0) {
                mView.findViewById(R.id.isCurrentLocation).setVisibility(View.VISIBLE);
            } else {
                mView.findViewById(R.id.isCurrentLocation).setVisibility(View.INVISIBLE);
            }

            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String str = dayTime.format(new Date(System.currentTimeMillis()));

            ((TextView) mView.findViewById(R.id.currentLocation)).setText(locationInfo.toString());
            ((TextView)mView.findViewById(R.id.updateTime)).setText(str);

            ForecastInformation forecastInformation = SQLiteDatabaseManager.getInstance().getForcastInformation(locationInfo.getLocationId());

            if (forecastInformation.isRainy()) {
                ((TextView)mView.findViewById(R.id.rain_text)).setText("비 온다");
                ((ImageView)mView.findViewById(R.id.imageCenter)).setImageResource(R.mipmap.ic_launcher_round);
            } else {
                ((TextView)mView.findViewById(R.id.rain_text)).setText("비 안 온다");
                ((ImageView)mView.findViewById(R.id.imageCenter)).setImageResource(R.drawable.splash);
            }
        }

//        for (int i = 1; i <= 5; i++) {
//            ((Button)mView.findViewById(parentActivity.getResources().getIdentifier("deleteBtn" + i, "id", "com.example.apiexample")))
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int locationId = ((Button)v).getText().charAt(1) - '0';
//                            parentActivity.deleteRequestDeleteLocation(locationId);
//                        }
//                    });
//        }

        container.addView(mView);

        return mView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    private View.OnClickListener todayRainyBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            getRequestGetLocalWeather();

//            if (mGPSInformation == null) {
//                mGPSInformation = new GPSInformation(MainActivity.this);
//            }
//            postCreateRequest(mGPSInformation.getLatitudeHour(), mGPSInformation.getLatitudeMinute(), mGPSInformation.getLongitudeHour(), mGPSInformation.getLongitudeMinute());
        }
    };

    private void getRequestGetLocalWeather() {
        RestAPI restAPI = mRetrofit.create(RestAPI.class);
        String myUserId = PreferenceManager.getString(mContext, Constant.USER_ID);
        System.out.println("My app test : getLocalWeather user Id : " + myUserId);
        restAPI.getLocalWeather(myUserId).enqueue(getLocalWeathersCallback);
    }

    private Callback<List<LinkedHashMap>> getLocalWeathersCallback = new Callback<List<LinkedHashMap>>() {
        @Override
        public void onResponse(Call<List<LinkedHashMap>> call, Response<List<LinkedHashMap>> response) {
            if(response.isSuccessful()) {

                List<LinkedHashMap> responseBody = response.body();
                System.out.println("My app test getlocalweathers: onResponse succeful, size : " + responseBody.size());
                int index = 0;
                String resultText = "";
                for (LinkedHashMap responseElement : responseBody) {
                    int locationId = responseElement.get("locationId").toString().charAt(0) - '0';
                    LocationInfo locationInfo = SQLiteDatabaseManager.getInstance().getMyRegionData(locationId);
                    String currentText = "[" + locationInfo.getLocationId() + "] "
                            + locationInfo.toString() + ", uid : " + responseElement.get("uid")
                            + ", rn1 : " + responseElement.get("precipitation")
                            + ", reh : " + responseElement.get("humidity")
                            + ", pty : " + responseElement.get("precipitationForm")
                            + (responseElement.get("precipitationForm").toString().charAt(0) != '0' ? " 비 온다." : " 비 안온다.");

                    resultText += currentText + "\n";
                    System.out.println(currentText);
                }
                System.out.println("My app test : herehere and this is body : " + responseBody.toString());
//                parentActivity.setMainText(resultText);
            } else {
                System.out.println("My app test : herehere response fail at gggg");
            }
        }

        @Override
        public void onFailure(Call<List<LinkedHashMap>> call, Throwable t) {
            System.out.println("My app test : herehere response fail at gggg");
        }
    };

//    public void setSelectedRegionInformation(String regionOneStep, String regionTwoStep, String regionThreeStep) {
//        mSelectedRegionData = new RegionData(regionOneStep, regionTwoStep, regionThreeStep);
//    }


}
