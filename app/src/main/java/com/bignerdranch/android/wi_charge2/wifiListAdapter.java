package com.bignerdranch.android.wi_charge2;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class wifiListAdapter extends ArrayAdapter<ScanResult> {

    public wifiListAdapter(Context context, ArrayList<ScanResult> wifiNetworks) {
        super(context, 0, wifiNetworks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ScanResult wifiNetwork = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView wifiName = convertView.findViewById(R.id.wifi_name);
        // Populate the data into the template view using the data object
        wifiName.setText(wifiNetwork.SSID);
        // Return the completed view to render on screen
        return convertView;
    }
}

