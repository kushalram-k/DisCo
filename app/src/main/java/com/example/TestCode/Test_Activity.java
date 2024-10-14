package com.example.TestCode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Test_Activity extends AppCompatActivity implements ClientTask.OnMessageReceivedListener {

    WifiManager wifimanager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    ServerTask serverTask;
    ClientTask clientTask;
    private Handler uiHandler;



    ListView peerList;
    Button searchButton;
    Button sendButton;
    TextInputEditText textInput;
    TextView receiverBox;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;
    private static final int NEARBY_WIFI_DEVICES_PERMISSION_REQUEST_CODE = 102;



    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart(){
        super.onStart();


    }

    void init(){
        wifimanager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);

        peerList = findViewById(R.id.peer_list);
        searchButton = findViewById(R.id.button_find);
        textInput = findViewById(R.id.text_input);
        sendButton = findViewById(R.id.button_send);
        receiverBox = findViewById(R.id.receiveBox);

        receiverBox.setText("");

        uiHandler = new Handler();

        searchButton.setOnClickListener(v -> discover());

        sendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Toast.makeText(Test_Activity.this,"sending",Toast.LENGTH_SHORT).show();
                sendMessage();

            }
        });

        mReceiver=new WifiDirectBroadcastReceiver(mManager,mChannel,this);
        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

                // Request permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return; // Exit the method
            }else{
                discover();
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.NEARBY_WIFI_DEVICES}, NEARBY_WIFI_DEVICES_PERMISSION_REQUEST_CODE);
            }
            else{
                discover();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, initiate peer discovery
                Log.d("Tag", "Location Permission Granted");
                discover();
            } else {
                Log.d("Tag", "Location Permission Denied");
                Toast.makeText(this, "Location permission is required for Wi-Fi Direct", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == NEARBY_WIFI_DEVICES_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Tag", "Nearby Wi-Fi Devices Permission Granted");
                discover();
            } else {
                Log.d("Tag", "Nearby Wi-Fi Devices Permission Denied");
                Toast.makeText(this, "Nearby Wi-Fi Devices permission is required for Wi-Fi Direct", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void discover() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Discovery initiated successfully
                    Toast.makeText(Test_Activity.this, "Peer discovery initiated", Toast.LENGTH_SHORT).show();
                    connect();
                }

                @Override
                public void onFailure(int reasonCode) {
                    String failureMessage;
                    switch (reasonCode) {
                        case WifiP2pManager.P2P_UNSUPPORTED:
                            failureMessage = "Wi-Fi Direct is not supported on this device.";
                            break;
                        case WifiP2pManager.ERROR:
                            failureMessage = "An internal error occurred.";
                            break;
                        case WifiP2pManager.BUSY:
                            failureMessage = "The system is busy, please try again.";
                            break;
                        default:
                            failureMessage = "Unknown error occurred: " + reasonCode;
                            break;
                    }
                    Toast.makeText(Test_Activity.this, "Peer discovery failed: " + failureMessage, Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Log.d("Tag", "Permission Denied. Cannot discover peers.");
        }
    }

    @Override
    public void onMessageReceived(String message) {
        // Update the UI when a message is received
        Toast.makeText(Test_Activity.this,"received",Toast.LENGTH_SHORT).show();
        runOnUiThread(() -> {
            receiverBox.setText(message);
        });
    }


    public void connect() {
        // Picking the first device found on the network.
        if(!peers.isEmpty()) {
            WifiP2pDevice device = peers.get(0);

            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            Toast.makeText(this, "connection try", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        // WiFiDirectBroadcastReceiver notifies us. Ignore for now.
                        Toast.makeText(Test_Activity.this, "Connection established", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(Test_Activity.this, "Connect failed. Retry.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void sendMessage() {
        String message = textInput.getText().toString();

        if (serverTask != null) {
            new SendMessageTask(serverTask, message).execute();
        } else if (clientTask != null) {
            new SendMessageTask(clientTask, message).execute();
        } else {
            Toast.makeText(Test_Activity.this, "Connection not established", Toast.LENGTH_SHORT).show();
        }
    }

    private static class SendMessageTask extends AsyncTask<Void, Void, Void> {
        private Object task; // Can be ServerTask or ClientTask
        private String message;

        SendMessageTask(Object task, String message) {
            this.task = task;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (task instanceof ServerTask) {
                ((ServerTask) task).sendMessage(message);
            } else if (task instanceof ClientTask) {
                ((ClientTask) task).sendMessage(message);
            }
            return null;
        }
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                // This device is the group owner, act as server
                Toast.makeText(getApplicationContext(), "Acting as server", Toast.LENGTH_SHORT).show();
                Log.d("ConnectionInfo", "Device is the group owner");

                // Create and start the ServerTask, and store the reference
                serverTask = new ServerTask(message -> {
                    // Update the UI with the received message from the client
                    runOnUiThread(() -> {
                        Log.d("ServerTask", "Message received: " + message);
                        receiverBox.setText(message);
                    });
                }, uiHandler);
                serverTask.start(); // Start the server task

            } else if (info.groupFormed) {
                // This device is a client, connect to the group owner
                Toast.makeText(getApplicationContext(), "Acting as client", Toast.LENGTH_SHORT).show();
                Log.d("ConnectionInfo", "Device is a client, connecting to group owner: " + groupOwnerAddress.getHostAddress());

                // Create and start the ClientTask, and store the reference
                clientTask = new ClientTask(groupOwnerAddress.getHostAddress(), message -> {
                    // Update the UI with the received message from the server
                    runOnUiThread(() -> {
                        Log.d("ClientTask", "Message received: " + message);
                        receiverBox.setText(message);
                    });
                }, uiHandler);
                clientTask.start(); // Start the client task
            }
        }
    };




    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {
            if(!peersList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peersList.getDeviceList());

                deviceNameArray=new String[peersList.getDeviceList().size()];
                deviceArray=new WifiP2pDevice[peersList.getDeviceList().size()];
                int index=0;
                for(WifiP2pDevice device:peersList.getDeviceList()){
                    deviceNameArray[index]=device.deviceName;
                    deviceArray[index]=device;
                    index++;
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,deviceNameArray);
                peerList.setAdapter(adapter);
            }

            // Updating the ListView on the UI thread
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Test_Activity.this, android.R.layout.simple_list_item_1, deviceNameArray);
                peerList.setAdapter(adapter);
            });

            if(peers.isEmpty()){
                Toast.makeText(Test_Activity.this, "No peer found", Toast.LENGTH_SHORT).show();

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}