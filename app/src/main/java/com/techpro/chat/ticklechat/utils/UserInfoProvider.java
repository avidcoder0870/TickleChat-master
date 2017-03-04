package com.techpro.chat.ticklechat.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

public class UserInfoProvider {

    public static final int FLAG_SNACKBAR = 1;
    public static final int FLAG_TOAST = 2;

    public static void notifyUser(Activity activity, String message, int flag) {
        if (flag == FLAG_SNACKBAR)
            showSnackBar(activity, message);
        else if (flag == FLAG_TOAST)
            showToast(activity, message);
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(Activity activity, String message) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    public static Snackbar showPermanentSnackBar(Activity activity, String message) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        return snackbar;
    }

    public static void showSnackBar(Context context, String message) {
        try {
            Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                    .show();
        } catch (ClassCastException e) {
            showToast(context, message);
        }
    }
}
