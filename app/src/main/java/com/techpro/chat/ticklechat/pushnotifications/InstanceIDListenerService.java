package com.techpro.chat.ticklechat.pushnotifications;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.techpro.chat.ticklechat.models.GcmBean;
import com.techpro.chat.ticklechat.retrofit.TechProServicesController;
import com.techpro.chat.ticklechat.utils.LocalStorage;
import com.techpro.chat.ticklechat.utils.ValidationUtils;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {

    public static final String GCM_SENDER_ID = "422150295548";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //refreshing the token and saving it in preferences
        processToken(this);
    }

    public static void processToken(Context context) {
        InstanceID instanceID = InstanceID.getInstance(context);
        try {
            String token = instanceID.getToken(GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            LocalStorage.getInstance(context).storeGcmToken(token);

            // API request
            HashMap<String, String> map = new HashMap<>();
            map.put("device_uid", getDeviceID(context));
            map.put("token", token);

//            TechProServicesController.getInstance()
//                    .registerGcm(LocalStorage.getInstance(context).getToken(), map).enqueue(callback);
        } catch (IOException e) {
            // avoiding the process
        }
    }

    private static Callback<GcmBean> callback = new Callback<GcmBean>() {
        @Override
        public void onResponse(Response<GcmBean> response, Retrofit retrofit) {
            if (ValidationUtils.validateRetrofitResponse(response)) {
                Log.e("TOKEN", response.body().getData().getToken());
            }
        }

        @Override
        public void onFailure(Throwable t) {

        }
    };

    private static String getDeviceID(Context context) {
        String device_id;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        if (device_id == null) {
            device_id = "";
        }
        return device_id;
    }
}
