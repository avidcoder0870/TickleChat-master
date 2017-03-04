package com.techpro.chat.ticklechat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.adapters.UserListAdapter;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.Group;
import com.techpro.chat.ticklechat.models.message.AllMessages;
import com.techpro.chat.ticklechat.models.user.User;
import com.techpro.chat.ticklechat.models.user.UserGroupBotModel;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeScreenFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tickle_friend,
                container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<UserGroupBotModel> readedmsg = new ArrayList<>();
        List<UserGroupBotModel> unreadedmsg = new ArrayList<>();
        List<UserGroupBotModel> list = new ArrayList<>();
        list.addAll(DataStorage.chatUserList);
        list.addAll(DataStorage.mygrouplist);
        Collections.sort(list, new Comparator<UserGroupBotModel>() {
            public int compare(UserGroupBotModel emp1, UserGroupBotModel emp2) {
                // ## Ascending order
                AllMessages.MessageList.ChatMessagesList msg;
                List<AllMessages.MessageList.ChatMessagesList> movieList = new ArrayList<>();
                String sentat1;
                String sentat2;
                if (emp1 instanceof User) {
                    User user = (User) emp1;
                    movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(getContext(), user.getId());
                    msg = movieList.get(movieList.size() - 1);
                    sentat1 = msg.getSentat();
                } else {
                    Group grp = (Group) emp1;
                    movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(getContext(), grp.getId());
                    msg = movieList.get(movieList.size() - 1);
                    sentat1 = msg.getSentat();
                }
                if (emp2 instanceof User) {
                    User user = (User) emp2;
                    movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(getContext(), user.getId());
                    msg = movieList.get(movieList.size() - 1);
                    sentat2 = msg.getSentat();
                } else {
                    Group grp = (Group) emp2;
                    movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(getContext(), grp.getId());
                    msg = movieList.get(movieList.size() - 1);
                    sentat2 = msg.getSentat();
                }
//                return sentat1.compareToIgnoreCase(emp2.getFirstName()); // To compare string values
                return Long.valueOf(sentat1).compareTo(Long.valueOf(sentat2)); // To compare integer values

                // ## Descending order
                // return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
                // return Integer.valueOf(emp2.getId()).compareTo(emp1.getId()); // To compare integer values
            }
        });

        for (UserGroupBotModel model : list) {
            AllMessages.MessageList.ChatMessagesList msg;
            List<AllMessages.MessageList.ChatMessagesList> movieList = new ArrayList<>();
            UserGroupBotModel finalmodel = null;
            if (model instanceof User) {
                User user = (User) model;
                movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(getContext(), user.getId());
                msg = movieList.get(movieList.size() - 1);
                finalmodel = user;
            } else {
                Group grp = (Group) model;
                movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(getContext(), grp.getId());
                msg = movieList.get(movieList.size() - 1);
                finalmodel = grp;
            }
            if (msg.getRead().equals("1")) {
                unreadedmsg.add(finalmodel);
            } else {
                readedmsg.add(finalmodel);
            }
        }
        list.clear();
        list.addAll(unreadedmsg);
        list.addAll(readedmsg);
        mAdapter = new UserListAdapter(list, getContext(), false, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
