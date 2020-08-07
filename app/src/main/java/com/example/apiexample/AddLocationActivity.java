package com.example.apiexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private ArrayList<ForecastInformation> mCustomArrayList = new ArrayList<ForecastInformation>();
    private Retrofit mRetrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        mCustomArrayList.add(new ForecastInformation("1.0", "99.0", "9.5", "1596799679948", 1));
        mListView = (ListView) findViewById(R.id.listofmyregion);
        mAddLocationListViewAdapter = new AddLocationListViewAdapter(this, mCustomArrayList);
        mListView.setAdapter(mAddLocationListViewAdapter);
        getRequestGetLocalWeather();

        ((Button)findViewById(R.id.add_location_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLocationActivity.this, SelectLocationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getRequestGetLocalWeather() {
        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        String myUserId = PreferenceManager.getString(this, Constant.USER_ID);
        System.out.println("My app test : getLocalWeather user Id : " + myUserId);
        restAPI.getLocalWeather(myUserId).enqueue(getLocalWeathersCallback);
    }

    private Retrofit getRetrofit() {
        if (mRetrofit != null) {
            return mRetrofit;
        }
        mRetrofit = new Retrofit.Builder().baseUrl(Constant.SERVER_BASE_URL).client(
                new OkHttpClient.Builder().sslSocketFactory(SSLSocket.getPinnedCertSslSocketFactory(this))
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
                String resultText = "";
                ArrayList<ForecastInformation> getArrayList = new ArrayList<ForecastInformation>();

                for (LinkedHashMap responseElement : responseBody) {
                    int locationId = responseElement.get("locationId").toString().charAt(0) - '0';
                    LocationInfo locationInfo = SQLiteDatabaseManager.getInstance().getMyRegionData(locationId);
                    String currentText = "[" + locationInfo.getLocationId() + "] "
                            + locationInfo.toString() + ", uid : " + responseElement.get("uid")
                            + ", rn1 : " + responseElement.get("precipitation")
                            + ", reh : " + responseElement.get("humidity")
                            + ", pty : " + responseElement.get("precipitationForm")
                            + (responseElement.get("precipitationForm").toString().charAt(0) != '0' ? " 비 온다." : " 비 안온다.");

                    String precipitationForm = responseElement.get("precipitationForm").toString();
                    String humidity = responseElement.get("humidity").toString();
                    String precipitation = responseElement.get("precipitation").toString();
                    String uid = responseElement.get("uid").toString();

                    resultText += currentText + "\n";
                    System.out.println(currentText);
                    getArrayList.add(new ForecastInformation(precipitationForm, humidity, precipitation, uid, locationId));
                }
                System.out.println("My app test : herehere and this is body : " + responseBody.toString());
                mCustomArrayList = getArrayList;

                System.out.println("mCustom size : " + mCustomArrayList.size());
                mAddLocationListViewAdapter.clear();
                mAddLocationListViewAdapter.addAll(mCustomArrayList);
                mAddLocationListViewAdapter.notifyDataSetChanged();
            } else {
                System.out.println("My app test : herehere response fail at gggg");
            }
        }

        @Override
        public void onFailure(Call<List<LinkedHashMap>> call, Throwable t) {
            System.out.println("My app test : herehere response fail at gggg");
        }
    };
}