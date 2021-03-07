package com.blood.opengldemo.util;

import android.widget.Toast;

import com.blood.opengldemo.main.MainApplication;

public class ToastUtil {

    public static void toast(String msg) {
        MainApplication.getInstance().runUITask(() -> {
            Toast toast = Toast.makeText(MainApplication.getContext(), "", Toast.LENGTH_SHORT);
            toast.setText(msg);
            toast.show();
        });
    }

}
