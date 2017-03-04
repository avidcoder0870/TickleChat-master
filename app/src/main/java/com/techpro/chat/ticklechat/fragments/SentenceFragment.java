package com.techpro.chat.ticklechat.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.adapters.AddSentenseAdapter;
import com.techpro.chat.ticklechat.adapters.StatusAdapter;
import com.techpro.chat.ticklechat.models.CustomModel;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.TickleFriend;
import com.techpro.chat.ticklechat.models.message.Tickles;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SentenceFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private TextView mTvAddNewTickle;
    private TextView mTvBtnSave;
    private ProgressDialog dialog;
    private RecyclerView mRvChangeStatus;
    private AddSentenseAdapter mStatusAdapter;
    private List<TickleFriend> mTempList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_sentence_fragment, container, false);
        }
        initUi();
        return mView;
    }

    private void initUi() {
        mRvChangeStatus = (RecyclerView) mView.findViewById(R.id.rv_select_sentence);
        mRvChangeStatus.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvChangeStatus.setHasFixedSize(false);
        mRvChangeStatus.setItemAnimator(new DefaultItemAnimator());

        dialog = ProgressDialog.show(SentenceFragment.this.getActivity(), "Loading", "Please wait...", true);
        getTicklesService(Integer.parseInt(DataStorage.UserDetails.getId()));
        mTvAddNewTickle = (TextView) mView.findViewById(R.id.tv_add_new_tickle);
        mTvBtnSave = (TextView) mView.findViewById(R.id.tv_btn_save);
        mTvBtnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_save:
                if (!AppUtils.isNetworkConnectionAvailable(getContext())) {
                    Toast.makeText(getContext(),
                            getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mTvAddNewTickle.getText() != null && mTvAddNewTickle.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please add valid sentence.", Toast.LENGTH_LONG).show();
                } else {
                    dialog = ProgressDialog.show(SentenceFragment.this.getActivity(), "Loading", "Please wait...", true);
                    callAddSentenceService(DataStorage.UserDetails.getId(), mTvAddNewTickle.getText().toString());
                }
                break;
        }
    }


    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private synchronized void callAddSentenceService(String userid, final String status) {
        //Getting webservice instance which we need to call
        Call<JSONObject> callForUserDetailsFromID = (ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId())
                .create(ApiInterface.class)).callAddSentenceService(status, userid);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response != null && response.body() != null && response.message().equals("OK")) {
                    mTvAddNewTickle.setText("");
                    mTempList.add(new TickleFriend("nnn", status, "0", null));
                    mStatusAdapter.notifyDataSetChanged();
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


    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private synchronized void getTicklesService(int userid) {
        if (!AppUtils.isNetworkConnectionAvailable(getContext())) {
//            Toast.makeText(getContext(),
//                    getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
            return;
        }
        //Getting webservice instance which we need to call
        Call<Tickles> callForUserDetailsFromID = (ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId())
                .create(ApiInterface.class)).getTickles(userid);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<Tickles>() {
            @Override
            public void onResponse(Call<Tickles> call, Response<Tickles> response) {
                if (response != null && response.body() != null) {
                    ArrayList<Tickles.MessageList.ChatMessagesTicklesList> tickles = response.body().getBody().getTickles();
                    for (int i = 0; i < tickles.size(); i++) {
                        mTempList.add(new TickleFriend(tickles.get(i).getId(), tickles.get(i).getMessage(), tickles.get(i).getApproved(), null));
                    }
                    mStatusAdapter = new AddSentenseAdapter(mTempList, getContext());
                    mRvChangeStatus.setAdapter(mStatusAdapter);
                    mStatusAdapter.setDataUpdateListener(new AddSentenseAdapter.DataUpdated() {
                        @Override
                        public void dataUpdated(String id) {
//                                dialog = ProgressDialog.show(SentenceFragment.this.getActivity(), "Loading", "Please wait...", true);
//                                callupdateStatusService(Integer.parseInt(DataStorage.UserDetails.getId()), id);
                        }
                    });
                } else {
                    Log.e("profile", "Success callTickles_Service but null response");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Tickles> call, Throwable t) {
                // Log error here since request failed
                Log.e("profile", t.toString());
                dialog.dismiss();
            }
        });

    }
}
