package com.blood.opengldemo.camera_filter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blood.opengldemo.R;
import com.blood.opengldemo.databinding.ActivityCameraFilterBinding;

import static android.opengl.GLSurfaceView.RENDERMODE_CONTINUOUSLY;

public class CameraFilterActivity extends AppCompatActivity {

    private ActivityCameraFilterBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_filter);
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
}