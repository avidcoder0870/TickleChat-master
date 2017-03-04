package com.techpro.chat.ticklechat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.activity.registration.Login;
import com.techpro.chat.ticklechat.activity.registration.RegistrationActivity;
import com.techpro.chat.ticklechat.activity.registration.SignInActivity;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private TextView mTvDeleteAccount, tv_logout_account;
    private LinearLayout mLlEnableNotifications;
    private Snackbar mSnackbar;
    private View mViewBlackOverlay;
    private RelativeLayout mRlMainContainer;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_setting, container, false);
        }
        initUi();
        return mView;
    }

    private void initUi() {
        mLlEnableNotifications = (LinearLayout) mView.findViewById(R.id.ll_enable_notification);
        mTvDeleteAccount = (TextView) mView.findViewById(R.id.tv_delete_account);
        tv_logout_account = (TextView) mView.findViewById(R.id.tv_logout_account);

        mViewBlackOverlay = mView.findViewById(R.id.blackout_view_for_snackbar);
        mRlMainContainer = (RelativeLayout) mView.findViewById(R.id.rl_main_container);

        mTvDeleteAccount.setOnClickListener(this);
        mTvDeleteAccount.setOnClickListener(this);
        mLlEnableNotifications.setOnClickListener(this);
        mViewBlackOverlay.setOnClickListener(this);
        tv_logout_account.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete_account:
                showSnackBar();
                break;
            case R.id.ll_enable_notification:
                //TODO : add delete account event
                break;
            case R.id.tv_logout_account:
                SharedPreferenceUtils.setValue(getContext(), SharedPreferenceUtils.LoginuserDetailsPreference, "");
                SharedPreferenceUtils.setColleactionObject(this.getContext(), SharedPreferenceUtils.myuserlist, null);
                SharedPreferenceUtils.setColleactionObject(this.getContext(), SharedPreferenceUtils.mygrouplist, null);
                SharedPreferenceUtils.setColleactionObject(this.getContext(), SharedPreferenceUtils.chatUserID, null);
                this.getActivity().startActivity(new Intent(this.getActivity(), SignInActivity.class));
                this.getActivity().finish();
                break;
            case R.id.blackout_view_for_snackbar:
                if (mSnackbar != null && mSnackbar.isShown()) {
                    mSnackbar.dismiss();
                }
                mViewBlackOverlay.setVisibility(View.GONE);
                break;

        }
    }

    private void showSnackBar() {

        mSnackbar = Snackbar.make(mViewBlackOverlay, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) mSnackbar.getView();
        layout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View snackView = mInflater.inflate(R.layout.layout_snackbar_delete_account, null);
        snackView.setBackgroundColor(getResources().getColor(R.color.white));

        final TextView mTvBtnDelete = (TextView) snackView.findViewById(R.id.tv_btn_delete);
//        final TextView mTvDeleteAccountInfo = (TextView) snackView.findViewById(R.id.tv_delete_account_info);
//        mTvDeleteAccountInfo.setMovementMethod(new ScrollingMovementMethod());

        View.OnClickListener snackbarViewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSnackbar != null && mSnackbar.isShown()) {
                    if (!AppUtils.isNetworkConnectionAvailable(getContext())) {
                        Toast.makeText(getContext(),
                                getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dialog = ProgressDialog.show(SettingsFragment.this.getActivity(), "Loading", "Please wait...", true);
                    deletUserService(Integer.parseInt(DataStorage.UserDetails.getId()));
                    mSnackbar.dismiss();
                }
                if (mViewBlackOverlay != null) {
                    mViewBlackOverlay.setVisibility(View.GONE);
                }
                switch (v.getId()) {
                    case R.id.tv_delete_account:
                        // TODO: 29/10/16 add action

                        if (mTvBtnDelete.isSelected()) {
                            mTvBtnDelete.setSelected(false);
                        } else {
                            mTvBtnDelete.setSelected(true);
                        }
                        break;
                }
            }
        };

        mTvBtnDelete.setOnClickListener(snackbarViewClickListener);
        layout.addView(snackView, 0);

        mSnackbar.show();
        mViewBlackOverlay.setVisibility(View.VISIBLE);

    }


    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private synchronized void deletUserService(int userid) {
        //Getting webservice instance which we need to call
        Call<JSONObject> callForUserDetailsFromID = (ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId())
                .create(ApiInterface.class)).deleteUser(userid);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response != null && response.body() != null && response.message().equals("OK")) {
                    SharedPreferenceUtils.setValue(getContext(), SharedPreferenceUtils.LoginuserDetailsPreference, "");
                    Toast.makeText(getContext(), "User Deleted.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                    Log.e("profile", "Success callTickles_Service but null response");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                // Log error here since request failed
                Log.e("profile", t.toString());
                dialog.dismiss();
            }
        });

    }

}
