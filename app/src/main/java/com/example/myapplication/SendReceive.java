package com.example.myapplication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import android.os.Handler;
import android.util.Log;


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

    public InetAddress getIPaddress(){
        return socket.getInetAddress();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket != null) {
            try {
                bytes = inputStream.read(buffer);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer, 0, bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object receivedObject = objectInputStream.readObject();

                // Update UI with the deserialized object
                if (receivedObject instanceof ChatMessage) {
                    // Cast the object to ChatMessage
                    ChatMessage chatMessage = (ChatMessage) receivedObject;

                    // Update UI or process the ChatMessage object
                    MessageListener listener = app.getMessageListener();
                    if (listener != null) {
                        listener.onMessageReceived(chatMessage);
                    }

                    if(ClientManager.getServerTask() != null){
                        ClientManager.getServerTask().sendMessage(chatMessage);

                    }

                } else {
                    // Handle the case where the object is not a ChatMessage
                    Log.e("receiveMessage", "Received object is not a ChatMessage");
                }
            } catch (IOException e) {
                e.printStackTrace();
                break; // Exit the loop if an exception occurs
            } catch (ClassNotFoundException e) {
                Log.e("SendReceive : ", "Error deserializing object",e);
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