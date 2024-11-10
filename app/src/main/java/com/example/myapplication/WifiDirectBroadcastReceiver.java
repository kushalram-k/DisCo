package com.example.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.TestCode.Test_Activity;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private mainPage service;
    WifiP2pManager.PeerListListener myPeerListListener;

    public WifiDirectBroadcastReceiver(){
        super();
        this.manager = null;
        this.channel = null;
        this.service = null;
    }

    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       mainPage service) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.service = service;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();


        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            //Indicates whether wifi-p2p is enabled


            int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
//            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
//                Toast.makeText(context, "Wifi is on", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(context, "wifi is off", Toast.LENGTH_SHORT).show();
//            }
            // This is the name of your device

        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            if(manager!=null){
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
                manager.requestPeers(channel,service.peerListListener);
            }
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){

            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo != null && networkInfo.isConnected()) {
                // Connected to another device
                manager.requestConnectionInfo(channel, service.connectionInfoListener); // Request connection info
            } else {
                // Disconnected
//                Toast.makeText(service, "Disconnected", Toast.LENGTH_SHORT).show();
            }

        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);

            if (device != null) {
                String deviceName = device.deviceName;
                Log.d("BroadcastReceiver", "Local Device Name: " + deviceName);
                if(ClientManager.deviceID == null) ClientManager.deviceID = deviceName;
            }
        }
    }


}
