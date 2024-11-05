package com.example.myapplication;
import com.example.myapplication.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivityMainPageBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class mainPage extends AppCompatActivity {
    private Button discoverButton;
    WifiManager wifimanager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiDirectBroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    public ServerTask serverTask;
    ClientTask clientTask;
    SendReceive sendReceive;
    Handler handler;


    private static final int CONNECTION_TIMEOUT = 1000; // 5 seconds timeout
    private int currentDeviceIndex = 0; // To keep track of the current device in the list
    private Handler connectionHandler = new Handler();
    private Runnable timeoutRunnable;


    @NonNull ActivityMainPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the correct layout (activity_main_page.xml)
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());

        // Set the content view to the root of the binding
        setContentView(binding.getRoot());
//        Toast.makeText(mainPage.this,"mainPage",Toast.LENGTH_SHORT).show();
//        startService(new Intent(this, Networkservice.class));
//        Intent serviceIntent = new Intent(this, Networkservice.class);
//        startService(serviceIntent);

        initialWork();

        discoverButton = findViewById(R.id.discoverButton);
        discoverButton.setOnClickListener(v -> discover());

        // Now you can reference bottomNavigationView correctly
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new groupFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.group) {
                replaceFragment(new groupFragment());
            } else if (itemId == R.id.person) {
                replaceFragment(new personFragment());
            } else if (itemId == R.id.settings) {
                replaceFragment(new settingsFragment());
            }
            return true;
        });
    }

    public ServerTask getServerTask() {
        return serverTask;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void initialWork(){

        wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mReceiver = new WifiDirectBroadcastReceiver(mManager,mChannel,this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void discover() {
        Toast.makeText(mainPage.this,"Clicked",Toast.LENGTH_SHORT).show();
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(mainPage.this,"Permissions denied",Toast.LENGTH_SHORT).show();
//            return;
//        }
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mainPage.this, "Peer discovery initiated", Toast.LENGTH_SHORT).show();
                connect();
            }

            @Override
            public void onFailure(int reasonCode) {
                String message;
                switch (reasonCode) {
                    case WifiP2pManager.ERROR:
                        message = "Internal error occurred.";
                        break;
                    case WifiP2pManager.P2P_UNSUPPORTED:
                        message = "Wi-Fi Direct is not supported on this device.";
                        break;
                    case WifiP2pManager.BUSY:
                        message = "Wi-Fi Direct is busy. Please try again.";
                        break;
                    default:
                        message = "Unknown error.";
                        break;
                }
                Toast.makeText(mainPage.this, "Peer discovery failed: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connect() {
        if (!peers.isEmpty() && currentDeviceIndex < deviceArray.length) {
            Toast.makeText(mainPage.this, "Starting connection", Toast.LENGTH_SHORT).show();
            WifiP2pDevice device = deviceArray[currentDeviceIndex];
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;

//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }

            // Initialize a timeout runnable
            timeoutRunnable = new Runnable() {
                @Override
                public void run() {
                    // If connection is not successful in the given timeout, move to the next device
                    Toast.makeText(mainPage.this, "Connection attempt timed out for " + device.deviceName, Toast.LENGTH_SHORT).show();
                    currentDeviceIndex++;
                    connect(); // Attempt to connect to the next device
                }
            };

            // Start the timeout countdown
            connectionHandler.postDelayed(timeoutRunnable, CONNECTION_TIMEOUT);

            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Remove the timeout callback since connection succeeded
                    connectionHandler.removeCallbacks(timeoutRunnable);
                    Toast.makeText(mainPage.this, "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int reason) {
                    // Remove the timeout callback since connection failed
                    connectionHandler.removeCallbacks(timeoutRunnable);
                    Toast.makeText(mainPage.this, "Device connection failed", Toast.LENGTH_SHORT).show();

                    // Move to the next device
                    currentDeviceIndex++;
                    connect();
                }
            });
        }else {
            Toast.makeText(mainPage.this,"No peer found", Toast.LENGTH_SHORT).show();
        }
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {
            if (!peersList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peersList.getDeviceList());
                Toast.makeText(mainPage.this,"Found " + peers.size() + " peers", Toast.LENGTH_SHORT).show();

                deviceNameArray = new String[peersList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peersList.getDeviceList().size()];
                int index = 0;
                for (WifiP2pDevice device : peersList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray);
//                lstvw.setAdapter(adapter);
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            if (info.groupFormed && info.isGroupOwner) {

                serverTask = new ServerTask(handler, (MyApplication) getApplication());
                serverTask.start();
            } else if (info.groupFormed) {

                clientTask = new ClientTask(info.groupOwnerAddress,handler,(MyApplication) getApplication());
                ClientTaskHolder.setClientTask(clientTask);
                clientTask.start();
            }
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverTask != null) {
            serverTask.interrupt();
        }
        if (clientTask != null) {
            clientTask.interrupt();
        }
    }
}