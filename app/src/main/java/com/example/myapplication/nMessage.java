package com.example.myapplication;

public class nMessage {
    String user_id;
    String message_id;
    boolean group_flag;
    int group_number;
    String text;
    long timestamp;
    boolean isSent;

    public nMessage(String user_id, String text, long timestamp, boolean isSent,boolean group_flag,int group_number) {
        this.text = text;
        this.timestamp = System.currentTimeMillis();
        this.isSent = isSent;
        this.group_flag = group_flag;
        this.user_id = user_id;
        this.group_number = group_number;
        this.message_id = user_id + "_" + timestamp;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public boolean getFlag(){
        return group_flag;
    }
}
