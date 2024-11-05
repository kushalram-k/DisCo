package com.example.myapplication;

public class ClientTaskHolder {
    private static ClientTask clientTask;

    public static void setClientTask(ClientTask task){
        clientTask = task;
    }

    public static ClientTask getClientTask(){
        return clientTask;
    }
}
