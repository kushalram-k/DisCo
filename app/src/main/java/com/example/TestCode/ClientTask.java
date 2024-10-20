package com.example.TestCode;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTask extends Thread {

    private Socket socket;
    private String hostAddress;
    private PrintWriter outputWriter;
    private BufferedReader inputReader;
    private OnMessageReceivedListener messageListener;
    private Handler uiHandler;

    public ClientTask(String hostAddress, OnMessageReceivedListener listener, Handler handler) {
        this.hostAddress = hostAddress;
        this.messageListener = listener;
        this.uiHandler = handler;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(hostAddress, 8888);

            // Setup for sending and receiving messages
            outputWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);

            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = inputReader.readLine()) != null) {
                Log.d("ServerTask", "Received message: " + message);
                String finalMessage = message;
                uiHandler.post(() -> messageListener.onMessageReceived(finalMessage));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (outputWriter != null) {
            Log.d("ClientTask", "Sending message: " + message);
            outputWriter.println(message);
        } else {
            Log.e("ClientTask", "OutputWriter is null, cannot send message.");
        }
    }


    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }
}