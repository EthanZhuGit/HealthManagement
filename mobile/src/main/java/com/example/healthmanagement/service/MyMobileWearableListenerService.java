package com.example.healthmanagement.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

public class MyMobileWearableListenerService extends WearableListenerService {
    private static final String TAG = "TAG" + "MobileWearable";
    private LocalBroadcastManager localBroadcastManager;
    public MyMobileWearableListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
        Log.d(TAG, "onPeerConnected: "+peer.getId()+" "+peer.getDisplayName());
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        int heartRate=bytesToInt(messageEvent.getData());
        Intent intent = new Intent("heart_rate_from_device");
        intent.putExtra("heart_rate", heartRate);
        localBroadcastManager.sendBroadcast(intent);
        Log.d(TAG, "onMessageReceived: "+heartRate);
    }

    public int bytesToInt(byte[] b) {

        int mask=0xff;
        int temp=0;
        int n=0;
        for(int i=0;i<b.length;i++){
            n<<=8;
            temp=b[i]&mask;
            n|=temp;
        }
        return n;
    }
}
