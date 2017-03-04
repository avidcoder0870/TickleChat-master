package com.techpro.chat.ticklechat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.activity.ChatScreen;
import com.techpro.chat.ticklechat.fragments.NewGroupFragment;
import com.techpro.chat.ticklechat.models.user.User;

import java.util.ArrayList;
import java.util.List;

public class AddGroupMembersAdapter extends RecyclerView.Adapter<AddGroupMembersAdapter.MyViewHolder>{

    private List<User> moviesList;
    private boolean showCheckbox = false;
    private boolean showBelowDesc = false;
    private Context mContext = null;

    public AddGroupMembersAdapter(List<User> moviesList, boolean showCheckbox, boolean showBelowDesc) {
        this.moviesList = moviesList;
        this.showCheckbox = showCheckbox;
        this.showBelowDesc = showBelowDesc;
    }

    public AddGroupMembersAdapter(List<User> moviesList, Context context, boolean showCheckbox, boolean showBelowDesc) {
        this.moviesList = moviesList;
        this.showCheckbox = showCheckbox;
        this.showBelowDesc = showBelowDesc;
        this.mContext = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tickle_friend_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User movie = moviesList.get(position);
        holder.friendName.setText(movie.getName());
        if (movie.getProfile_image() != null) {
            byte[] decodedString = Base64.decode(movie.getProfile_image().getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (decodedByte!=null)
                holder.friendImage.setImageBitmap(decodedByte);
        }
        if (showCheckbox) {
            holder.addfriends.setVisibility(View.VISIBLE);
        } else {
            holder.addfriends.setVisibility(View.INVISIBLE);
        }
        if (movie.getUser_status() != null)
            holder.friendNumber.setText(movie.getUser_status());
        if (showBelowDesc) {
            holder.friendNumber.setVisibility(View.VISIBLE);
        } else {
            holder.friendNumber.setVisibility(View.INVISIBLE);
        }
        Log.e("(position % 2) => "+position,"(position % 2)=>"+(position % 2));
//        if ((position % 2) == 0) {
//            holder.backgroundlayout.setBackgroundColor(Color.parseColor("#f1f1f1"));
//        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView friendName,  friendNumber;
        public ImageView friendImage;
        public CheckBox addfriends;
        public LinearLayout backgroundlayout;

        public MyViewHolder(View view) {
            super(view);
            backgroundlayout = (LinearLayout) view.findViewById(R.id.backgroundlayout);
            friendName = (TextView) view.findViewById(R.id.friendName);
            friendNumber = (TextView) view.findViewById(R.id.friendNumber);
            friendImage = (ImageView) view.findViewById(R.id.friendImage);
            addfriends = (CheckBox) view.findViewById(R.id.checkBox);
            addfriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d("RecyclerView", b+" getPosition：" + getPosition());
                    if (b == true) {
                        User user = moviesList.get(getPosition());
                        Log.d("RecyclerView", "getAdapterPosition：" + user.getName());
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (v.getTag() != null && ((boolean) v.getTag()) == true) {
                            boolean b = NewGroupFragment.addedUser.remove((moviesList.get(getPosition()).getId()));
                            v.setTag(false);
                            v.setBackgroundColor(Color.WHITE);
                        } else {
                            NewGroupFragment.addedUser.add(Integer.parseInt(moviesList.get(getPosition()).getId()));
                            v.setTag(true);
                            v.setBackgroundColor(0Xff33b5e5);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
//                    Log.d("RecyclerView", "getPosition：" + getPosition());
//                    Log.d("RecyclerView", "getAdapterPosition：" + getAdapterPosition());
//                    Log.d("RecyclerView", "getLayoutPosition：" + getLayoutPosition());
//                    User user = moviesList.get(getPosition());
//                    Intent intent = new Intent(mContext, ChatScreen.class);
//                    intent.putExtra("userid",user.getId());
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}
