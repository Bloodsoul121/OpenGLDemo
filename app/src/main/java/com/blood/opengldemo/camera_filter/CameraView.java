package com.blood.opengldemo.camera_filter;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class CameraView extends GLSurfaceView {

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(new CameraRenderer(this));
        //注意必须在setRenderer后面
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }
}
