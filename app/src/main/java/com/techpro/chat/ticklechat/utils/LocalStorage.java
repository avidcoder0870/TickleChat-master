package com.techpro.chat.ticklechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class LocalStorage {

    private static final String PREF_NAME = "com.babychakra.android";

    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_GCM_TOKEN = "key_gcm_token";
    private static final String KEY_LOGIN_DATA = "key_login_data";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_META = "key_meta";
    private static final String KEY_ADDRESS = "key_address";
    private static final String KEY_IS_VERIFIED = "key_is_verified";
    private static final String KEY_PERCENT = "key_percent";

    private static LocalStorage sInstance;

    private SharedPreferences mPref;

    private static Context mContext;

    public static LocalStorage getInstance(Context context) {
        mContext = context;
        if (sInstance == null) {

            return new LocalStorage(context);
        }

        return sInstance;
    }

    private LocalStorage(Context context) {
        sInstance = this;
        mPref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
    }

    public void setLoggedInData(Object loginBean) {
        clearData();
//        storeToken(loginBean.getMeta().getToken());
//        String address = "";
//        if (ValidationUtils.validateObject(loginBean.getData().getLocation())) {
//            address = loginBean.getData().getLocation();
//        }
//        if (ValidationUtils.validateObject(loginBean.getData().getCity())) {
//            if (address.length() > 0) {
//                address += ", " + loginBean.getData().getCity().getCity_name();
//            }
//        }
//        storeAddress(address);
//        storeServiceName(loginBean.getData().getName());
//        AppConfigs.setVerified(loginBean.getMeta().isVerified());
//        storeMeta(loginBean.getMeta());
//        mPref.edit().putString(KEY_LOGIN_DATA, new Gson().toJson(loginBean)).commit();
    }

    public void clearData() {
        mPref.edit().clear().commit();
    }


    public float getCompleteProfilePercent() {
        return mPref.getFloat(KEY_PERCENT, 0);
    }

    public String getToken() {
        return mPref.getString(KEY_TOKEN, null);
    }

    public String getGcmToken() {
        return mPref.getString(KEY_GCM_TOKEN, null);
    }

    public String getServiceName() {
        return mPref.getString(KEY_NAME, null);
    }

    public String getAddress() {
        return mPref.getString(KEY_ADDRESS, null);
    }

    public boolean logout() {
        return mPref.edit().clear().commit();
    }
}
