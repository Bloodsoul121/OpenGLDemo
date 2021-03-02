package com.blood.opengldemo.main;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blood.opengldemo.R;
import com.blood.opengldemo.camera_filter.CameraFilterActivity;
import com.blood.opengldemo.databinding.ActivityMainBinding;
import com.blood.opengldemo.opengl.OpenglActivity;
import com.blood.opengldemo.util.ToastUtil;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements BindingCallback<MainActivityBean> {

    private ActivityMainBinding mBinding;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
        requestPermissions();
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        mDisposable = rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        )
                .subscribe(aBoolean -> ToastUtil.toast(aBoolean ? "accept" : "deny"));
    }

    private void init() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(new MainAdapter(this, prepareActivityList(), this));
    }

    private List<MainActivityBean> prepareActivityList() {
        List<MainActivityBean> list = new ArrayList<>();
        list.add(new MainActivityBean(this, "Opengl 示例", OpenglActivity.class));
        list.add(new MainActivityBean(this, "Opengl 相机滤镜", CameraFilterActivity.class));
        return list;
    }

    @Override
    public void onItemClick(MainActivityBean bean) {
        bean.skip();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}