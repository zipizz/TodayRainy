package com.example.apiexample;

public class URLConstant {
    public static final String GET_LOCAL_WEATHER = "/rest/local-weather/{uid}";
    public static final String GET_ALL_LOCAL_WEATHER = "/rest/local-weathers";
    public static final String CREATE_USER = GET_LOCAL_WEATHER;
    public static final String DELETE_USER = GET_LOCAL_WEATHER;
    public static final String GET_LOCATION = GET_LOCAL_WEATHER + "/{locationid}";
    public static final String REGISTER_LOCATION = GET_LOCATION;
    public static final String DELETE_LOCATION = REGISTER_LOCATION;
}
