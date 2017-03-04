package com.techpro.chat.ticklechat.pushnotifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.activity.SplashActivity;

import java.net.URL;

public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {

    public static final int NOTIFICATION_ID = 1;

    private static final int SMALL_ICON = R.mipmap.ic_launcher;
    private static final int LARGE_ICON = R.mipmap.ic_launcher;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        //TODO:: Action to perform when message is received that user is verified
        //TODO:: Remove this toast when original message format is received
        sendNotification(data);
    }

    @SuppressLint("NewApi")
    private void sendNotification(Bundle extras) {
        Log.v("Notification", " ipush recieved");
        //TODO:: This code is provided by Mohit. It is untested
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (isPackageExisted(extras.getString("packageName"))) {
            Intent inty = null;
            PendingIntent contentIntent;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification;

            // start with if notification should hav intent for action or not
            if (extras.containsKey("notificationHasIntent")) {
                if (extras.getString("notificationHasIntent").equals("Y")) {
                    // intent only if said by server
                    // get call back class from preferences.
                    try {
                        inty = new Intent(this, SplashActivity.class);
                        inty.replaceExtras(extras);

                        contentIntent = PendingIntent.getActivity(this, 0, inty, PendingIntent.FLAG_CANCEL_CURRENT);
                        mBuilder.setContentIntent(contentIntent);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            // Define sound URI
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(soundUri); // notification sound

            mBuilder.setAutoCancel(true);// default cancellable
            // define UI for notification based on extras

            mBuilder.setSmallIcon(SMALL_ICON);
            if (extras.containsKey("notificationContentTitle")) {
                if (extras.getString("notificationContentTitle") != null) {
                    mBuilder.setContentTitle(extras.getString("notificationContentTitle"));
                }
            }

            if (extras.containsKey("notificationContentText")) {
                //notification second line
                if (extras.getString("notificationContentText") != null) {
                    mBuilder.setContentText(extras.getString("notificationContentText"));
                }
            }

            if (extras.containsKey("hasLargeIcon")) {
                // notification Large icon Top left
                if (extras.getString("hasLargeIcon").equals("Y")) {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), LARGE_ICON);
                    mBuilder.setLargeIcon(bm);
                }
            }

            if (extras.containsKey("notificationSubText")) {
                // notification second line
                if (extras.getString("notificationSubText") != null) {
                    if (isJellyBeanAPI16()) {
                        mBuilder.setSubText(extras.getString("notificationSubText"));
                    }
                }
            }

            if (extras.containsKey("notificationTickerText")) {
                // ticker at 1st time o status
                if (extras.getString("notificationTickerText") != null) {
                    mBuilder.setTicker(extras.getString("notificationTickerText"));
                }
            }

            if (extras.containsKey("notificationTime")) {
                // Long value Notification time / top right
                mBuilder.setWhen(Long.parseLong(extras.getString("notificationTime")));
            }

            if (extras.containsKey("notificationPriority")) {
                // Long value  Notification  time / top right
                if (isJellyBeanAPI16()) {
                    //TODO:: Need to check the priority from the intent then set the priority
                    mBuilder.setPriority(Notification.PRIORITY_HIGH);
                }
            }

            // Vibration
            if (extras.containsKey("hasVibration")) {
                // Long value //
                // Notification time top right
                if (extras.getString("hasVibration").equals("Y")) {
                    mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                }
            }

            // LED
            if (extras.containsKey("hasLightCode")) {
                // Notification time top right

                mBuilder.setLights(0xFFFF0000, 100, 3000);
            }

            // check for GCM notificaiton styles as normal/ bigtext / bigpicture
            if (extras.getString("gcmStyle").equalsIgnoreCase("bigpicture")) {

                // check if JB or APII 15 above ..
                // stream image aply
                // else
                // set default picture
                if (isJellyBean()) {

                    // Now create the Big picture notification.
                    URL url;

                    try {
                        url = new URL(extras.getString("notificationBigImagelink"));
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        notification = new NotificationCompat.BigPictureStyle(mBuilder).bigPicture(image).build();
                    } catch (Exception e) {
                        notification = new NotificationCompat.BigPictureStyle(mBuilder)
                                .bigPicture(BitmapFactory.decodeResource(getResources(), LARGE_ICON))
                                .build();
                    }
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                } else {
                    // if not jb show normal notification
                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                }
            } else if (extras.getString("gcmStyle").equalsIgnoreCase("bigtext")) {
                if (extras.containsKey("notificationText")) {
                    mBuilder.setStyle(new NotificationCompat.BigTextStyle(mBuilder).bigText(extras.getString("notificationText")));
                }
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            } else {
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        } else {
            Log.v("sendNotification", " in sendNotification(): package not exsist");
        }
    }

    public static boolean isJellyBean() {
        boolean isJellyBean = false;
        int sdkVersion = getDeviceSDKVersion();
        if (sdkVersion != 0 && sdkVersion > 15) {
            isJellyBean = true;
        } else {
            String androidVersion = getDeviceAndroidVersion();
            if (androidVersion.compareTo("4.0.4") > 0) {
                isJellyBean = true;
            }
        }

        return isJellyBean;
    }

    public static boolean isJellyBeanAPI16() {
        boolean isJellyBean = false;
        int sdkVersion = getDeviceSDKVersion();
        if (sdkVersion != 0 && sdkVersion > 16) {
            isJellyBean = true;
        } else {
            String androidVersion = getDeviceAndroidVersion();
            if (androidVersion.compareTo("4.1") > 0) {
                isJellyBean = true;
            }
        }

        return isJellyBean;
    }

    public static String getDeviceAndroidVersion() {
        String androidOS = android.os.Build.VERSION.RELEASE;

        return androidOS;
    }

    public static int getDeviceSDKVersion() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;

        return sdkVersion;
    }

    private boolean isPackageExisted(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName + ".debug", PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }

        return true;
    }
}
