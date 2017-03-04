package com.techpro.chat.ticklechat.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class FragmentUtils {

    public static void addFragment(AppCompatActivity activity, Fragment fragment, int containerId) {
        String tag = fragment.getClass().getName();
        activity.getSupportFragmentManager().beginTransaction().add(containerId, fragment, tag).addToBackStack(tag)
                .commit();
    }

    public static void replaceFragment(AppCompatActivity activity, Fragment fragment, int containerId) {
        replaceFragment(activity, fragment, containerId, true);
    }

    public static void replaceFragment(AppCompatActivity activity, Fragment fragment, int containerId, boolean addToBackStack) {
        String tag = fragment.getClass().getName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, tag);
        if(addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public static Fragment getCurrentVisibleFragment(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        String fragmentTag =
                fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = activity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    public static void goToHome(AppCompatActivity activity){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        String fragmentTag =
                fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
    }
}
