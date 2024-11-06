//package com.example.myapplication;
//
//
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.net.wifi.WifiManager;
//import android.net.wifi.WpsInfo;
//import android.net.wifi.p2p.WifiP2pConfig;
//import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pDeviceList;
//import android.net.wifi.p2p.WifiP2pInfo;
//import android.net.wifi.p2p.WifiP2pManager;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.Manifest;
//
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//
//import com.example.TestCode.Test_Activity;
//import com.google.android.material.textfield.TextInputEditText;
//
//
//import java.net.InetAddress;
//import java.security.Provider;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class Networkservice extends Service {
//
//    WifiManager wifimanager;
//    WifiP2pManager mManager;
//    WifiP2pManager.Channel mChannel;
//    BroadcastReceiver mReceiver;
//    IntentFilter mIntentFilter;
//
//    ServerTask serverTask;
//    ClientTask clientTask;
//    private Handler uiHandler;
//    private static final long DISCOVERY_PERIOD_MS = 30000; // Time to run the service (30 seconds)
//    private Handler discoveryHandler;
//    private Runnable discoveryRunnable;
//
//
//    ListView peerList;
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
//    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;
//    private static final int NEARBY_WIFI_DEVICES_PERMISSION_REQUEST_CODE = 102;
//
//
//
//    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
//    String[] deviceNameArray;
//    WifiP2pDevice[] deviceArray;
//
//    @Override
//    public void onCreate(){
//        super.onCreate();
//        Toast.makeText(Networkservice.this,"service started",Toast.LENGTH_SHORT).show();
//        init();
//    }
//
//    void init(){
//        wifimanager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel=mManager.initialize(this,getMainLooper(),null);
//
//        uiHandler = new Handler();
////        sendButton.setOnClickListener(new View.OnClickListener(){
////
////            @Override
////            public void onClick(View v){
////                Toast.makeText(Networkservice.this,"sending",Toast.LENGTH_SHORT).show();
////                sendMessage();
////
////            }
////        });
//
//        mReceiver=new WifiDirectBroadcastReceiver(mManager,mChannel,this);
//        mIntentFilter=new IntentFilter();
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//
//        Intent permissionIntent = new Intent(this, RequestPermissions.class);
//        permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(permissionIntent);
//        discover();
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent,int flags, int startId){
//        // Start peer discovery when service starts
//        Intent permissionIntent = new Intent(this, RequestPermissions.class);
//        permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(permissionIntent);
//        discover();
//
//        // Start a handler to check for connections periodically
//        startConnectionChecker();
//
//        // Stop service after a fixed amount of time
//        discoveryHandler.postDelayed(() -> {
//            if (peers.isEmpty()) { // Check if there are no connections
//                stopSelf(); // Stop the service
//                Toast.makeText(Networkservice.this, "No connections. Stopping service.", Toast.LENGTH_SHORT).show();
//            }
//        }, DISCOVERY_PERIOD_MS);
//        return START_STICKY;
//    }
//
//    private void startConnectionChecker() {
//        discoveryHandler = new Handler();
//        discoveryRunnable = new Runnable() {
//            @Override
//            public void run() {
//                if (peers.isEmpty()) {
//                    Toast.makeText(Networkservice.this, "No peers found. Retrying...", Toast.LENGTH_SHORT).show();
//                    discover(); // Retry discovering peers
//                }
//                discoveryHandler.postDelayed(this, 50000); // Check every 50 seconds
//            }
//        };
//        discoveryHandler.post(discoveryRunnable);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (discoveryHandler != null) {
//            discoveryHandler.removeCallbacks(discoveryRunnable); // Remove callbacks to avoid memory leaks
//        }
//    }
//
//    public void discover(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
//                @Override
//                public void onSuccess() {
//                    // Discovery initiated successfully
//                    Toast.makeText(Networkservice.this, "Peer discovery initiated", Toast.LENGTH_SHORT).show();
//                    connect();
//                }
//
//                @Override
//                public void onFailure(int reasonCode) {
//                    String failureMessage;
//                    switch (reasonCode) {
//                        case WifiP2pManager.P2P_UNSUPPORTED:
//                            failureMessage = "Wi-Fi Direct is not supported on this device.";
//                            break;
//                        case WifiP2pManager.ERROR:
//                            failureMessage = "An internal error occurred.";
//                            break;
//                        case WifiP2pManager.BUSY:
//                            failureMessage = "The system is busy, please try again.";
//                            break;
//                        default:
//                            failureMessage = "Unknown error occurred: " + reasonCode;
//                            break;
//                    }
//                    Toast.makeText(Networkservice.this, "Peer discovery failed: " + failureMessage, Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        } else {
//            Log.d("Tag", "Permission Denied. Cannot discover peers.");
//        }
//    }
//
//    public void connect() {
//        // Picking the first device found on the network.
//        if(!peers.isEmpty()) {
//            for (WifiP2pDevice device : peers) {
//                if (!Objects.equals(device.deviceName, "Mohd-Kaif")) {
//                    WifiP2pConfig config = new WifiP2pConfig();
//                    config.deviceAddress = device.deviceAddress;
//                    config.wps.setup = WpsInfo.PBC;
//
//                    Toast.makeText(getApplicationContext(), "Attempting connection to: " + device.deviceName, Toast.LENGTH_SHORT).show();
//
//                    // Check for permissions
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                            ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED) {
//
//                        // Handler for managing connection timeout
//                        Handler handler = new Handler();
//                        Runnable timeoutRunnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                // Timeout reached, attempt to cancel the connection if not successful
//                                Toast.makeText(getApplicationContext(), "Connection attempt to " + device.deviceName + " timed out.", Toast.LENGTH_SHORT).show();
//                                mManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                        Toast.makeText(getApplicationContext(), "Cancelled connection attempt to: " + device.deviceName, Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onFailure(int reason) {
//                                        Toast.makeText(getApplicationContext(), "Failed to cancel connection attempt to: " + device.deviceName, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        };
//
//                        // Start connection and set timeout
//                        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//                            @Override
//                            public void onSuccess() {
//                                // Cancel the timeout callback if connection is successful
//                                handler.removeCallbacks(timeoutRunnable);
//                                Toast.makeText(getApplicationContext(), "Connected to: " + device.deviceName, Toast.LENGTH_SHORT).show();
//                                mManager.requestConnectionInfo(mChannel, connectionInfoListener);
//
//                                // Stop further attempts after the first successful connection if desired
//                            }
//
//                            @Override
//                            public void onFailure(int reason) {
//                                // Connection attempt failed for this device
//                                handler.removeCallbacks(timeoutRunnable); // Ensure timeout isn't triggered if it fails quickly
//                                Toast.makeText(getApplicationContext(), "Connection failed to: " + device.deviceName + ". Retrying next device.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        // Set a timeout period for connection attempt (e.g., 10 seconds)
//                        handler.postDelayed(timeoutRunnable, 10000); // 10,000 ms = 10 seconds
//
//                        // Optional: Add a delay between connection attempts if needed (e.g., 2 seconds)
//                        try {
//                            Thread.sleep(2000); // 2000ms = 2 seconds
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private static class SendMessageTask extends AsyncTask<Void, Void, Void> {
//        private Object task; // Can be ServerTask or ClientTask
//        private String message;
//
//        SendMessageTask(Object task, String message) {
//            this.task = task;
//            this.message = message;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (task instanceof com.example.myapplication.ServerTask) {
//                ((com.example.myapplication.ServerTask) task).sendMessage(message);
//            } else if (task instanceof com.example.myapplication.ClientTask) {
//                ((com.example.myapplication.ClientTask) task).sendMessage(message);
//            }
//            return null;
//        }
//    }
//
//    public void sendMessage(String Message) {
//        String message = Message;
//
//        if (serverTask != null) {
//            new Networkservice.SendMessageTask(serverTask, message).execute();
//        } else if (clientTask != null) {
//            new Networkservice.SendMessageTask(clientTask, message).execute();
//        } else {
//            Toast.makeText(Networkservice.this, "Connection not established", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//
//    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
//        @Override
//        public void onConnectionInfoAvailable(WifiP2pInfo info) {
//            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
//
//            if (info.groupFormed && info.isGroupOwner) {
//                // This device is the group owner, act as server
//                Toast.makeText(getApplicationContext(), "Acting as server", Toast.LENGTH_SHORT).show();
//                Log.d("ConnectionInfo", "Device is the group owner");
//
//                // Create and start the ServerTask, and store the reference
////                serverTask = new com.example.myapplication.ServerTask(message -> {
////                    // Update the UI with the received message from the client
////                    runOnUiThread(() -> {
////                        Log.d("ServerTask", "Message received: " + message);
////                        receiverBox.setText(message);
////                    });
////                }, uiHandler);
//                serverTask.start(); // Start the server task
//
//            } else if (info.groupFormed) {
//                // This device is a client, connect to the group owner
//                Toast.makeText(getApplicationContext(), "Acting as client", Toast.LENGTH_SHORT).show();
//                Log.d("ConnectionInfo", "Device is a client, connecting to group owner: " + groupOwnerAddress.getHostAddress());
//
//                // Create and start the ClientTask, and store the reference
//                clientTask = new ClientTask(info.groupOwnerAddress, uiHandler);
//                clientTask.start(); // Start the client task
//            }
//        }
//    };
//
//
//
//
//    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
//        @Override
//        public void onPeersAvailable(WifiP2pDeviceList peersList) {
//            if(!peersList.getDeviceList().equals(peers)){
//                peers.clear();
//                peers.addAll(peersList.getDeviceList());
//
//                deviceNameArray=new String[peersList.getDeviceList().size()];
//                deviceArray=new WifiP2pDevice[peersList.getDeviceList().size()];
//                int index=0;
//                for(WifiP2pDevice device:peersList.getDeviceList()){
//                    deviceNameArray[index]=device.deviceName;
//                    deviceArray[index]=device;
//                    index++;
//                }
//                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,deviceNameArray);
//                peerList.setAdapter(adapter);
//            }
//
//            // Updating the ListView on the UI thread
////            runOnUiThread(() -> {
////                ArrayAdapter<String> adapter = new ArrayAdapter<>(Networkservice.this, android.R.layout.simple_list_item_1, deviceNameArray);
////                peerList.setAdapter(adapter);
////            });
//
//            if(peers.isEmpty()){
//                Toast.makeText(Networkservice.this, "No peer found", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    };
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // We don't provide binding, so return null
//        return null;
//    }
//}
