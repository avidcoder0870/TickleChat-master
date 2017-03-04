package com.techpro.chat.ticklechat.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.techpro.chat.ticklechat.views.TechProLoadingDialog;

/**
 * Created by vishalrandive on 20/04/16.
 */
public class BaseFragment extends Fragment {

    protected Context mContext;
    protected AppCompatActivity mBaseActivity;
    private TechProLoadingDialog mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof Activity)
            mBaseActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public TechProLoadingDialog showLoadingDialog(Context context) {
        if (mLoadingDialog == null)
            mLoadingDialog = new TechProLoadingDialog(context);
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    public void showProgressLoadingView(View view) {
        view.setVisibility(View.VISIBLE);

    }

    public void hideProgressLoadingView(View view) {
        view.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mContext = null;
    }
}
