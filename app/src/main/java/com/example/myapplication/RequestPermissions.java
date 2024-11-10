package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class RequestPermissions extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int NEARBY_WIFI_DEVICES_PERMISSION_REQUEST_CODE = 102;
    private static final int WIFI_ENABLE_REQUEST_CODE = 103;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        checkAndEnableWifi();
        // Check if the necessary permissions are already granted based on the Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 and above (API 33) requires NEARBY_WIFI_DEVICES permission
            checkAndRequestNearbyWifiDevicesPermission();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android Q and above (API 29) requires ACCESS_FINE_LOCATION and CHANGE_WIFI_STATE
            checkAndRequestLocationAndWifiPermissions();
        } else {
            // For older versions, just proceed
            finish();
        }
    }

    // Method to check if Wi-Fi is enabled and prompt the user to enable it
    private void checkAndEnableWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        // Check if Wi-Fi is enabled
        if (!wifiManager.isWifiEnabled()) {
            // If Wi-Fi is disabled, prompt the user to enable it
            Toast.makeText(this, "Wi-Fi is disabled. Enabling Wi-Fi is required.", Toast.LENGTH_SHORT).show();
            Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivityForResult(wifiIntent, WIFI_ENABLE_REQUEST_CODE);
        }
    }


    // Check and request NEARBY_WIFI_DEVICES for Android 13 and above (API 33)
    private void checkAndRequestNearbyWifiDevicesPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Request NEARBY_WIFI_DEVICES permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.NEARBY_WIFI_DEVICES,Manifest.permission.ACCESS_FINE_LOCATION},
                    NEARBY_WIFI_DEVICES_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions already granted, close the activity
            finish();
        }
    }

    // Check and request ACCESS_FINE_LOCATION and CHANGE_WIFI_STATE for Android Q and above (API 29)
    private void checkAndRequestLocationAndWifiPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request the missing permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // If permissions are already granted, close the activity
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Handle result for ACCESS_FINE_LOCATION and CHANGE_WIFI_STATE
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, start Wi-Fi Direct service
                startWiFiDirectService();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Permissions required for Wi-Fi Direct", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == NEARBY_WIFI_DEVICES_PERMISSION_REQUEST_CODE) {
            // Handle result for NEARBY_WIFI_DEVICES permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start Wi-Fi Direct service
                startWiFiDirectService();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Nearby Wi-Fi devices permission required for Wi-Fi Direct", Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity after requesting permissions
        finish();
    }

    private void startWiFiDirectService() {
        // You can start the service again if needed after permission is granted
//        Intent serviceIntent = new Intent(this, Networkservice.class);
//        startService(serviceIntent);
          Intent intent = new Intent(this, mainPage.class);
          startActivity(intent);
    }
}

