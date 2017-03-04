package com.techpro.chat.ticklechat.controller;

import android.content.Context;
import android.util.Log;

import com.techpro.chat.ticklechat.database.DataBaseHelper;
import com.techpro.chat.ticklechat.models.Messages;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sagars on 10/19/16.
 */
public class MessageController {

    private Context mcontext;
    private static ArrayList<Messages> messageList = null;

    public MessageController(Context context) {
        mcontext = context;
        if (messageList == null) {
            DataBaseHelper dbh = new DataBaseHelper(mcontext);
            dbh.openDataBase();
            messageList = dbh.GetMeMessage();
        }
    }

    public ArrayList<Messages> getMessages(){
        ArrayList<Messages> messageList1 = new ArrayList<>();
        Random r = new Random();
        int i1 = r.nextInt(288) + 1;
        messageList1.add(messageList.get(i1));
//        Log.e("ssssssss", "data=> "+i1);
//        Log.e("ssssssss", "data=> "+(messageList.get(i1).getMessage()));
        i1 = r.nextInt(288) + 1;
        messageList1.add(messageList.get(i1));
//        Log.e("ssssssss", "data=> "+i1);
//        Log.e("ssssssss", "data=> "+(messageList.get(i1).getMessage()));
        i1 = r.nextInt(288) + 1;
        messageList1.add(messageList.get(i1));
//        Log.e("ssssssss", "data=> "+i1);
//        Log.e("ssssssss", "data=> "+(messageList.get(i1).getMessage()));
        i1 = r.nextInt(288) + 1;
        messageList1.add(messageList.get(i1));
//        Log.e("ssssssss", "data=> "+i1);
//        Log.e("ssssssss", "data=> "+(messageList.get(i1).getMessage()));
        i1 = r.nextInt(288) + 1;
        messageList1.add(messageList.get(i1));
//        Log.e("ssssssss", "data=> "+i1);
//        Log.e("ssssssss", "data=> "+(messageList.get(i1).getMessage()));

        return messageList1;
    }
}
