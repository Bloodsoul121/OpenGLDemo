package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.blood.opengldemo.R;

public class WarmFilter extends BaseFboFilter {

    public WarmFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.warm_frag);
    }

    @Override
    public void beforeDraw() {
    }

}
