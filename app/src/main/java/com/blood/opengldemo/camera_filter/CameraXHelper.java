package com.blood.opengldemo.camera_filter;

import android.util.Size;

import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.UseCase;
import androidx.lifecycle.LifecycleOwner;

public class CameraXHelper {

    private final LifecycleOwner mLifecycleOwner;
    private final Preview.OnPreviewOutputUpdateListener mListener;

    public CameraXHelper(LifecycleOwner lifecycleOwner, Preview.OnPreviewOutputUpdateListener listener) {
        mLifecycleOwner = lifecycleOwner;
        mListener = listener;
    }

    public void openCamera() {
        CameraX.bindToLifecycle(mLifecycleOwner, getPreView());
    }

    private UseCase getPreView() {
        // 分辨率并不是最终的分辨率，CameraX会自动根据设备的支持情况，结合你的参数，设置一个最为接近的分辨率
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetResolution(new Size(640, 480))
                .setLensFacing(CameraX.LensFacing.BACK) //前置或者后置摄像头
                .build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(mListener);
        return preview;
    }

}
