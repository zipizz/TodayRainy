package com.example.apiexample;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class GetForecastInformationWorker extends Worker {

    private static int count;

    public GetForecastInformationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("my app test start worker do work start, and this is count at : " + count++);
        return RestAPIFunction.getInstance(getApplicationContext()).getAllLocalWeatherBackground(PreferenceManager.getString(getApplicationContext(), Constant.USER_ID));
    }
}