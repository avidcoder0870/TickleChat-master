package com.techpro.chat.ticklechat.retrofit;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class TechProServicesController {

    private static final String BASE_URL = "http://";

    private TechProServices mTechProServices;
    private static TechProServicesController sTechProServicesController = null;

    private TechProServicesController() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
        mTechProServices = retrofit.create(TechProServices.class);
    }

    public static TechProServicesController getInstance() {
        if (sTechProServicesController == null)
            sTechProServicesController = new TechProServicesController();
        return sTechProServicesController;
    }

    public TechProServices getBabychakraServices() {
        return mTechProServices;
    }
}
