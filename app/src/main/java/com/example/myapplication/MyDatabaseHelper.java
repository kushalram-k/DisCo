package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME="ChatLibrary.db";
    public static final int DATABASE_VERSION=1;

//    public static final String TABLE_NAME="ChatMessages";
//    public static final String COLUMN_ID="_id";
//    public static final String COLUMN_GROUP="message_group";
//    public static final String COLUMN_SENDER="message_sender";
//    public static final String COLUMN_MESSAGE="message_message";
//    public static final String COLUMN_TIME="message_time";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String query="CREATE TABLE " + "ChatMessages"+ " ("+ "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "message_group" + " TEXT, " + "message_sender" + " TEXT, " + "message_message" + " TEXT, " + "message_time" + " INTEGER);";
        String query="CREATE TABLE ChatMessages (_id INTEGER PRIMARY KEY AUTOINCREMENT, message_group TEXT, message_sender TEXT, message_message TEXT, message_time INTEGER, message_sender_name TEXT);";
        String query2="CREATE TABLE PrivateMessages (_id INTEGER PRIMARY KEY AUTOINCREMENT, message_sender TEXT, message_receiver TEXT, message_message TEXT, message_time INTEGER);";
        String query3="CREATE TABLE forUserId (_id INTEGER PRIMARY KEY AUTOINCREMENT, userIdInDB TEXT, userNameInDB TEXT);";
        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "ChatMessages");
        onCreate(db);
    }
    void addMessage(String group, String sender, String message, long time,String sender_name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("message_group",group);
        cv.put("message_sender",sender);
        cv.put("message_message",message);
        cv.put("message_time",time);
        cv.put("message_sender_name",sender_name);
        long result=db.insert("ChatMessages",null,cv);
        if(result==-1){
            Toast.makeText(context, "Sending Message Failed...", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Message Sent...", Toast.LENGTH_SHORT).show();
        }
    }

    void addUserIdToDB(String userID,String userName){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("userIdInDB",userID);
        cv.put("userNameInDB",userName);
        long result=db.insert("forUserId",null,cv);
        if(result==-1){
            Toast.makeText(context, "Adding userId to DB failed...", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "UserId Added to DB...", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllDate(){
        String query="SELECT * FROM " + "ChatMessages";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        if(db != null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }


    Cursor readUserIdFromDb(){
        String query="SELECT * FROM " + "forUserId";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        if(db != null){
            cursor= db.rawQuery(query,null);
        }
        return cursor;
    }
}