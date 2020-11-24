package com.example.odds;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * Created by odds, 2020/11/6 15:08
 * Desc:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
