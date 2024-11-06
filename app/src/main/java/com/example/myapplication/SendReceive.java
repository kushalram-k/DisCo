package com.example.myapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import android.os.Handler;


public class SendReceive extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private MyApplication app;

    public SendReceive(Socket skt, Handler handler,MyApplication app) {
        socket = skt;
        this.handler = handler;
        this.app = app;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket != null) {
            try {
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    String message = new String(buffer,0,bytes);

                    //Update UI in chatPage
                    MessageListener listener = app.getMessageListener();
                    if(listener != null){
                        listener.onMessageReceived(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break; // Exit the loop if an exception occurs
            }
        }
    }

    public void write(final byte[] bytes) {
        new Thread(() -> {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface onMessageReceivedListener{
        void onMessageReceived(String message,long timestamp);
    }
}