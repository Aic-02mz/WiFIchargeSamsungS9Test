package com.bignerdranch.android.wi_charge2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {

    private final WifiChargingService service;

    public WifiReceiver(WifiChargingService service) {
        this.service = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check for stable WiFi connection
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            service.stopCharging();
        } else if (info.getType() != ConnectivityManager.TYPE_WIFI) {
            service.stopCharging();
        } else {
            int strength = WifiManager.calculateSignalLevel(((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getRssi(), 5);
            if (strength < 2) {
                service.stopCharging();
            } else {
                service.startCharging();
            }
        }
    }
}
