package com.techpro.chat.ticklechat.retrofit;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.ResponseBody;
import com.techpro.chat.ticklechat.AppConfigs;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.listeners.FragmentChangeCallback;
import com.techpro.chat.ticklechat.models.ErrorBean;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

public class RetrofitResponseErrorHandler {

    Activity mActivity;

    public RetrofitResponseErrorHandler(Activity activity) {
        mActivity = activity;
    }

    public void handleError(Retrofit retrofit, Response<?> response) {
        Converter<ResponseBody, ErrorBean> errorConverter =
                retrofit.responseConverter(ErrorBean.class, new Annotation[0]);
        try {
            ErrorBean error = errorConverter.convert(response.errorBody());
//            if (Integer.parseInt(error.getErrors().get(0).getHttp_code()) == 403
//                    && error.getErrors().get(0).getCode().equalsIgnoreCase("GEN-FCKOFF")) {
//                ((FragmentChangeCallback) mActivity).onReplaceFragment(new NotVerifiedFragment());
//                AppConfigs.setVerified(false);
//
//                return;
//            }
            Snackbar.make(mActivity.findViewById(android.R.id.content),
                    error.getErrors().get(0).getMessage(),
                    Snackbar.LENGTH_LONG).show();
        } catch (IOException ioException) {
            Snackbar.make(mActivity.findViewById(android.R.id.content),
                    mActivity.getString(R.string.default_exeption_text),
                    Snackbar.LENGTH_LONG).show();
        } catch (JsonSyntaxException e) {
            Snackbar.make(mActivity.findViewById(android.R.id.content),
                    mActivity.getString(R.string.default_exeption_text),
                    Snackbar.LENGTH_LONG).show();
        } catch (NullPointerException e){
            Snackbar.make(mActivity.findViewById(android.R.id.content),
                    mActivity.getString(R.string.default_exeption_text),
                    Snackbar.LENGTH_LONG).show();
        }
    }

}
