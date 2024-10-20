package com.example.TestCode;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTask extends Thread {

    private Socket clientSocket;
    private ServerSocket serverSocket;
    private PrintWriter outputWriter;
    private BufferedReader inputReader;
    private OnMessageReceivedListener messageListener;
    private Handler uiHandler;

    // Constructor that accepts a listener for message receiving
    public ServerTask(OnMessageReceivedListener listener, Handler handler) {
        this.messageListener = listener;
        this.uiHandler = handler;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8888);
            clientSocket = serverSocket.accept();

            // Setup for sending and receiving messages
            outputWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream())), true);

            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

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