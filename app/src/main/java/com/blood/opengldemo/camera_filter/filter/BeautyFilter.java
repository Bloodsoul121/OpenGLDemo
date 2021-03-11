package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.blood.opengldemo.R;

public class BeautyFilter extends BaseFboFilter {

    private final int mVWidth;
    private final int mVHeight;

    public BeautyFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.beauty_frag);
        mVWidth = GLES20.glGetUniformLocation(mProgram, "width");
        mVHeight = GLES20.glGetUniformLocation(mProgram, "height");
    }

    @Override
    public void beforeDraw() {
        GLES20.glUniform1i(mVWidth, mWidth);
        GLES20.glUniform1i(mVHeight, mHeight);
    }
}
