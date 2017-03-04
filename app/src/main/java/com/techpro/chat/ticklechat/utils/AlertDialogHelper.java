package com.techpro.chat.ticklechat.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;

public class AlertDialogHelper {

    public static void showAlertDialog(Context context, String title, String message) {
        alertDialog(context, title, message).show();
    }

    public static AlertDialog alertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if (null == title)
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        else
            alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return alertDialog;
    }

    public static void showActionConfirmationAlertDialog(Context context, String title, String message,
                                                         DialogInterface.OnClickListener positiveButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        if (null != title) {
            builder.setTitle(title);
        }

        builder.setPositiveButton("OK", positiveButtonClickListener);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
