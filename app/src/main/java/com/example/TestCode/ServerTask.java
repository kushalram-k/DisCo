package com.example.TestCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTask extends Thread {

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String message = reader.readLine();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write("Hello from the client!\n");
            writer.flush();


            client.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
