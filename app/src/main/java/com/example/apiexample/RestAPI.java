package com.example.apiexample;

import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestAPI {

//    @GET("SpringRestExample/rest/local-weather/{id}")
//    Call<ForecastInformation> ForecastInformation(@Path("id") String id);

    @GET("SpringRestExample" + URLConstant.GET_LOCAL_WEATHER)
    Call<List<LinkedHashMap>> getLocalWeather(@Path("uid") String userIdentifier);


//    @GET("SpringRestExample/rest/local-weathers")
//    Call<List<LinkedHashMap>> getLocalWeather(@Path("id") int id);

    @POST("SpringRestExample" + URLConstant.CREATE_USER)
    Call<Integer> CreateUser(@Path("uid") String userIdentifier);

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
//    @POST("SpringRestExample/rest/local-weather/create")
//    Call<Integer> CreateWeatherID(@Body WeatherLocation createBody);

    @POST("SpringRestExample" + URLConstant.ADD_LOCATION)
    Call<Integer> AddLocation(@Path("uid") String userIdentifier, @Path("locationid") int locationId, @Body LocationInfoForServer locationInfo);

    @GET("SpringRestExample" + URLConstant.GET_LOCATION)
    Call<LinkedHashMap> getLocation(@Path("uid") String userIdentifier, @Path("locationid") int locationId);

    @DELETE("SpringRestExample" + URLConstant.DELETE_LOCATION)
    Call<Integer> deleteLocation(@Path("uid") String userIdentifier, @Path("locationid") int locationId);

    @DELETE("SpringRestExample" + URLConstant.DELETE_USER)
    Call<Integer> deleteUser(@Path("uid") String userIdentifier);

//    @GET("SpringRestExample/rest/local-weather/delete/{id}")
//    Call<List<LinkedHashMap>> deleteLocation
}