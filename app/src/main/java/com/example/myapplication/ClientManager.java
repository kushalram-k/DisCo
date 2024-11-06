package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientManager {
    private static Map<String, ClientTask> clientTasks = new HashMap<>();

    private static ServerTask serverTask;

    // Add a client task for a specific client identified by some unique key (e.g., device address)
    public static void addClientTask(String clientKey, ClientTask clientTask) {
        clientTasks.put(clientKey, clientTask);
    }

    public static void setServerTask(ServerTask s){
        serverTask = s;
    }

    public static ServerTask getServerTask(){
        return serverTask;
    }

    // Remove a client task
    public static void removeClientTask(String clientKey) {
        clientTasks.remove(clientKey);
    }

    // Get the client task for a specific client
    public static ClientTask getClientTask(String clientKey) {
        return clientTasks.get(clientKey);
    }

    public static List<ClientTask> getAll(){
        List<ClientTask> l = new ArrayList<>();
        for(Map.Entry<String, ClientTask> client : clientTasks.entrySet()){
            l.add(client.getValue());
        }

        return l;
    }

    // Send message to the client
    public static void sendMessageToClient(String clientKey, String message) {
        ClientTask clientTask = clientTasks.get(clientKey);
        if (clientTask != null) {
            clientTask.sendMessage(message);
        }
    }
}
