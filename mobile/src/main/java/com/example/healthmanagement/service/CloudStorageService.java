package com.example.healthmanagement.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.healthmanagement.datebase.CloudDataBaseHelper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
public class CloudStorageService extends IntentService {
    private static final String TAG = "TAG" + "CloudStorageService";

    private static final String ACTION_UPLOAD= "com.example.healthmanagement.service.action.FOO";
    private static final String ACTION_DOWNLOAD = "com.example.healthmanagement.service.action.BAZ";


    public CloudStorageService() {
        super("CloudStorageService");
    }


    public static void startActionUpload(Context context) {
        Log.d(TAG, "startActionUpload: ");
        Intent intent = new Intent(context, CloudStorageService.class);
        intent.setAction(ACTION_UPLOAD);
        context.startService(intent);
    }


    public static void startActionDownload(Context context) {
        Log.d(TAG, "startActionDownload: ");
        Intent intent = new Intent(context, CloudStorageService.class);
        intent.setAction(ACTION_DOWNLOAD);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD.equals(action)) {
                Log.d(TAG, "onHandleIntent: start upload ");
                CloudDataBaseHelper.upLoad();
            } else if (ACTION_DOWNLOAD.equals(action)) {
                Log.d(TAG, "onHandleIntent: start download");

            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
