package com.techpro.chat.ticklechat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TickleSharedPrefrence {

    private SharedPreferences prefs;
    private String MY_PREFS_NAME = "TickleSharedPrefrence";
    private static TickleSharedPrefrence mTickleSharedPrefrence;

    private SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private TickleSharedPrefrence(Context context){
        prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static TickleSharedPrefrence getInstance(Context context){
        if (mTickleSharedPrefrence == null)
            mTickleSharedPrefrence = new TickleSharedPrefrence(context);

        return mTickleSharedPrefrence;
    }

    public void addToSharedPreference(String Key, String value){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Key, value);
        editor.commit();
    }

    public String getFromSharedPreference(String Key){
        return prefs.getString(Key, "");
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor = prefs.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
