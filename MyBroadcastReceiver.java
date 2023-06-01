package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private Context context;

    public MyBroadcastReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

             if (isConnected && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                onCellularDataStateChange(true);
            } else {
                onCellularDataStateChange(false);
            }
        }
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void unregister() {
        context.unregisterReceiver(this);
    }

    public void onCellularDataStateChange(boolean isCellularDataOn) {
    }
}