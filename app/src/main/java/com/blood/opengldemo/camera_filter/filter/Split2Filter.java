package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.blood.opengldemo.R;

public class Split2Filter extends BaseFboFilter {

    public Split2Filter(Context context) {
        super(context, R.raw.base_vert, R.raw.split2_frag);
    }

    @Override
    public void beforeDraw() {
    }

}
