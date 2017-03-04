package com.techpro.chat.ticklechat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by sagars on 10/25/16.
 */
public class SharedPreferenceUtils {
    public static String LoginuserDetailsPreference = "LoginuserDetailsPreference";
    public static String chatUserID = "chatUserList";
    public static String mygrouplist = "mygrouplist";
    public static String myuserlist = "myAllUserlist";
    public static void setValue(Context context, String name, String value) {
        SharedPreferences mPrefs = context.getSharedPreferences("tickle_pref", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(name, value);
        prefsEditor.commit();
    }

    public static String getValue(Context context, String name, String defaultValue) {
        SharedPreferences mPrefs = context.getSharedPreferences("tickle_pref", 0);
        Gson gson = new Gson();
        String json = mPrefs.getString(name, defaultValue);
        return json;
    }

    public static Object getColleactionObject(Context context,String name){
        Object obj = null;
        try {
            File file = new File(context.getDir(name, Context.MODE_PRIVATE), name);
            ObjectInputStream outputStream = new ObjectInputStream(new FileInputStream(file));
            obj = outputStream.readObject();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("getColleactionObject=>"+obj,"getColleactionName> "+name);
        return obj;
    }


    public static void setColleactionObject(Context context,String name, Object obj){
        try {
            File file = new File(context.getDir(name, Context.MODE_PRIVATE), name);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(obj);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
