package com.bignerdranch.android.wi_charge2;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class WifiChargingService extends Service {

    private static final String TAG = "WifiChargingService";

    private ChargingClass chargingClass;
    private WifiReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        // Create charging class and WiFi receiver
        chargingClass = new ChargingClass(this);
        receiver = new WifiReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        // Start charging and register WiFi receiver
        startCharging();
        registerReceiver(receiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        // Return sticky to keep the service running in the background
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // Stop charging and unregister WiFi receiver
        stopCharging();
        unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startCharging() {
        Log.d(TAG, "startCharging");
        chargingClass.startCharging();
    }

    public void stopCharging() {
        Log.d(TAG, "stopCharging");
        chargingClass.stopCharging();
    }
}
