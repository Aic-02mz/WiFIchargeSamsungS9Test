package com.bignerdranch.android.wi_charge2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends AppCompatActivity {

    private Switch wifiSwitch;
    private ListView wifiListView;
    private WifiManager wifiManager;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private BroadcastReceiver wifiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        wifiSwitch = findViewById(R.id.wifiSwitch);
        wifiListView = findViewById(R.id.wifiListView);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiSwitch.setChecked(wifiManager.isWifiEnabled());

        wifiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiSwitch.isChecked()) {
                    wifiManager.setWifiEnabled(true);
                } else {
                    wifiManager.setWifiEnabled(false);
                }
            }
        });

        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ssid = results.get(position).SSID;
                Toast.makeText(WifiActivity.this, ssid, Toast.LENGTH_SHORT).show();
            }
        });

        wifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                results = wifiManager.getScanResults();
                unregisterReceiver(this);

                for (ScanResult scanResult : results) {
                    arrayList.add(scanResult.SSID);
                    adapter.notifyDataSetChanged();
                }
            }
        };

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        wifiListView.setAdapter(adapter);
    }

    public void getWifiList(View view) {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

    public void checkWifiConnection(View view) {
        if (wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Wifi is enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wifi is disabled", Toast.LENGTH_SHORT).show();
        }
    }
}