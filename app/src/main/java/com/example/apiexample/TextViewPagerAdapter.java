package com.example.apiexample;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

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

    public TextViewPagerAdapter () {

    }

    public TextViewPagerAdapter(Context context, MainActivity parentActivity) {
        this.mContext = context;
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return 1;
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
                    parentActivity.startActivity(intent);
//                    AddLocationFragment addLocationFragment = new AddLocationFragment(mContext);
//                    FragmentTransaction fragmentTransaction = parentActivity.getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.add(R.id.favorite_frame, addLocationFragment).commit();
//                    fragmentTransaction.replace(R.id.hello_fragment_frame, addLocationFragment).commit();
                }
            });

//            mView.findViewById(R.id.request_button).setOnClickListener(todayRainyBtnClickListener);
//            mView.findViewById(R.id.request_button).setTag("request_button");
//            FragmentList fragmentRegionStepOne = new FragmentList(1, parentActivity, this);
//            FragmentTransaction fragmentTransaction = parentActivity.getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.add(R.id.main_frame, fragmentRegionStepOne).commit();


//            ((Button)mView.findViewById(R.id.btn1)). setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    parentActivity.deleteUser();
//                }
//            });

//            ((Button)mView.findViewById(R.id.btn2)). setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String allInformation = "";
//                    allInformation += "userid : " + PreferenceManager.getString(mContext, Constant.USER_ID)
//                            + ", my region count : " + PreferenceManager.getInt(mContext, Constant.MY_REGION_COUNT)
//                            + ", finished ? " + PreferenceManager.getBoolean(mContext, Constant.IS_INITIATION_FINISHED)
//                            + "\n"
//                            + "current location info \n";
//
//                    class MyAsyncTask extends AsyncTask<Void, Void, String> {
//
//                        private String allInformation = "";
//
//                        public MyAsyncTask(String allInformation) {
//                            this.allInformation = allInformation;
//                        }
//
//                        @Override
//                        protected String doInBackground(Void... voids) {
//                            for (LocationInfo locationInfo : SQLiteDatabaseManager.getInstance().getMyAddedRegionDataList()) {
//                                allInformation += locationInfo.getLocationId() + " : " + locationInfo.toString() + "\n";
//                            }
//
//                            allInformation += "remain location id : ";
//
//                            for (Integer locationId : SQLiteDatabaseManager.getInstance().getLocationIdList()) {
//                                allInformation += locationId + " ";
//                            }
//
//                            allInformation += "\n";
//                            return allInformation;
//                        }
//
//                        @Override
//                        protected void onPostExecute(String result) {
//                            System.out.println("My app test allinfo : " + result);
//                            parentActivity.setMainText(result);
////                            ((Button)mView.findViewById(R.id.request_button)).setText(result);
////                            ((Button)mView.findViewWithTag("request_button")).setText(result);
//
//                        }
//                    }
//
//                    new MyAsyncTask(allInformation).execute();
//                }
//            });
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
        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        String myUserId = PreferenceManager.getString(mContext, Constant.USER_ID);
        System.out.println("My app test : getLocalWeather user Id : " + myUserId);
        restAPI.getLocalWeather(myUserId).enqueue(getLocalWeathersCallback);
    }

    private Retrofit getRetrofit() {
        if (mRetrofit != null) {
            return mRetrofit;
        }
        mRetrofit = new Retrofit.Builder().baseUrl(Constant.SERVER_BASE_URL).client(
                new OkHttpClient.Builder().sslSocketFactory(SSLSocket.getPinnedCertSslSocketFactory(mContext))
                        .hostnameVerifier(((hostname, session) -> true))
                        .build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return mRetrofit;
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
