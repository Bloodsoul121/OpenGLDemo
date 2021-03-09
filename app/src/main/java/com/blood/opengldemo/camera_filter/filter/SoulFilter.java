package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.blood.opengldemo.R;

public class SoulFilter extends BaseFboFilter {

    private int mixturePercent;
    private int scalePercent;
    private float mix = 0.0f; //透明度，越大越透明
    private float scale = 0.0f; //缩放，越大就放的越大

    public SoulFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.soul_frag);
        mixturePercent = GLES20.glGetUniformLocation(mProgram, "mixturePercent");
        scalePercent = GLES20.glGetUniformLocation(mProgram, "scalePercent");
    }

    @Override
    public void beforeDraw() {
        GLES20.glUniform1f(mixturePercent, 1.0f - mix);
        GLES20.glUniform1f(scalePercent, 1.0f + scale);
        mix += 0.025f;
        scale += 0.025f;
        if (mix >= 1.0) {
            mix = 0.0f;
        }
        if (scale >= 1.0) {
            scale = 0.0f;
        }
    }

}
