package com.example.myapplication;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTask extends Thread {

    ServerSocket serverSocket;
    SendReceive sendReceive;
    private Handler handler;
    private MyApplication app;
    public ServerTask(Handler handler, MyApplication app){
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8888);
            Socket socket = serverSocket.accept();
            sendReceive = new SendReceive(socket,handler,app);
            sendReceive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        if (sendReceive != null) {
            sendReceive.write(message.getBytes());
        } else {
            Log.d("serverTask","connection not established");
        }
    }
}