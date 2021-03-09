package com.blood.opengldemo.camera_filter.filter;

import android.content.Context;
import android.opengl.GLES20;

public abstract class BaseFboFilter extends BaseFilter {

    protected int[] mFrameBuffers;
    protected int[] mFrameTextures;

    public BaseFboFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);

        releaseFrame();

        // 初始化

        mFrameBuffers = new int[1];
        mFrameTextures = new int[1];

        // 实例化一个fbo
        GLES20.glGenFramebuffers(1, mFrameBuffers, 0);

        // 创建一个纹理图层
        GLES20.glGenTextures(1, mFrameTextures, 0);

        // 配置纹理
        for (int i = 0; i < mFrameTextures.length; i++) {
            // bind 之间是一个原子操作
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameTextures[i]);

            //放大过滤，模糊 or 锯齿
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            //缩小过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            // end 原子操作结束
        }

        // 又来绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameTextures[0]);

        /*
         * 指定一个二维的纹理图片
         * level
         *     指定细节级别，0级表示基本图像，n级则表示Mipmap缩小n级之后的图像（缩小2^n）
         * internalformat
         *     指定纹理内部格式，必须是下列符号常量之一：GL_ALPHA，GL_LUMINANCE，GL_LUMINANCE_ALPHA，GL_RGB，GL_RGBA。
         * width height
         *     指定纹理图像的宽高，所有实现都支持宽高至少为64 纹素的2D纹理图像和宽高至少为16 纹素的立方体贴图纹理图像 。
         * border
         *     指定边框的宽度。必须为0。
         * format
         *     指定纹理数据的格式。必须匹配internalformat。下面的符号值被接受：GL_ALPHA，GL_RGB，GL_RGBA，GL_LUMINANCE，和GL_LUMINANCE_ALPHA。
         * type
         *     指定纹理数据的数据类型。下面的符号值被接受：GL_UNSIGNED_BYTE，GL_UNSIGNED_SHORT_5_6_5，GL_UNSIGNED_SHORT_4_4_4_4，和GL_UNSIGNED_SHORT_5_5_5_1。
         * data
         *     指定一个指向内存中图像数据的指针。
         */
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_RGBA,
                width,
                height,
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                null
        );

        // 绑定fbo数据，这里只是一个原子操作的开始，与上面类似
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        // 真正发生绑定   fbo  和 纹理  (图层)
        GLES20.glFramebufferTexture2D(
                GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                mFrameTextures[0],
                0
        );

        // 下面两个都是解锁原子操作的
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void releaseFrame() {
        if (mFrameTextures != null) {
            GLES20.glDeleteTextures(1, mFrameTextures, 0);
            mFrameTextures = null;
        }
        if (mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
        }
    }

    @Override
    public int onDraw(int texture) {
        // 数据渲染到fbo中
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        super.onDraw(texture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);  //
        return mFrameTextures[0] ;
    }
}
