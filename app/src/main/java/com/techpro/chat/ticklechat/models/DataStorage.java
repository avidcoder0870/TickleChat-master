package com.techpro.chat.ticklechat.models;

import com.techpro.chat.ticklechat.models.message.AllMessages;
import com.techpro.chat.ticklechat.models.message.Tickles;
import com.techpro.chat.ticklechat.models.user.GetUserDetailsBody;
import com.techpro.chat.ticklechat.models.user.User;
import com.techpro.chat.ticklechat.models.user.UserDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sagar on 10/22/2016.
 */

public class DataStorage {

    public static UserDetailsModel UserDetails = null;
    public static User currentUserDetailsBody = null;
    public static ArrayList<AllMessages.MessageList.ChatMessagesList> allMessages = null;
    public static User randomUser = null;
    public static List<User> myAllUserlist = null;
    public static List<User> chatUserList = null;
    public static List<Group> mygrouplist = null;
//    public static HashMap<User,List<AllMessages.MessageList.ChatMessagesList>> mychatMessages = null;
//    public static HashMap<Group,List<AllMessages.MessageList.ChatMessagesList>> myGroupchatMessages = null;

}
