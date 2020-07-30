package com.example.apiexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private CSVFileReader mCSVFileReader = null;
    private ArrayList<RegionData> mRegionDataCollections = null;
    private SQLiteDatabaseManager mSQLiteDatabaseManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);









        ((Button)findViewById(R.id.todayRainView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCSVFileReader = new CSVFileReader(getResources());
                mRegionDataCollections = mCSVFileReader.getRegionDataCollectionsFromCSVFile();
                mSQLiteDatabaseManager = new SQLiteDatabaseManager(MainActivity.this);
                mSQLiteDatabaseManager.createRegionDataDB(mRegionDataCollections);




                mSQLiteDatabaseManager.showRegionDataDB();
            }
        });





        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSInformation gpsInformation = new GPSInformation(MainActivity.this);
                System.out.println("lat H : " + gpsInformation.getLatitudeHour() + ", lat M : " + gpsInformation.getLatitudeMinute()
                        + ", long H : " + gpsInformation.getLongitudeHour() + ", long M : " + gpsInformation.getLongitudeMinute());
                System.out.println("주소 : " + gpsInformation.getGPSAddress().toString());
            }
        });

        ((Button)findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.SERVER_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                RestAPI restAPI = retrofit.create(RestAPI.class);
                Call<ForecastInformation> call = restAPI.ForecastInformation("1");

                call.enqueue(new Callback<ForecastInformation>() {
                    @Override
                    public void onResponse(Call<ForecastInformation> call, Response<ForecastInformation> response) {
                        ForecastInformation forecastInformation = response.body();
                        System.out.println(forecastInformation.toString());
                        ((TextView)v).setText(forecastInformation.toString());
                    }

                    @Override
                    public void onFailure(Call<ForecastInformation> call, Throwable t) {
                        System.out.println("error : " + t.getMessage() + ", " + call.toString());
                    }
                });
//                call.enqueue(new Callback<ForecastInformation>() {
//                    @Override
//                    public void onResponse(Call<List<ForecastInformation>> call, Response<List<ForecastInformation>> response) {
//                        List<ForecastInformation> forecastInformations = response.body();
//
//                        for (ForecastInformation forecastInformation : forecastInformations) {
//                            System.out.println(forecastInformation.toString());
//                            ((TextView)v).setText(forecastInformation.toString());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<ForecastInformation>> call, Throwable t) {
//                        System.out.println("error : " + t.getMessage());
//                    }
//                });
            }
        });
        closeAll();
    }

    private void closeAll() {
        if (mCSVFileReader != null) {
            mCSVFileReader.close();
        }

        if (mSQLiteDatabaseManager != null) {
            mSQLiteDatabaseManager.close();
        }
    }
}
