package com.techpro.chat.ticklechat.pushnotifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.LocalStorage;


public class GCMIntentService extends IntentService {

    private static final String TAG = GCMIntentService.class.getSimpleName();

    private NotificationManager notificationManager;


    public static final int NOTIFICATION_ID = 1;

//    private static final int SMALL_ICON = R.mipmap.ic_launcher;
    private static final int LARGE_ICON = R.mipmap.ic_launcher;

    private static final int SMALL_ICON = R.mipmap.ic_launcher;



    private NotificationManager mNotificationManager;

    NotificationCompat.Builder builder;

    String mycallbackclass = "";

    public GCMIntentService() {
        super("GcmIntentService");
    }

    public static final String LOG_TAG = "GcmIntentService";

    @Override
    public void onCreate() {

        Log.v("GCMIntentService", "in OnCreate()");
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.

        Log.v("GCMIntentService", "GCMIntentService: onHandleIntent");


        String messageType = gcm.getMessageType(intent);
        if (extras != null) {
            if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
			 * extended in the future with new message types, just ignore any message types you're
			 * not interested in, or that you don't recognize.
			 */

                if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                    Log.v("GCMIntentService", "messageType: GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE");

                    // Post notification of received message
                    if (extras.containsKey("message")) {

                        final String messageValue = extras.getString("message");
                        if (messageValue.contains("babychakrachat")) {
                            processNotification(extras);
                            return;
                        }
                    }

                }
            } else {
                Log.v("GCMIntentService", "extras are empty");
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void processNotification(Bundle extras) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final String messageValue = extras.getString("message");

        if (TextUtils.isEmpty(messageValue))
            return;

        try {
            JsonObject object = new JsonParser().parse(messageValue).getAsJsonObject();
            String title = object.get("notificationContentTitle").getAsString();
            String subTitle = object.get("notificationContentText").getAsString();
//
//            Intent intent;
//
//            if (LocalStorage.getInstance(getApplicationContext()).getLoggedInData()!=null) {
//                intent = new Intent(this, ChatNewActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("callFromOtherActivity", true);
//                AppConfigs.QB_NOTIFICATION_OPPONENT = object.get("opponentid").getAsInt();
//                if (object.has("opponentid")) {
//
//                    intent.putExtra("opponentid", object.get("opponentid").getAsInt());
//                    intent.putExtra("opponentid_notification", object.get("opponentid").getAsInt());
//                }
//
//
//            } else {
//                intent = new Intent(this, SplashScreenActivity.class);
//            }
//            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(
//                    SMALL_ICON).setContentTitle(title).setStyle(
//                    new NotificationCompat.BigTextStyle().bigText("Babychakra")).setContentText(subTitle);
//
//            mBuilder.setAutoCancel(true);// default cancellable
//            mBuilder.setContentIntent(contentIntent);
            // Define sound URI
//            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            mBuilder.setSound(soundUri); // notification sound

//            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            // notify about new push
            //
            Intent intentNewPush = new Intent("new push");
            intentNewPush.putExtra("message", messageValue);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentNewPush);

            AppUtils.showLog("Broadcasting event " + messageValue + " with data: " + messageValue);

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
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

        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }

        return true;
    }
}
