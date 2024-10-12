package com.example.TestCode;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class peerConnection extends Service{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;



    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            Log.d("peer handler", "message received");
            manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//            channel = manager.initialize(getApplicationContext(), getMainLooper(), null);
//            receiver = new WifiDirectBroadcastReceiver(manager, channel, this);

            intentFilter = new IntentFilter();
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), android.Manifest.permission.NEARBY_WIFI_DEVICES) ==
                    PackageManager.PERMISSION_GRANTED) {
                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                    }
                });
            } else {
                Intent intent = new Intent(getApplicationContext(), Test_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("REQUEST_PERMISSION", true); // Use extra to signal permission request
                startActivity(intent);

            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }

            Log.d("peer handler", "waking up");

            stopSelf(msg.arg1);
        }

    }


    @Override
    public void onCreate(){
        HandlerThread thread = new HandlerThread("peerConnector",Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        Toast.makeText(this,"finding peers", Toast.LENGTH_SHORT).show();

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startID;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy(){
            Toast.makeText(this,"peer discovery terminated", Toast.LENGTH_SHORT).show();
        }
    }
