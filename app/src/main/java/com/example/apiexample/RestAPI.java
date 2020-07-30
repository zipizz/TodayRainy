package com.example.apiexample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPI {

//    @GET("/SpringRestExample/rest/local-weather/{id}")
//    Call<List<ForecastInformation>> ForecastInformations(
//        @Path("id") String id
//    );

    @GET("/SpringRestExample/rest/local-weather/{id}")
    Call<ForecastInformation> ForecastInformation(
            @Path("id") String id
    );
}