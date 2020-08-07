package com.example.apiexample;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPIInstance  {

    private static Context mContext = null;
    private static Retrofit mRetrofit = null;

    public RestAPIInstance(Context context) {
        this.mContext = context;
    }

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder().baseUrl(Constant.SERVER_BASE_URL).client(
                    new OkHttpClient.Builder().sslSocketFactory(SSLSocket.getPinnedCertSslSocketFactory(mContext))
                            .hostnameVerifier(((hostname, session) -> true))
                            .build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
