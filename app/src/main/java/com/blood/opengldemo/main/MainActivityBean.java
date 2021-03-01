package com.blood.opengldemo.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivityBean {

    public Context mContext;
    public String mContent;
    public Class<? extends Activity> mClazz;

    public MainActivityBean(Context context, String content, Class<? extends Activity> clazz) {
        mContext = context;
        mContent = content;
        mClazz = clazz;
    }

    public void skip() {
        Intent intent = new Intent(mContext, mClazz);
        mContext.startActivity(intent);
    }

}
