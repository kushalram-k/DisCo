package com.example.TestCode;

import android.net.wifi.p2p.WifiP2pDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class NetworkManager {

    private Map<WifiP2pDevice,List<WifiP2pDevice>> adjList;

    public List<WifiP2pDevice> createRoutingList(WifiP2pDevice source ,WifiP2pDevice destination){

        List<WifiP2pDevice> routingList = new ArrayList<>();

        routingList = BFS(adjList,source,destination);

        return routingList;
    }

    public void addPeer(WifiP2pDevice currPeer, WifiP2pDevice newPeer){
        this.adjList.get(currPeer).add(newPeer);
        this.adjList.get(newPeer).add(currPeer);
    }

    public void notifyNetwork(){
        notifyPeer();
    }

    private void notifyPeer(){

    }

    private List<WifiP2pDevice> BFS(Map<WifiP2pDevice, List<WifiP2pDevice>> adjList, WifiP2pDevice source, WifiP2pDevice destination) {
        Queue<WifiP2pDevice> q = new LinkedList<>();
        q.offer(source);

        Map<WifiP2pDevice, WifiP2pDevice> predecessors = new HashMap<>();  // To store the predecessor of each node
        Set<WifiP2pDevice> visited = new HashSet<>();
        visited.add(source);

        // BFS loop
        while (!q.isEmpty()) {
            WifiP2pDevice front = q.poll();

            // If we reach the destination, we reconstruct the path
            if (front.equals(destination)) {
                return constructPath(predecessors, source, destination);
            }

            // Visit neighbors
            for (WifiP2pDevice neighbor : adjList.getOrDefault(front, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    q.offer(neighbor);
                    visited.add(neighbor);
                    predecessors.put(neighbor, front);


                    if (neighbor.equals(destination)) {
                        return constructPath(predecessors, source, destination);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private List<WifiP2pDevice> constructPath(Map<WifiP2pDevice, WifiP2pDevice> predecessors, WifiP2pDevice source, WifiP2pDevice destination) {
        List<WifiP2pDevice> path = new LinkedList<>();
        for (WifiP2pDevice at = destination; at != null; at = predecessors.get(at)) {
            path.add(0, at);
        }
        return path;
    }

}
