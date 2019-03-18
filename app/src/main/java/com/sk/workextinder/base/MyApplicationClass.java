package com.sk.workextinder.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

/*
 * Created by Sambhaji Karad on 12-03-2019
 */

public class MyApplicationClass extends Application {

    private static Context context;
    private static final String TAG = MyApplicationClass.class.getName();
    private static MyApplicationClass mInstance;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplicationClass.context = getApplicationContext();
        mInstance = this;

        // register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity arg0, Bundle arg1) {
                // new activity created; force its orientation to portrait
                arg0.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityDestroyed(Activity arg0) {
            }

            @Override
            public void onActivityPaused(Activity arg0) {
            }

            @Override
            public void onActivityResumed(Activity arg0) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
            }

            @Override
            public void onActivityStarted(Activity arg0) {
            }

            @Override
            public void onActivityStopped(Activity arg0) {
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return MyApplicationClass.context;
    }

    public static synchronized MyApplicationClass getInstance() {
        return mInstance;
    }
}