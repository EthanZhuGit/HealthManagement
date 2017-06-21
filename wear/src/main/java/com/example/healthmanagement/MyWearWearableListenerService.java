package com.example.healthmanagement;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

public class MyWearWearableListenerService extends WearableListenerService {
    private static final String TAG = "TAG" + "MyWear";
    private SensorManager sensorManager;
    Sensor sensor;
    private LocalBroadcastManager localBroadcastManager;
    public MyWearWearableListenerService() {
    }
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this.getApplicationContext())
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
        Log.d(TAG, "onPeerConnected: "+peer.getId()+" "+peer.getDisplayName());
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if ("getHeartRate".equals(messageEvent.getPath())) {
            measureHeartRate();
        }

    }

    private void measureHeartRate() {
        sensorManager =(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor= sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void sendData(int heartRateData) {
        final byte[] data=intToBytes(heartRateData);
            PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
            nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                @Override
                public void onResult(NodeApi.GetConnectedNodesResult result) {
                    final List<Node> nodes = result.getNodes();
                    if (nodes != null) {
                        for (int i=0; i<nodes.size(); i++) {
                            final Node node = nodes.get(i);
                            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),"getHeartRate", data);
                            Log.d(TAG, "onMessageReceived: "+"wear send msg");
                        }
                    }
                }
            });
    }

    private SensorEventListener sensorEventListener=new SensorEventListener() {
        private int sensorChangedTimes;
        private int record;
        private int sum;
        private int heartRate;
        @Override
        public void onSensorChanged(SensorEvent event) {
            sensorChangedTimes++;
            if (sensorChangedTimes > 4 && sensorChangedTimes <=7) {
                record++;
                sum+=event.values[0];
                Intent intent = new Intent("data_update");
                intent.putExtra("rate", event.values[0]);
                localBroadcastManager.sendBroadcast(intent);
                Log.d(TAG, "onSensorChanged: "+record+" "+event.values[0]+" "+sum);
            }else if (sensorChangedTimes>7){
                heartRate=(int)Math.round((sum/1.0)/record);
                Log.d(TAG, "onSensorChanged: "+heartRate);
                Intent intent = new Intent("data_avg");
                intent.putExtra("avg", heartRate);
                sendData(heartRate);
                sensorManager.unregisterListener(sensorEventListener,sensor);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public  byte[] intToBytes(int n){
        byte[] b = new byte[4];

        for(int i = 0;i < 4;i++)
        {
            b[i]=(byte)(n>>(24-i*8));

        }
        return b;
    }


}
