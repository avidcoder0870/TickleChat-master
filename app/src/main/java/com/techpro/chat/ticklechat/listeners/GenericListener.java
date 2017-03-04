package com.techpro.chat.ticklechat.listeners;


/**
 * Created by Vishal Randive on 2/16/2016.
 */
public class GenericListener<T> implements OnResponseListener<T> {
    public GenericListener() {
    }

    @Override
    public void onResponse(int callerID, T messages) {

    }
}