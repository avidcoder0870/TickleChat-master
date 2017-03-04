package com.techpro.chat.ticklechat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.controller.MessageController;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.message.AllMessages;
import com.techpro.chat.ticklechat.models.message.SendMessage;
import com.techpro.chat.ticklechat.models.user.User;
import com.techpro.chat.ticklechat.models.user.UserDetailsModel;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<AllMessages.MessageList.ChatMessagesList> moviesList;
    private boolean showCheckbox = false;
    private boolean showBelowDesc = false;
    private Bitmap leftbitmap = null;
    private Bitmap rightbitmap = null;

    private Context mContext = null;
    private MessageAdapter mAdapter1 = null;

    public ChatAdapter(List<AllMessages.MessageList.ChatMessagesList> moviesList, boolean showCheckbox, boolean showBelowDesc) {
        this.moviesList = moviesList;
        this.showCheckbox = showCheckbox;
        this.showBelowDesc = showBelowDesc;
    }

    public ChatAdapter(MessageAdapter mAdapter1, List<AllMessages.MessageList.ChatMessagesList> moviesList, Context context, boolean showCheckbox, boolean showBelowDesc) {
        this.moviesList = moviesList;
        this.showCheckbox = showCheckbox;
        this.showBelowDesc = showBelowDesc;
        this.mContext = context;
        this.mAdapter1 = mAdapter1;
        mAdapter1.setDataUpdateListener(new MessageAdapter.DataUpdated() {
            @Override
            public void dataUpdated(boolean setRefresh, final int isgroup, final String id, final String tickleId, final String message) {
//                if (setRefresh) {
//                    ChatAdapter.this.moviesList = (List<AllMessages.MessageList.ChatMessagesList>) SharedPreferenceUtils.
//                            getColleactionObject(mContext, id);
//                    notifyDataSetChanged();
//                    return;
//                }

                AllMessages.MessageList.ChatMessagesList msg = new AllMessages().new MessageList().new ChatMessagesList();
                msg.setFrom_id(DataStorage.UserDetails.getId());
                msg.setId(tickleId);
                msg.setIsgroup(String.valueOf(isgroup));
                msg.setMessage(message);
                msg.setRead("0");
                msg.setTickle_id(tickleId);
                msg.setTo_id(id);
                ChatAdapter.this.moviesList.add(msg);
                notifyDataSetChanged();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                        callSendMessageService(isgroup, tickleId, id, message);
//                    }
//                });
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_row_with_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AllMessages.MessageList.ChatMessagesList mChatMessageList = moviesList.get(position);
        if (DataStorage.UserDetails == null && DataStorage.UserDetails.getId() == null) {
            String json = SharedPreferenceUtils.getValue(mContext, SharedPreferenceUtils.LoginuserDetailsPreference, "");
            if (json.equals("")) {
            } else {
                Gson gson = new Gson();
                UserDetailsModel obj = gson.fromJson(json, UserDetailsModel.class);
                DataStorage.UserDetails = obj;
            }
        }

        if (DataStorage.UserDetails.getId().equalsIgnoreCase(mChatMessageList.getFrom_id())) {
            holder.llRightRow.setVisibility(View.VISIBLE);
            holder.llLeftRow.setVisibility(View.GONE);
            try {
                String messages = mChatMessageList.getMessage().replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                messages = URLDecoder.decode(messages, "UTF-8");
                holder.tvMessageRight.setText(messages);
            } catch (Exception e) {
                e.printStackTrace();
                holder.tvMessageRight.setText(mChatMessageList.getMessage());
            }
            try {
                if (leftbitmap == null) {
                    byte[] decodedString = Base64.decode(DataStorage.UserDetails.getProfile_image(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (decodedByte != null) {
                        leftbitmap = decodedByte;
                    } else {
                        leftbitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tickle_logo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.ivProfileRight.setImageBitmap(leftbitmap);
        } else {
            holder.llRightRow.setVisibility(View.GONE);
            holder.llLeftRow.setVisibility(View.VISIBLE);
            try {
                String messages = mChatMessageList.getMessage().replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                messages = URLDecoder.decode(messages, "UTF-8");
                holder.tvMessage.setText(messages);
            } catch (Exception e) {
                e.printStackTrace();
                holder.tvMessage.setText(mChatMessageList.getMessage());
            }

            try {
                User user = null;
                for (int i = 0; i < DataStorage.chatUserList.size(); i++) {
                    if (DataStorage.chatUserList.get(i).getId().equals(mChatMessageList.getFrom_id())) {
                        user = DataStorage.chatUserList.get(i);
                        break;
                    }
                }

                if (user != null) {
                    if (rightbitmap == null) {
                        byte[] decodedString = Base64.decode(user.getProfile_image(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        if (decodedByte != null) {
                            rightbitmap = decodedByte;
                        } else {
                            rightbitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tickle_logo);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.ivProfile.setImageBitmap(rightbitmap);
        }
    }

    @Override
    public int getItemCount() {
        if (moviesList == null)
            return 0;
        return moviesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessage;
        public CircularImageView ivProfile;
        public TextView tvMessageRight;
        public CircularImageView ivProfileRight;

        public LinearLayout llLeftRow;
        public LinearLayout llRightRow;


        public MyViewHolder(View view) {
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.tv_message);
            ivProfile = (CircularImageView) view.findViewById(R.id.iv_profile);
            tvMessageRight = (TextView) view.findViewById(R.id.tv_message_right);
            ivProfileRight = (CircularImageView) view.findViewById(R.id.iv_profile_right);

            llLeftRow = (LinearLayout) view.findViewById(R.id.ll_left_row);
            llRightRow = (LinearLayout) view.findViewById(R.id.ll_right_row);
        }
    }


    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private void callSendMessageService(final int isGroup, final String tickleId, final String toID, final String message) {
        //Getting webservice instance which we need to call
        Call<SendMessage> callForUserDetailsFromID = ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId()).create(ApiInterface.class).postChatMessage(tickleId, toID, message);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<SendMessage>() {
            @Override
            public void onResponse(Call<SendMessage> call, Response<SendMessage> response) {
                if (response != null && response.body() != null && response.body().getStatus().equals("success")) {
                    AllMessages.MessageList.ChatMessagesList msg = new AllMessages().new MessageList().new ChatMessagesList();
                    msg.setFrom_id(response.body().getBody().getFrom_id());
                    msg.setId(response.body().getBody().getId());
                    msg.setIsgroup(String.valueOf(isGroup));
                    msg.setMessage(response.body().getBody().getMessage());
                    msg.setRead("0");
                    msg.setSentat(response.body().getBody().getSent());
                    msg.setTickle_id(response.body().getBody().getTickle_id());
                    msg.setTo_id(response.body().getBody().getTo_id());
                    List<AllMessages.MessageList.ChatMessagesList> usermessages = (List<AllMessages.MessageList.
                            ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(mContext, msg.getTo_id());
                    if (usermessages == null)
                        usermessages = new ArrayList<AllMessages.MessageList.ChatMessagesList>();
                    usermessages.add(msg);
                    if (DataStorage.randomUser != null) {
                        DataStorage.chatUserList.add(DataStorage.randomUser);
                        DataStorage.myAllUserlist.add(DataStorage.randomUser);

                        SharedPreferenceUtils.setColleactionObject(mContext, SharedPreferenceUtils.myuserlist,
                                DataStorage.myAllUserlist);
                        SharedPreferenceUtils.setColleactionObject(mContext, SharedPreferenceUtils.chatUserID,
                                DataStorage.chatUserList);
                        DataStorage.randomUser = null;
                    }
                    SharedPreferenceUtils.setColleactionObject(mContext, msg.getTo_id(), usermessages);

                } else {
                    callSendMessageService(isGroup, tickleId, toID, message);
                    Log.e("SendMessage", "Success callMessage_ALL_Service but null response ==> " + response.body());
                }
            }

            @Override
            public void onFailure(Call<SendMessage> call, Throwable t) {
                // Log error here since request failed
                callSendMessageService(isGroup, tickleId, toID, message);
                Log.e("SendMessage", t.toString());
//                dialog.dismiss();
            }
        });

    }
}



