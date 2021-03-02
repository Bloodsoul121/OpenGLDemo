package com.blood.opengldemo.camera_filter;

import android.content.Context;
import android.opengl.GLES20;

import com.blood.opengldemo.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;

public class CameraFilter {

    // 顶点坐标
    float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };

    // 纹理坐标
    float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    private float[] mtx;
    private int mWidth;
    private int mHeight;
    private final FloatBuffer mVertexBuffer;
    private final FloatBuffer mTextureBuffer;
    private final int mProgram;
    private final int mVPosition;
    private final int mVCoord;
    private final int mVTexture;
    private final int mVMatrix;

    public CameraFilter(Context context) {
        // 申请gpu内存空间，顶点
        mVertexBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.clear();
        mVertexBuffer.put(VERTEX);

        // 申请gpu内存空间，纹理
        mTextureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureBuffer.clear();
        mTextureBuffer.put(TEXTURE);

        // 读取顶点程序和片元程序，本地文件
        String vertexShader = OpenGLUtil.readRawTextFile(context, R.raw.camera_vert);
        String fragShader = OpenGLUtil.readRawTextFile(context, R.raw.camera_frag);

        mProgram = OpenGLUtil.loadProgram(vertexShader, fragShader);

        //顶点数组
        mVPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //接收纹理坐标，接收采样器采样图片的坐标
        mVCoord = GLES20.glGetAttribLocation(mProgram, "vCoord");
        //采样点的坐标
        mVTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
        //变换矩阵， 需要将原本的vCoord（01,11,00,10） 与矩阵相乘
        mVMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
    }

    public void onSizeChanged(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

    // 开始渲染
    public void onDraw(int texture) {
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glUseProgram(mProgram);

        // 从索引位0的地方读
        mVertexBuffer.position(0);
        // 赋值
        // index 指定要修改的通用顶点属性的索引
        // size  指定每个通用顶点属性的组件数
        // type  指定数组中每个组件的数据类型
        //       接受符号常量GL_FLOAT  GL_BYTE，GL_UNSIGNED_BYTE，GL_SHORT，GL_UNSIGNED_SHORT或GL_FIXED。
        //       初始值为GL_FLOAT。
        // normalized 指定在访问定点数据值时是应将其标准化（GL_TRUE）还是直接转换为定点值（GL_FALSE）。
        GLES20.glVertexAttribPointer(mVPosition, 2, GL_FLOAT, false, 0, mVertexBuffer);
        // 生效
        GLES20.glEnableVertexAttribArray(mVPosition);

        // 纹理
        mTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(mVCoord, 2, GL_FLOAT, false, 0, mTextureBuffer);
        GLES20.glEnableVertexAttribArray(mVCoord);

        GLES20.glActiveTexture(GL_TEXTURE0);

        //生成一个采样
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        // 第0个图层
        GLES20.glUniform1i(mVTexture, 0);

        GLES20.glUniformMatrix4fv(mVMatrix, 1, false, mtx, 0);

        //通知画画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
