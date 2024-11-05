package com.example.TestCode;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
//            Socket clientSocket = new Socket("SERVER_IP_ADDRESS", 8888); // Replace SERVER_IP_ADDRESS with actual IP
//            PrintWriter clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
//            clientOutput.println("Hello from client"); // Send a message to test the connection

            clientSocket = serverSocket.accept();
            Log.d("ServerTask", "Connected to client");
            // Setup for sending and receiving messages
            Log.d("ServerTask", "This is second");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(clientSocket != null){
            try {
                outputWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream())), true);
                Log.d("ServerTask", "This is third");
                inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = inputReader.readLine()) != null) {
                    Log.d("ServerTask", "Received message: " + message);
                    String finalMessage = message;
                    uiHandler.post(() -> messageListener.onMessageReceivedFromClient(finalMessage));
                }
            } catch (IOException e) {
                Log.d("ServerTask", "exception");
                e.printStackTrace();
            }
        }
    }

//    public void sendMessage(String message) {
//        if (outputWriter != null) {
//            Log.d("ClientTask", "preparing to Sending message: " + message);
//            outputWriter.println(message);
//            Log.d("ClientTask", "Sent message: " + message);
//        } else {
//            Log.e("ClientTask", "OutputWriter is null, cannot send message.");
//        }
//    }
public void sendMessage(String message) {
    Log.d("ServerTask", "This is fourth");
    try {
        if (outputWriter == null && clientSocket != null) {
            outputWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream())), true);
        }
        if (outputWriter != null) {
            Log.d("ServerTask", "Sending message: " + message);
            outputWriter.println(message);
            Log.d("ServerTask", "Sent message: " + message);
        } else {
            Log.e("ServerTask", "OutputWriter is still null, cannot send message.");
        }
    } catch (Exception e) {
        Log.e("ServerTask", "Error while sending message", e);
    }
}


    public interface OnMessageReceivedListener {
        void onMessageReceivedFromClient(String message);
    }
}











//package com.example.TestCode;
//
//import android.os.Handler;
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;

//public class ServerTask extends Thread {
//
//    private Socket clientSocket;
//    private ServerSocket serverSocket;
//    private PrintWriter outputWriter;
//    private BufferedReader inputReader;
//    private OnMessageReceivedListener messageListener;
//    private Handler uiHandler;
//
//    // Constructor that accepts a listener for message receiving
//    public ServerTask(OnMessageReceivedListener listener, Handler handler) {
//        this.messageListener = listener;
//        this.uiHandler = handler;
//    }
//
//    @Override
//    public void run() {
//        try {
//            // Initialize the server socket on port 8888
//            serverSocket = new ServerSocket(8888);
//            Log.d("ServerTask", "ServerSocket created on port 8888");
//
//            // Wait for a client connection
//            clientSocket = serverSocket.accept();
//            Log.d("ServerTask", "Connected to client");
//            Log.d("ServerTask", "This is second");
//            // Setup for sending and receiving messages
//            outputWriter = new PrintWriter(new BufferedWriter(
//                    new OutputStreamWriter(clientSocket.getOutputStream())), true);
//            Log.d("ServerTask", "This is third");
//            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//            // Listen for messages from the client
//            String message;
//            while ((message = inputReader.readLine()) != null) {
//                Log.d("ServerTask", "Received message: " + message);
//                String finalMessage = message;
//                uiHandler.post(() -> messageListener.onMessageReceivedFromClient(finalMessage));
//            }
//        } catch (Exception e) {
//            Log.e("ServerTask", "Error in ServerTask", e);
//        } finally {
//            // Ensure resources are closed to avoid port conflicts
//            try {
//                if (clientSocket != null) clientSocket.close();
//                if (serverSocket != null) serverSocket.close();
//                Log.d("ServerTask", "Server and client sockets closed");
//            } catch (Exception e) {
//                Log.e("ServerTask", "Error closing sockets", e);
//            }
//        }
//    }
//
//    // Method to send a message to the client
//    public void sendMessage(String message) {
//        Log.d("ServerTask", "This is fourth");
//        Log.d("ServerTask", "Preparing to send message: " + message);
//        try {
//            // Initialize outputWriter if not set
//            if (outputWriter == null && clientSocket != null) {
//                outputWriter = new PrintWriter(new BufferedWriter(
//                        new OutputStreamWriter(clientSocket.getOutputStream())), true);
//            }
//            if (outputWriter != null) {
//                Log.d("ServerTask", "Sending message: " + message);
//                outputWriter.println(message);
//                Log.d("ServerTask", "Message sent: " + message);
//            } else {
//                Log.e("ServerTask", "OutputWriter is null, cannot send message.");
//            }
//        } catch (Exception e) {
//            Log.e("ServerTask", "Error while sending message", e);
//        }
//    }
//
//    // Listener interface to handle messages received from the client
//    public interface OnMessageReceivedListener {
//        void onMessageReceivedFromClient(String message);
//    }
//}
