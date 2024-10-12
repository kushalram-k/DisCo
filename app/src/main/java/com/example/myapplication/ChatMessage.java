package com.example.myapplication;

public class ChatMessage {
    private String text;
    private long timestamp;
    private boolean isSent;  // True if the message is sent, false if received

    public ChatMessage(String text, long timestamp, boolean isSent) {
        this.text = text;
        this.timestamp = timestamp;
        this.isSent = isSent;
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
}