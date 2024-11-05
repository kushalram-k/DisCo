package com.example.myapplication;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    private MessageListener messageListener;
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }

    public void setMessageListener(MessageListener messageListener){
        this.messageListener = messageListener;
    }

    public MessageListener getMessageListener(){
        return messageListener;
    }
}
