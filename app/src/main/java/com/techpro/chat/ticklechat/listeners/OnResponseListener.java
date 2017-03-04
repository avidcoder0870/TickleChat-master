package com.techpro.chat.ticklechat.listeners;

/**
 * Created by Administrator on 9/23/2015.
 */
public interface OnResponseListener<T> {
    public void onResponse(int callerID, T messages);
}

