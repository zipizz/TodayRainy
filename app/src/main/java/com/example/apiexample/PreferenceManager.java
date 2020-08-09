package com.example.apiexample;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    public static final String SHARED_PREFERENCES_NAME = Constant.SHARED_PREFERENCES_NAME;
    public static final String SHARED_PREFERENCES_STRING_DEFAULT_VALUE = "NOT DEFINED";
    public static final boolean SHARED_PREFERENCES_BOOLEAN_DEFAULT_VALUE = false;
    public static final int SHARED_PREFERENCES_INT_DEFAULT_VALUE = 0;
    public static final long SHARED_PREFERENCES_LONG_DEFAULT_VALUE = -1L;
    public static final float SHARED_PREFERENCES_FLOAT_DEFAULT_VALUE = -1f;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void setFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        return getPreferences(context).getString(key, SHARED_PREFERENCES_STRING_DEFAULT_VALUE);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPreferences(context).getString(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return getPreferences(context).getBoolean(key, SHARED_PREFERENCES_BOOLEAN_DEFAULT_VALUE);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPreferences(context).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key) {
        return getPreferences(context).getInt(key, SHARED_PREFERENCES_INT_DEFAULT_VALUE);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPreferences(context).getInt(key, defaultValue);
    }

    public static long getLong(Context context, String key) {
        return getPreferences(context).getLong(key, SHARED_PREFERENCES_LONG_DEFAULT_VALUE);
    }

    public static float getFloat(Context context, String key) {
        return getPreferences(context).getFloat(key, SHARED_PREFERENCES_FLOAT_DEFAULT_VALUE);
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.remove(key);
        editor.commit();
    }

    public static void incrementMyRegionCount(Context context) {
        setInt(context, Constant.MY_REGION_COUNT, getInt(context, Constant.MY_REGION_COUNT) + 1);
    }

    public static void decrementMyRegionCount(Context context) {
        setInt(context, Constant.MY_REGION_COUNT, getInt(context, Constant.MY_REGION_COUNT) - 1);
    }
}
