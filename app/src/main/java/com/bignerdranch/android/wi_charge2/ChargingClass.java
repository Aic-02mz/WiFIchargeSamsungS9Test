package com.bignerdranch.android.wi_charge2;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

public class ChargingClass {

    private final PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private final WifiManager wifiManager;

    public ChargingClass(Context context) {
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public void startCharging() {
        // Acquire wake lock
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WifiCharger:WakeLock");
        wakeLock.acquire(10*60*1000L /*10 minutes*/);
        // Enable WiFi
        wifiManager.setWifiEnabled(true);
    }

    public void stopCharging() {
        // Release wake lock
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        // Disable WiFi
        wifiManager.setWifiEnabled(false);
    }
}
