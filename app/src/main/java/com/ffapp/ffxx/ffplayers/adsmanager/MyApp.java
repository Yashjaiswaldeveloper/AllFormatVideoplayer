package com.ffapp.ffxx.ffplayers.adsmanager;

import android.app.Application;


public class MyApp extends Application {
    public String ONESIGNAL_APP_ID = "";

    public AppOpenManager getAppOpenManager () {
        return appOpenManager;
    }

    private AppOpenManager appOpenManager;
    @Override
    public void onCreate () {
        super.onCreate ();


        appOpenManager = new AppOpenManager (this);
    }
}
