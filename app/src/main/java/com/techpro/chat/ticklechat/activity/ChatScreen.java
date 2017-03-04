package com.techpro.chat.ticklechat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.adapters.ChatAdapter;
import com.techpro.chat.ticklechat.adapters.MessageAdapter;
import com.techpro.chat.ticklechat.adapters.TickleFriendAdapter;
import com.techpro.chat.ticklechat.controller.MessageController;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.Messages;
import com.techpro.chat.ticklechat.models.TickleFriend;
import com.techpro.chat.ticklechat.models.message.AllMessages;
import com.techpro.chat.ticklechat.models.message.Tickles;
import com.techpro.chat.ticklechat.models.user.User;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends Activity {
    protected Toolbar mToolbar;
    protected RecyclerView message_list, text_list;
    private ChatAdapter mAdapter;
    private MessageAdapter mAdapter1;
    private List<AllMessages.MessageList.ChatMessagesList> movieList = new ArrayList<>();
    private ArrayList<Messages> ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        message_list = (RecyclerView) findViewById(R.id.message_list);
        text_list = (RecyclerView) findViewById(R.id.text_list);
        int isgroup = 0;
        String sentID = "";
        String userid = getIntent().getStringExtra("userid");
        String groupid = getIntent().getStringExtra("groupid");
        String username = getIntent().getStringExtra("username");
        mToolbar.setTitle(username);
//        boolean israndom = getIntent().getBooleanExtra("israndom",false);
        Log.e("RecyclerView", "userid：" + userid);
        Log.e("RecyclerView", "groupid：" + groupid);
        if (groupid == null ) {
            sentID = userid;
            movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.
                    getColleactionObject(getApplicationContext(), userid);
            if (movieList!= null && movieList.size()!= 0) {
                movieList.get(movieList.size() - 1).setRead("0");
                SharedPreferenceUtils.setColleactionObject(getApplicationContext(), userid, movieList);
            }
        } else {
            isgroup = 1;
            sentID = groupid;
            movieList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.
                    getColleactionObject(getApplicationContext(), groupid);
            if (movieList!= null &&  movieList.size()!= 0) {
                movieList.get(movieList.size() - 1).setRead("0");
                SharedPreferenceUtils.setColleactionObject(getApplicationContext(), groupid, movieList);
            }
        }
        if (movieList == null)
            movieList = new ArrayList<>();


        ctrl = new MessageController(getApplicationContext()).getMessages();
        mAdapter1 = new MessageAdapter(ctrl, getApplicationContext(), false, true, sentID, isgroup);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        text_list.setLayoutManager(mLayoutManager1);
        text_list.setItemAnimator(new DefaultItemAnimator());
        text_list.setAdapter(mAdapter1);
        message_list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    int heightDiff = message_list.getRootView().getHeight() - message_list.getHeight();
                    if (heightDiff > 100) {
                        message_list.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    }
                } catch (Exception e) {

                }
            }
        });

        mAdapter = new ChatAdapter(mAdapter1,movieList, getApplicationContext(), false, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        message_list.setLayoutManager(mLayoutManager);
        message_list.setItemAnimator(new DefaultItemAnimator());
        message_list.setAdapter(mAdapter);

        final ImageView ivSlideUpDown = (ImageView) findViewById(R.id.iv_slide_up_down);
        ivSlideUpDown.setColorFilter(getResources().getColor(R.color.white));
        ivSlideUpDown.setImageResource(R.drawable.down_white_arrow_icon);
        ivSlideUpDown.setSelected(true);
        ivSlideUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ivSlideUpDown.isSelected()) {
                    ivSlideUpDown.setSelected(true);
                    ivSlideUpDown.setImageResource(R.drawable.down_white_arrow_icon);
                    text_list.setVisibility(View.VISIBLE);

                } else {
                    ivSlideUpDown.setSelected(false);
                    ivSlideUpDown.setImageResource(R.drawable.up_arrow_icon);
                    text_list.setVisibility(View.GONE);
                }
            }
        });
    }
}