package com.blood.opengldemo.camera_filter.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.blood.opengldemo.camera_filter.base.CameraRenderer;

public class CameraView extends GLSurfaceView {

    private CameraRenderer mCameraRenderer;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        mCameraRenderer = new CameraRenderer(this);
        setRenderer(mCameraRenderer);
        //注意必须在setRenderer后面
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    private Speed mSpeed = Speed.MODE_NORMAL;

    public enum Speed {
        MODE_EXTRA_SLOW, MODE_SLOW, MODE_NORMAL, MODE_FAST, MODE_EXTRA_FAST
    }

    public void setSpeed(Speed speed) {
        this.mSpeed = speed;
    }

    public void startRecord() {
        //速度  时间/速度 speed小于就是放慢 大于1就是加快
        float speed = 1.f;
        switch (mSpeed) {
            case MODE_EXTRA_SLOW:
                speed = 0.3f;
                break;
            case MODE_SLOW:
                speed = 0.5f;
                break;
            case MODE_NORMAL:
                speed = 1.f;
                break;
            case MODE_FAST:
                speed = 2.f;
                break;
            case MODE_EXTRA_FAST:
                speed = 3.f;
                break;
        }
        mCameraRenderer.startRecord(speed);
    }

    public void stopRecord() {
        mCameraRenderer.stopRecord();
    }

    public void switchOutH264(boolean isOutH264) {
        mCameraRenderer.switchOutH264(isOutH264);
    }

    public void toggleSoulFilter() {
        mCameraRenderer.toggleSoulFilter();
    }

    public void toggleSplit2Filter() {
        mCameraRenderer.toggleSplit2Filter();
    }

}
