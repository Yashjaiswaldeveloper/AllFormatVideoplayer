package com.ffapp.ffxx.ffplayers.adsmanager;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.fftwo.ffftwo.gametwo.activity.SplashActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.pesonal.adsdk.AppManage;

import java.util.Date;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";

    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private static boolean isShowingAd = false;
    private final MyApp myApplication;
    private long loadTime = 0;

    private static String TAG = AppOpenManager.class.getSimpleName ();

    public AppOpenManager (MyApp myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks (this);
        ProcessLifecycleOwner.get ().getLifecycle ().addObserver (this);

    }


    @OnLifecycleEvent (ON_START)
    public void onStart () {

        if (currentActivity != null) {
            if (currentActivity instanceof SplashActivity) {
            } else {
                Log.d (LOG_TAG, "onStart" + currentActivity);
                if (!isShowingAd) {

                }

            }

        }


    }

    /**
     * Request an ad
     */

    public void fetchAd () {

        if (AppManage.app_adShowStatus == 0) {
            return;
        }



    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest () {
        return new AdRequest.Builder ().setHttpTimeoutMillis (10000).build ();
    }


    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo (long numHours) {
        long dateDifference = (new Date ()).getTime () - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable () {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo (4);
    }

    @Override
    public void onActivityCreated (@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted (@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed (@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused (@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped (@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState (@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed (@NonNull Activity activity) {
        currentActivity = null;
    }



}

