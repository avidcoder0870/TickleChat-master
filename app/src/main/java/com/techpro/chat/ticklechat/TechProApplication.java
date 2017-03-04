package com.techpro.chat.ticklechat;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;


/**
 * Created by vishalrandive on 20/04/16.
 */
public class TechProApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }
}