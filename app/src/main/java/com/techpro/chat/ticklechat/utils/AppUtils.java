package com.techpro.chat.ticklechat.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.techpro.chat.ticklechat.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppUtils {

    public static final String BABYCHAKRA_CONTACT = "+0000000000";
    public static final String BABYCHAKRA_EMAIL = "a@a.com";
    public static int NOTIFICATION_OPPONENT;

    public static final String FONT_ICON = "fonticon";
    public static final String FONT_OPENSANS_LITE = "opensanslite";
    public static final String FONT_OPENSANS_BOLD = "opensansbold";
    public static final String FONT_OPENSANS_SEMIBOLD = "semibold";

    public static final String FONT_OPENSANS_REGULAR = "opensansregular";

    public static void showKeyboard(final Activity context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String convertViews(long views) {
        String convertedViews = "" + views;
        String formatString = "%.2f";

        if (views <= 99) {
            convertedViews = "" + views;
        } else if (views >= 999) {
            if (views % 1000 == 0)
                formatString = "%.0f";

            float pr = (float) views / 1000.0f;
            convertedViews = String.format(formatString, pr) + " k";
        } else if (views >= 9999999) {
            if (views % 100000 == 0)
                formatString = "%.0f";

            float pr = (float) views / 100000.0f;
            convertedViews = String.format(formatString, pr) + " lac";
        } else if (views >= 4999999999L) {
            if (views % 10000000 == 0)
                formatString = "%.0f";

            float pr = (float) views / 10000000.0f;
            convertedViews = String.format(formatString, pr) + " cr";
        }

        return convertedViews;
    }



    public static String convertDateTime(String datetime) {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        SimpleDateFormat fullDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date today = null;
        try {
            today = fullDate.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!fDate.equals(today)) {
            SimpleDateFormat toFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date dateNew = null;
            try {
                dateNew = toFullDate.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.US);

            return sdf.format(dateNew);
        } else {
            SimpleDateFormat toFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date dateold = null;
            try {
                dateold = toFullDate.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);

            return sdf.format(dateold);
        }
    }

    public static String convertReviewsDate(String date) {
        try {
            Date parseDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate);
            String suffix = getDateSuffix(calendar.get(Calendar.DATE));
            String day = new SimpleDateFormat("d", Locale.US).format(parseDate);
            String month = new SimpleDateFormat("MMM", Locale.US).format(parseDate);
            String year = new SimpleDateFormat("yyyy", Locale.US).format(parseDate);
            date = String.format("%s%s %s %s", day, suffix, month, year);

        } catch (ParseException ignored) {
            Log.e("ERROR", ignored.getMessage());
        }
        return date;
    }

    public static String getDateSuffix(int day) {
        switch (day) {
            case 1:
            case 21:
            case 31:
                return ("st");

            case 2:
            case 22:
                return ("nd");

            case 3:
            case 23:
                return ("rd");

            default:
                return ("th");
        }
    }

    public static void call(String number, final Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static void email(String mail, final Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        intent.putExtra(Intent.EXTRA_TEXT, "body of email");
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            UserInfoProvider.showSnackBar(context,context.getString(R.string.no_mail_client));
        }
    }


    public static void addToContact(String number, final Context context) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        context.startActivity(intent);
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {

                return true;
            }
        }
        return false;
    }

    public static void shareLink(String application, final Context context, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.review_me_on_ticklechat) + " " + url);
        sendIntent.setType("text/plain");
        sendIntent.setPackage(application);
        PackageManager pm = context.getPackageManager();
        if (pm.getLaunchIntentForPackage(application) != null) {
            ActivityInfo activityInfo = sendIntent.resolveActivityInfo(pm, sendIntent.getFlags());
            if (activityInfo.exported) {
                context.startActivity(sendIntent);
            }
        } else {
            Intent intent = new Intent();
            intent.setData(Uri.parse("market://details?chatUserList=" + application));
            context.startActivity(intent);
        }
    }


    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        String contactName = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {

            return null;
        }
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    public static String getDateCurrentTimeZone(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(timestamp * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date currenTimeZone = calendar.getTime();
        return sdf.format(currenTimeZone);
    }

    public static String getTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(timestamp * 1000);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currenTimeZone = calendar.getTime();
        return sdf.format(currenTimeZone);
    }

    /** developer : vishal randive **/
    private static final String DATE_FORMAT_FOR_CHAT = "yyyyMMddhhmmss";
    public static String getCurrentDate(){

        Calendar calendar_2 = Calendar.getInstance();
        long millis = calendar_2.getTimeInMillis();//System.currentTimeMillis() * 1000;
        calendar_2.setTimeInMillis(millis);
        return android.text.format.DateFormat.format(DATE_FORMAT_FOR_CHAT, millis).toString();

//        return mYear+""+mMonth+""+mDay+"_"+mHour+""+mMinute+""+mSecond;
    }
    public static void showLog(String msg) {
        Log.e("babychakra logs : ", msg);
    }

    public static void showToast(final String message, final Context context) {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public static final String DATE_FORMAT_2 = "d MMM yy, h:mm a"; //"EEE, d MMM yyyy HH:mm:ss Z"	Wed, 4 Jul 2001 12:08:56 -0700
    private static final String DATE_FORMAT = "h:mm a";
    public static String getTimeText(long duration) throws Exception
    {
        Date date =new Date(duration * 1000);
//        Date date2 = Calendar.getInstance().getTime();

        Calendar calendar_1 = Calendar.getInstance();
        calendar_1.setTimeInMillis(duration * 1000);

        int mYear = calendar_1.get(Calendar.YEAR);
        int mMonth = calendar_1.get(Calendar.MONTH);
        int mDay = calendar_1.get(Calendar.DAY_OF_MONTH);


        Calendar calendar_2 = Calendar.getInstance();
        long millis = calendar_2.getTimeInMillis();//System.currentTimeMillis() * 1000;
        calendar_2.setTimeInMillis(millis);

        int mYear2 = calendar_2.get(Calendar.YEAR);
        int mMonth2 = calendar_2.get(Calendar.MONTH);
        int mDay2 = calendar_2.get(Calendar.DAY_OF_MONTH);

        if((mYear+mMonth+mDay) == (mYear2+mMonth2+mDay2))
        {
            return android.text.format.DateFormat.format(DATE_FORMAT, date.getTime()).toString();

        }else{
            return android.text.format.DateFormat.format(DATE_FORMAT_2, date.getTime()).toString();
        }

    }


    private void shareAppLinkViaFacebook(Context context) {
        String urlToShare = "YOUR_URL";

        try {
            Intent intent1 = new Intent();
            intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT", urlToShare);
            context.startActivity(intent1);
        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            Intent intent = new Intent(Intent.ACTION_SEND);
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            context.startActivity(intent);
        }
    }

    public static String getDeviceID(Context context) {

        String device_id = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        if (device_id == null) {
            device_id = "";
        }
        return device_id;
    }

    public static Typeface getFontType(Context context, String font) {
        Typeface tf;
        switch (font) {
            case FONT_OPENSANS_LITE:
                tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
                break;

            case FONT_OPENSANS_BOLD:
                tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Bold.ttf");
                break;

            case FONT_OPENSANS_SEMIBOLD:
                tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Semibold.ttf");
                break;
            default:
                tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
                break;

        }
        return tf;
    }

}

