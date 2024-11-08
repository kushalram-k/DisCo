package com.example.myapplication;

import android.database.Cursor;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class ChatMessage implements Serializable {
    private  String userId;
    private String text;
    private long timestamp;
    private boolean isSent;  // True if the message is sent, false if received
    private String grp;


    public ChatMessage(String text, long timestamp, boolean isSent, String grpName,String userId) {
        this.userId=userId;
        this.text = text;
        this.timestamp = timestamp;
        this.isSent = isSent;
        this.grp = grpName;
    }



    public String getUserId(){return userId;}

    public String getText() {
        return text;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public String getGrpName(){return grp;}
}