package com.locus.locusdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class LocusDemo extends Application {

    private static LocusDemo mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = LocusDemo.this;
    }
    public synchronized static LocusDemo getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
