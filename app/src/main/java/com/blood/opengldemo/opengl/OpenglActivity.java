package com.blood.opengldemo.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blood.opengldemo.R;
import com.blood.opengldemo.databinding.ActivityOpenglBinding;
import com.blood.opengldemo.opengl.graph.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLSurfaceView.RENDERMODE_CONTINUOUSLY;

public class OpenglActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private ActivityOpenglBinding mBinding;
    private Triangle mTriangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_opengl);
        mBinding.glSurface.setEGLContextClientVersion(2);
        mBinding.glSurface.setRenderer(this);
        mBinding.glSurface.setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.glSurface.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.glSurface.onPause();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 渲染一层黑色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 绘制三角形
        mTriangle.draw();
    }

}