package com.example.TestCode;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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
                BigInteger p= new BigInteger("86383735498515442470694310751833982723879495920683551250620390160365051838871");
                BigInteger q= new BigInteger("60674162632997004273005648766578742160698248478315597973765512463047130671311");
                BigInteger n=p.multiply(q);
                BigInteger pMinusOne = p.subtract(BigInteger.ONE);
                BigInteger qMinusOne = q.subtract(BigInteger.ONE);
                BigInteger tot = pMinusOne.multiply(qMinusOne);

                BigInteger enc =new BigInteger("65537");
                BigInteger dec= enc.modInverse(tot);
                byte[] byteArray1 = message.getBytes(StandardCharsets.UTF_8);

                // Convert the byte array to a BigInteger
                BigInteger c = new BigInteger(1, byteArray1);
                BigInteger m = c.modPow(dec,n);
                byte [] byteArray2= c.toByteArray();

                String finalMessage = new String(byteArray2, StandardCharsets.UTF_8);

                uiHandler.post(() -> messageListener.onMessageReceived(finalMessage));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (outputWriter != null) {
            Log.d("ClientTask", "Sending message: " + message);
            BigInteger p= new BigInteger("86383735498515442470694310751833982723879495920683551250620390160365051838871");
            BigInteger q= new BigInteger("60674162632997004273005648766578742160698248478315597973765512463047130671311");
            BigInteger n=p.multiply(q);

            BigInteger enc =new BigInteger("65537");
            byte[] byteArray1 = message.getBytes(StandardCharsets.UTF_8);

            // Convert the byte array to a BigInteger
            BigInteger m = new BigInteger(1, byteArray1);
            BigInteger c = m.modPow(enc,n);
            byte [] byteArray2= c.toByteArray();

            final String Cipher = new String(byteArray2, StandardCharsets.UTF_8);
            outputWriter.println(message);
        } else {
            Log.e("ClientTask", "OutputWriter is null, cannot send message.");
        }
    }


    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }
}