package com.techpro.chat.ticklechat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techpro.chat.ticklechat.models.Messages;
import com.techpro.chat.ticklechat.models.message.Tickles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagars on 10/19/16.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ticklechat.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;
//    private boolean isSavingDone = true;
//    private ArrayList<Messages> savedmessage;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
//        savedmessage = GetMeMessage();
    }

    public void insertMessages(ArrayList<Tickles.MessageList.ChatMessagesTicklesList> msgs){
//        SQLiteDatabase db = this.getWritableDatabase();
//        isSavingDone = false;
//        for (int i=0; i<msgs.size(); i++) {
//
//            if (isMessageExist(msgs.get(i).getId())) {
//                continue;
//            }
//
//            Log.e("profile", "inserter ==> "+msgs.get(i).getMessage());
//            ContentValues values = new ContentValues();
//            values.put("id", msgs.get(i).getId());
//            values.put("message", msgs.get(i).getMessage());
//            values.put("requested_by", msgs.get(i).getRequested_by());
//            values.put("approved", msgs.get(i).getApproved());
//            values.put("modified_at", msgs.get(i).getRequested_at());
//            db.insert("tickle_messages", null, values);
//        }
//        isSavingDone = true;
//        db.close();
    }
    // Getting single contact
    public ArrayList<Messages> GetMeMessage() {

//        if (isSavingDone == false)
//            return savedmessage;

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Messages> messages = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM 'tickle_messages';", null);
        if (cursor != null ) {
            cursor.moveToFirst();
            do {
                Messages cont = new Messages(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
                messages.add(cont);
            } while (cursor.moveToNext());

// return contact
            cursor.close();
            db.close();

            return messages;

        }
        return null;
    }

    // Getting single contact
    public boolean isMessageExist(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Messages> messages = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tickle_messages where id == "+id+";", null);
        if (cursor != null ) {
            if (cursor.getCount() == 0) {
                cursor.close();
                db.close();
                return false;
            }
        }
        cursor.close();
        db.close();
        return true;
    }

    public void CopyDataBaseFromAsset() throws IOException {

        Log.e("sagar","CopyDataBaseFromAsset m==> ");
        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

// Path to the just created empty db
        String outFileName = getDatabasePath();
        Log.e("sagar","outFileName m==> "+outFileName);

// if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        Log.e("sagar","ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX m==> "+ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists()) {
            Log.e("sagar","f.exists==> "+false);
            f.mkdir();
        }

// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        Log.e("sagar","f.exists==> write done");

// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);
        Log.e("sagar","DATABASE_NAME m==> "+DATABASE_NAME);
        Log.e("sagar","dbFile.exists() m==> "+dbFile.exists());

        if (!dbFile.exists()) {
            Log.e("sagar","exists m==> "+false);
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub

    }
}
