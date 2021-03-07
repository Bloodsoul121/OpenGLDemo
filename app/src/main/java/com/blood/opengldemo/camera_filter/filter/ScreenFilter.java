package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;

import com.blood.opengldemo.R;

public class ScreenFilter extends BaseFilter {

    public ScreenFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
    }

    @Override
    public void beforeDraw() {

    }
}
