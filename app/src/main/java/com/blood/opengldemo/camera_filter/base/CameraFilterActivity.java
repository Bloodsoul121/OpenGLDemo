package com.blood.opengldemo.camera_filter.base;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blood.opengldemo.R;
import com.blood.opengldemo.camera_filter.view.CameraView;
import com.blood.opengldemo.camera_filter.view.RecordButton;
import com.blood.opengldemo.databinding.ActivityCameraFilterBinding;

public class CameraFilterActivity extends AppCompatActivity implements RecordButton.OnRecordListener, RadioGroup.OnCheckedChangeListener {

    private ActivityCameraFilterBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_filter);
        init();
    }

    private void init() {
        mBinding.btnRecord.setOnRecordListener(this);
        mBinding.rgSpeed.setOnCheckedChangeListener(this);
        mBinding.outH264.setOnClickListener(v -> {
            mBinding.outH264.setEnabled(false);
            mBinding.cameraView.switchOutH264(true);
        });
        mBinding.toggleSoul.setOnClickListener(v -> mBinding.cameraView.toggleSoulFilter());
        mBinding.toggleSplit2.setOnClickListener(v -> mBinding.cameraView.toggleSplit2Filter());
        mBinding.toggleAdapt.setOnClickListener(v -> mBinding.cameraView.toggleAdaptFilter());
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mBinding.cameraView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mBinding.cameraView.onPause();
//    }

    @Override
    public void onRecordStart() {
        mBinding.cameraView.startRecord();
    }

    @Override
    public void onRecordStop() {
        mBinding.cameraView.stopRecord();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_extra_slow:
                mBinding.cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_SLOW);
                break;
            case R.id.btn_slow:
                mBinding.cameraView.setSpeed(CameraView.Speed.MODE_SLOW);
                break;
            case R.id.btn_normal:
                mBinding.cameraView.setSpeed(CameraView.Speed.MODE_NORMAL);
                break;
            case R.id.btn_fast:
                mBinding.cameraView.setSpeed(CameraView.Speed.MODE_FAST);
                break;
            case R.id.btn_extra_fast:
                mBinding.cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_FAST);
                break;
        }
    }
}