package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.blood.opengldemo.R;

public class CameraAdaptFilter extends BaseFboFilter {

    private final int vVertexMatrix;
    private final int vTextureMatrix;
    private float[] mVertexMatrix;
    private float[] mTextureMatrix;

    public CameraAdaptFilter(Context context) {
        super(context, R.raw.camera_adapt_vert, R.raw.camera_frag);
        //变换矩阵， 需要将原本的vCoord（01,11,00,10） 与矩阵相乘
        vVertexMatrix = GLES20.glGetUniformLocation(mProgram, "vVertexMatrix");
        vTextureMatrix = GLES20.glGetUniformLocation(mProgram, "vTextureMatrix");
    }

    public void setVertexMatrix(float[] vertexMatrix) {
        mVertexMatrix = vertexMatrix;
    }

    public void setTextureMatrix(float[] textureMatrix) {
        mTextureMatrix = textureMatrix;
    }

    @Override
    public void beforeDraw() {
        if (mVertexMatrix != null) {
            GLES20.glUniformMatrix4fv(vVertexMatrix, 1, false, mVertexMatrix, 0);
        }
        if (mTextureMatrix != null) {
            GLES20.glUniformMatrix4fv(vTextureMatrix, 1, false, mTextureMatrix, 0);
        }
    }
}
