package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.blood.opengldemo.R;

public class CameraAdaptFilter extends BaseFboFilter {

    private final int vMatrix;
    private final int vTextureMatrix;
    private float[] mMatrix;
    private float[] mTextureMatrix;

    public CameraAdaptFilter(Context context) {
        super(context, R.raw.camera_adapt_vert, R.raw.camera_frag);
        //变换矩阵， 需要将原本的vCoord（01,11,00,10） 与矩阵相乘
        vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        vTextureMatrix = GLES20.glGetUniformLocation(mProgram, "vTextureMatrix");
    }

    public void setMatrix(float[] mtx) {
        mMatrix = mtx;
    }

    public void setTextureMatrix(float[] textureMatrix) {
        mTextureMatrix = textureMatrix;
    }

    @Override
    public void beforeDraw() {
        if (mMatrix != null) {
            GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMatrix, 0);
        }
        if (mTextureMatrix != null) {
            GLES20.glUniformMatrix4fv(vTextureMatrix, 1, false, mTextureMatrix, 0);
        }
    }
}
