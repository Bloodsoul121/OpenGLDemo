package com.blood.opengldemo.main;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class MainApplication extends Application {

    private static MainApplication mApplication;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static MainApplication getInstance() {
        return mApplication;
    }

    public static Context getContext() {
        return mApplication.getApplicationContext();
    }

    public void runUITask(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

}
