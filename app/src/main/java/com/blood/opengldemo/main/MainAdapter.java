package com.blood.opengldemo.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blood.opengldemo.R;
import com.blood.opengldemo.databinding.LayoutItemMainActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<BindingViewHolder<LayoutItemMainActivityBinding>> implements View.OnClickListener {

    private final Context mContext;
    private final List<MainActivityBean> mDatas = new ArrayList<>();
    private final BindingCallback<MainActivityBean> mCallback;

    public MainAdapter(Context context, List<MainActivityBean> list, BindingCallback<MainActivityBean> callback) {
        mContext = context;
        mCallback = callback;
        mDatas.clear();
        mDatas.addAll(list);
    }

    @NonNull
    @Override
    public BindingViewHolder<LayoutItemMainActivityBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemMainActivityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_item_main_activity, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<LayoutItemMainActivityBinding> holder, int position) {
        MainActivityBean bean = mDatas.get(position);
        holder.mBinding.content.setTag(bean);
        holder.mBinding.content.setText(bean.mContent);
        holder.mBinding.content.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        MainActivityBean bean = (MainActivityBean) v.getTag();
        mCallback.onItemClick(bean);
    }

}
