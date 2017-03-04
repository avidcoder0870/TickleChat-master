package com.techpro.chat.ticklechat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.controller.MessageController;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.Messages;
import com.techpro.chat.ticklechat.models.message.AllMessages;
import com.techpro.chat.ticklechat.models.message.SendMessage;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    private ArrayList<Messages> moviesList;
    private boolean showCheckbox = false;
    private boolean showBelowDesc = false;
    private String sentID = "";
    private int isGroup = 0;
    private Context mContext = null;
    private DataUpdated dataupdate = null;

    public MessageAdapter( ArrayList<Messages> moviesList, boolean showCheckbox, boolean showBelowDesc) {
        this.moviesList = moviesList;
        this.showCheckbox = showCheckbox;
        this.showBelowDesc = showBelowDesc;
    }

    public MessageAdapter( ArrayList<Messages> moviesList, Context context, boolean showCheckbox, boolean showBelowDesc,
                           String sentID, int isGroup) {
        this.moviesList = moviesList;
        this.showCheckbox = showCheckbox;
        this.showBelowDesc = showBelowDesc;
        this.isGroup = isGroup;
        this.sentID = sentID;
        this.mContext = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_text_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Messages movie = moviesList.get(position);
        if(!TextUtils.isEmpty(movie.getMessage())) {
            try {
                String messages  = movie.getMessage().replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                messages = URLDecoder.decode(messages, "UTF-8");
                holder.tvChatMessage.setText(messages);
            } catch (Exception e) {
                holder.tvChatMessage.setText(movie.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (moviesList == null)
            return 0;
        return moviesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvChatMessage;
//        public ImageView friendImage;
//        public CheckBox addfriends;
//        public LinearLayout backgroundlayout;

        public MyViewHolder(View view) {
            super(view);
            Log.e("RecyclerView", "ssssssssssgetPosition：" + getPosition());
//            backgroundlayout = (LinearLayout) view.findViewById(R.id.backgroundlayout);
            tvChatMessage = (TextView) view.findViewById(R.id.tv_chat_message);
//            friendNumber = (TextView) view.findViewById(R.id.friendNumber);
//            friendImage = (ImageView) view.findViewById(R.id.friendImage);
//            addfriends = (CheckBox) view.findViewById(R.id.checkBox);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("RecyclerView", "getPosition：" + getPosition());
                    Log.e("RecyclerView", "getAdapterPosition：" + getAdapterPosition());
                    Log.e("RecyclerView", "getLayoutPosition：" + getLayoutPosition());
                    if (!AppUtils.isNetworkConnectionAvailable(mContext)) {
                        Toast.makeText(mContext,
                                mContext.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dataupdate.dataUpdated(false,isGroup,sentID,String.valueOf(moviesList.get(getPosition()).getID()),moviesList.get(getPosition()).getMessage());
//                    callSendMessageService(String.valueOf(moviesList.get(getPosition()).getID()),sentID,moviesList.get(getPosition()).getMessage());
                    moviesList = new MessageController(mContext).getMessages();
                    notifyDataSetChanged();
//                    if (mContext != null){
//                        Intent intent = new Intent(mContext, ChatScreen.class);
//                        mContext.startActivity(intent);
//                    }
                }
            });
        }
    }

    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private void callSendMessageService(String tickleId, String toID, String message) {
        //Getting webservice instance which we need to call
        Call<SendMessage> callForUserDetailsFromID = ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId()).create(ApiInterface.class).postChatMessage(tickleId,toID,message);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<SendMessage>() {
            @Override
            public void onResponse(Call<SendMessage> call, Response<SendMessage> response) {
                if (response != null && response.body()!= null) {
                    if (response.body().getStatus().equals("success")) {
                        moviesList = new MessageController(mContext).getMessages();
                        notifyDataSetChanged();
                        AllMessages.MessageList.ChatMessagesList msg= new AllMessages().new MessageList().new ChatMessagesList();
                        msg.setFrom_id(response.body().getBody().getFrom_id());
                        msg.setId(response.body().getBody().getId());
                        msg.setIsgroup(String.valueOf(isGroup));
                        msg.setMessage(response.body().getBody().getMessage());
                        msg.setRead("0");
                        msg.setSentat(response.body().getBody().getSent());
                        msg.setTickle_id(response.body().getBody().getTickle_id());
                        msg.setTo_id(response.body().getBody().getTo_id());
                        List<AllMessages.MessageList.ChatMessagesList> usermessages  = (List<AllMessages.MessageList.
                                ChatMessagesList>) SharedPreferenceUtils.getColleactionObject(mContext,msg.getTo_id());
                        if (usermessages == null)
                            usermessages =  new ArrayList<AllMessages.MessageList.ChatMessagesList>();
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
                        SharedPreferenceUtils.setColleactionObject(mContext,msg.getTo_id(),usermessages);
                        dataupdate.dataUpdated(true,isGroup,sentID,sentID,sentID);
                    }
                } else {
                    Log.e("SendMessage", "Success callMessage_ALL_Service but null response ==> "+response.body());
                }
            }

            @Override
            public void onFailure(Call<SendMessage> call, Throwable t) {
                // Log error here since request failed
                Log.e("SendMessage", t.toString());
//                dialog.dismiss();
            }
        });

    }

    public interface DataUpdated {
        void dataUpdated(boolean setRefresh,int isgroup, String id, String tickleId, String message);
    }

    public void setDataUpdateListener(DataUpdated dataupdate){
        this.dataupdate = dataupdate;
    }
}
