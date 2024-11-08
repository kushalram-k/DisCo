package com.example.myapplication;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerTask extends Thread {

    ServerSocket serverSocket;
    List<SendReceive> clients;
    private Handler handler;
    private MyApplication app;
    public ServerTask(Handler handler, MyApplication app){
        this.handler = handler;
        this.app = app;
        clients = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());

                // Create a new SendReceive object for each client and start it in a separate thread
                SendReceive sendReceiveObj = new SendReceive(socket, handler, app);
                clients.add(sendReceiveObj);
                sendReceiveObj.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ChatMessage message) {
        for(SendReceive sendReceive : clients) {

            if (sendReceive != null) {
                try {
                    // Serialize the object
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();

                    // Send the serialized object as a byte array
                    sendReceive.write(byteArrayOutputStream.toByteArray());
                } catch (IOException e) {
                    Log.e("sendMessage", "Error serializing object", e);
                }
            } else {
                Log.d("serverTask", "connection not established");
            }
        }
    }
}