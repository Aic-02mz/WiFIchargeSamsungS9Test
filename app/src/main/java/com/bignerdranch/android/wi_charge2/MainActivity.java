package com.bignerdranch.android.wi_charge2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView mConnectionStatusTextView;
    private wifiListAdapter mWifiViewModel;
    private WifiManager wifiManager;
    private WifiReceiver receiver;
    private TextView connectionStatus;
    private LinearLayout settingsLayout;
    private SwitchCompat chargingSwitch;

    private final BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkConnection();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        receiver = new WifiReceiver();
        connectionStatus = findViewById(R.id.connection_status);
        Button settingsButton = findViewById(R.id.settings_button);
        settingsLayout = findViewById(R.id.settings_layout);
        chargingSwitch = findViewById(R.id.charging_switch);
        ListView wifiList = findViewById(R.id.wifi_list);

        ArrayList<String> wifiNetworks = new ArrayList<>();
        ArrayList<ScanResult> wifiListAdapter = null;
        wifiListAdapter adapter = new wifiListAdapter(this, wifiListAdapter);
        wifiList.setAdapter(adapter);

        // Check for stable WiFi connection
        checkConnection();

        settingsButton.setOnClickListener(view -> {
            // Show/hide settings screen
            if (settingsLayout.getVisibility() == View.GONE) {
                settingsLayout.setVisibility(View.VISIBLE);
            } else {
                settingsLayout.setVisibility(View.GONE);
            }
        });

        chargingSwitch.setOnClickListener(view -> {
            // Enable/disable charging feature
            chargingSwitch.isChecked();// Turn on charging
// Turn off charging
        });
    }

    private void checkConnection() {
        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            connectionStatus.setText(R.string.not_connected);
        } else if (info.getType() != ConnectivityManager.TYPE_WIFI) {            connectionStatus.setText(R.string.not_wifi);
        } else {
            int strength = WifiManager.calculateSignalLevel(wifiManager.getConnectionInfo().getRssi(), 5);
            if (strength < 2) {
                connectionStatus.setText(R.string.weak_connection);
            } else {
                connectionStatus.setText(R.string.connected);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register WiFi receiver
        registerReceiver(receiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister WiFi receiver
        unregisterReceiver(receiver);
    }

    private class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update connection status when WiFi state changes
            checkConnection();
        }
    }
}

