package com.techpro.chat.ticklechat.listeners;

import android.app.Activity;
import android.view.View;

import com.techpro.chat.ticklechat.utils.AppUtils;


public class TechProEditTextFocusListener implements View.OnFocusChangeListener {

    private static final int KEYBOARD_VISIBILITY_DELAY_TIME = 500;
    Activity mActivity;

    public TechProEditTextFocusListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onFocusChange(final View view, boolean hasFocus) {
        if (hasFocus) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppUtils.showKeyboard(mActivity, view);
                }
            }, KEYBOARD_VISIBILITY_DELAY_TIME);
        }
    }
}
