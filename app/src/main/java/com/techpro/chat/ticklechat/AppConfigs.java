package com.techpro.chat.ticklechat;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

import lombok.Getter;
import lombok.Setter;
/**
 * Created by vishalrandive on 06/04/16.
 */
public class AppConfigs extends MultiDexApplication {

    public static final int DEFAULT_CALLER_ID = 0;

    public static final int ERROR_CALLER_ID = 0;

    public static String CHAT_IMG_MSG = "~*Image*~";
    public static final int DEFAULT_EVENT_ID = 101;

    public static final int PLACEHOLDER_LARGE = R.mipmap.ic_launcher;
    public static final int PLACEHOLDER_CIRCULAR = R.mipmap.ic_launcher;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    @Getter
    @Setter
    private static boolean isNavigationHeaderUpdated = false;

    @Getter
    @Setter
    private static boolean isVerified;

    private static AppConfigs instance;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (ValidationUtils.validateObject(categoryList))
//            categoryList.clear();
        FirebaseApp.initializeApp(getApplicationContext());
        instance = this;
    }

    public static AppConfigs getInstance() {
        return instance;
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
