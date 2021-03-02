package com.blood.opengldemo.camera_filter;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import androidx.camera.core.Preview;
import androidx.fragment.app.FragmentActivity;

import com.blood.opengldemo.util.LogUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FilterRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener {

    private final CameraXHelper mCameraXHelper;
    private SurfaceTexture mCameraTexture;

    public FilterRender(FragmentActivity activity) {
        mCameraXHelper = new CameraXHelper(activity, this);
        mCameraXHelper.openCamera();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.log("onSurfaceCreated");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.log("onSurfaceChanged");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        LogUtil.log("onDrawFrame");
    }

    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        LogUtil.log("onUpdated");
        // 摄像头预览到的数据 在这里
        mCameraTexture = output.getSurfaceTexture();
    }
}
