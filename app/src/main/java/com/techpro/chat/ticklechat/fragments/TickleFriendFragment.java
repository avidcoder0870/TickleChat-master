package com.techpro.chat.ticklechat.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.adapters.TickleFriendAdapter;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.user.User;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TickleFriendFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TickleFriendAdapter mAdapter;
    private List<ContactInvite> myContacts = new ArrayList<>();
    private List<String> mynumbers = new ArrayList<>();
    ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContentResolver cr = getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        ContactInvite user = new ContactInvite();
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        user.setName(name);
                        user.setNumber(phoneNo);
                        mynumbers.add(phoneNo);
                        myContacts.add(user);
//                        Toast.makeText(getContext(), "Name: " + name
//                                + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    }
                    pCur.close();
                }
            }
        }

        dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
        getRegisteredUser(mynumbers);
        View view = inflater.inflate(R.layout.activity_tickle_friend,
                container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mAdapter = new TickleFriendAdapter(DataStorage.chatUserList,getContext(),false,true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private class ContactInvite extends User {
        private String name;
        private String number;
        private boolean isRegisteredUser = false;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public boolean isRegisteredUser() {
            return isRegisteredUser;
        }

        public void setRegisteredUser(boolean registeredUser) {
            isRegisteredUser = registeredUser;
        }
    }

    private void getRegisteredUser(final List<String> members) {
        //Getting webservice instance which we need to call
        String idList = members.toString();
//        final String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        Log.e("===============>", "csv.toString() ==> " + idList);
//        csv = "{\"members\"}:\""+csv+"\"";
        Call<JsonObject> callForUserDetailsFromID = ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId()).
                create(ApiInterface.class).getRegisteredUser(idList);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response != null && response.body() != null) {
                    Log.e("SendMessage", "response.message(: " + response.message());
                    Log.e("SendMessage", "response.code(: " + response.code());
                    Log.e("SendMessage", "response.code(: " + response.body());
//                    grp.setMembers(csv);
//                    DataStorage.mygrouplist.add(grp);
//                    SharedPreferenceUtils.setColleactionObject(getContext(), grp.getId(), new ArrayList<AllMessages.MessageList.ChatMessagesList>());
//                    SharedPreferenceUtils.setColleactionObject(getContext(), SharedPreferenceUtils.mygrouplist,
//                            DataStorage.mygrouplist);
//                    getActivity().getSupportFragmentManager().popBackStack();
//                    Toast.makeText(NewGroupFragment.this.getContext(), "Group created Succesfully.", Toast.LENGTH_LONG).show();
//                    if (response.body() != null && response.body().getStatus().equals("success")) {
//                        grp.setMembers(members.toString().replace("[","").replace("]",""));
//                        DataStorage.mygrouplist.add(grp);
//                        SharedPreferenceUtils.setColleactionObject(mActivity.getApplicationContext(),
//                                SharedPreferenceUtils.mygrouplist,DataStorage.mygrouplist);
//                    }
                } else {
//                    Toast.makeText(NewGroupFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                    Log.e("SendMessage", "Success callMessage_ALL_Service but null response");
                }
//                NewGroupFragment.this.dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
//                Toast.makeText(NewGroupFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e("SendMessage", t.toString());
//                NewGroupFragment.this.dialog.dismiss();
            }
        });

    }
}
