package com.example.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTask extends Thread {

    Socket socket;
    InetAddress hostAddress;
    Handler handler;
    SendReceive sendReceive;
    private MyApplication app;
    public ClientTask(InetAddress hostAddress,Handler handler,MyApplication app) {
        this.hostAddress = hostAddress;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(hostAddress, 8888), 5000);
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