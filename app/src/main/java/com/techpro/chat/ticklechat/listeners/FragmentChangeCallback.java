package com.techpro.chat.ticklechat.listeners;

import android.support.v4.app.Fragment;

/**
 * Created by viveksingh on 24/12/15.
 */
public interface FragmentChangeCallback {

    /**
     * callback to notify the activity to replace the current fragment with the provded fragment
     *
     * @param fragment New fragment
     */
    void onReplaceFragment(Fragment fragment);
    void onReplaceFragment(Fragment fragment, boolean addToBackStack);

    /**
     * callback to notify the activity to replace the current fragment with the provded fragment
     *
     * @param fragment New fragment
     */
    void onAddFragment(Fragment fragment);
}
