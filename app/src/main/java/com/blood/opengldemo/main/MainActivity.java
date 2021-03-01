package com.blood.opengldemo.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blood.opengldemo.R;
import com.blood.opengldemo.databinding.ActivityMainBinding;
import com.blood.opengldemo.opengl.OpenglActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BindingCallback<MainActivityBean> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new MainAdapter(this, prepareActivityList(), this));
    }

    private List<MainActivityBean> prepareActivityList() {
        List<MainActivityBean> list = new ArrayList<>();
        list.add(new MainActivityBean(this, "Opengl 示例", OpenglActivity.class));
        return list;
    }

    @Override
    public void onItemClick(MainActivityBean bean) {
        bean.skip();
    }
}