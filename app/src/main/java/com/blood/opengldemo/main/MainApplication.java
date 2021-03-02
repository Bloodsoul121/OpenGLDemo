package com.blood.opengldemo.main;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static MainApplication mApplication;

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

}
