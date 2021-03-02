package com.blood.opengldemo.util;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "LogUtil";

    public static void log(String msg) {
        Log.i(TAG, ">>> " + msg);
    }

}
