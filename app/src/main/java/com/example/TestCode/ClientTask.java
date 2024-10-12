package com.example.TestCode;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientTask extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String hostAddress;
    private Test_Activity activity;

    public ClientTask(String hostAddress, Test_Activity activity) {
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

            // Listen for incoming messages
            String message;
            while ((message = input.readLine()) != null) {
                // Update UI on the main thread when a message is received
                final String receivedMessage = message;
                activity.runOnUiThread(() -> updateChatUI(receivedMessage));
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, notify the user via UI in case of connection failure
            activity.runOnUiThread(() -> Toast.makeText(activity, "Connection failed", Toast.LENGTH_SHORT).show());
        } finally {
            // Close the socket and streams when finished
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Call this method to send a message to the server
    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    // Method to update the chat UI
    private void updateChatUI(String message) {
        // Update your ListView or TextView here with the received message
        activity.updateUI(message);
    }
}
