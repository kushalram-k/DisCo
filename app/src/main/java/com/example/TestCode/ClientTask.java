package com.example.TestCode;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

class ClientTask extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String hostAddress;
    private Activity activity;

    public ClientTask(String hostAddress, Activity activity) {
        this.hostAddress = hostAddress;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            // Connect to the server (group owner)
            socket = new Socket(hostAddress, 8888);

            // Setup input/output streams
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

//            String message;
//            while ((message = input.readLine()) != null) {
//                // Process the received message and update UI
//                activity.runOnUiThread(() -> updateChatUI(message));
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Call this method to send a message to the server
    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    // Call this to update the chat UI
    private void updateChatUI(String message) {
        // Update your ListView or TextView here with the received message
    }
}
