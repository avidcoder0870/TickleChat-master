package com.techpro.chat.ticklechat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionUtils {
    //Define the request code (in lower 8 bit range: 0 to 255)
    public static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;

    static String TAG = "PERMISSION";

    public static void checkPermission(Activity activity, String permission) {

        //Call this snippet when ever you're trying to get anything from the Telephony manager like getDeviceId() or getSimSerialNumber();
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            //Proceed with whatever you're doing
        } else {
            Log.d(TAG, "Current app does not have READ_PHONE_STATE permission");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_PHONE_STATE_PERMISSION);
        }
    }
}
