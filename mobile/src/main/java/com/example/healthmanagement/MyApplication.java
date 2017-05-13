package com.example.healthmanagement;

import android.app.Application;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVOSCloud;
import com.facebook.stetho.Stetho;

import org.litepal.LitePal;

/**
 * Created by zyx10 on 2017/5/12 0012.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        Stetho.initializeWithDefaults(this);
        AVOSCloud.initialize(this, "eOvH3EDHGuxb7W76prT9oj0g-gzGzoHsz", "EAQtjdNOUJAyNyCQcIq16t8z");
        AVOSCloud.setDebugLogEnabled(true);

    }
}
