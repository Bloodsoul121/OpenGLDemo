package com.blood.opengldemo.camera_filter;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.io.IOException;

public class MediaRecorder {

    private Context mContext;
    private String mPath;
    private int mWidth;
    private int mHeight;
    private EGLContext mGlContext;
    private float mSpeed;
    private MediaCodec mMediaCodec;
    private Surface mSurface;
    private MediaMuxer mMediaMuxer;
    private Handler mHandler;
    private boolean mIsStart;
    private EGLEnv mEglEnv;

    public MediaRecorder(Context context, String path, EGLContext glContext, int width, int height) {
        mContext = context.getApplicationContext();
        mPath = path;
        mWidth = width;
        mHeight = height;
        mGlContext = glContext;
    }

    public void start(float speed) {
        mSpeed = speed;
        initMediaCodec();
        initMediaMuxer();
        initOpenGlEnv();
    }

    private void initMediaCodec() {
        try {
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight);
            //颜色空间 从 surface当中获得
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            //码率
            format.setInteger(MediaFormat.KEY_BIT_RATE, 1500_000);
            //帧率
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 25);
            //关键帧间隔
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
            //创建编码器
            mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            //配置编码器
            mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            //输入数据     byte[]   gpu  类似 mediaprojection
            mSurface = mMediaCodec.createInputSurface();
            //开启编码
            mMediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMediaMuxer() {
        //混合器 (复用器) 将编码的h.264封装为mp4
        try {
            mMediaMuxer = new MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initOpenGlEnv() {
        //創建OpenGL 的 環境
        HandlerThread handlerThread = new HandlerThread("codec-gl");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(() -> {
            mEglEnv = new EGLEnv(mContext, mGlContext, mSurface, mWidth, mHeight);
            mIsStart = true;
        });
    }

    public void stop() {
        mIsStart = false;
        mHandler.post(() -> {
            codec(true);
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;
            mEglEnv.release();
            mEglEnv = null;
            mSurface = null;
            mHandler.getLooper().quitSafely();
            mHandler = null;
        });
    }

    public void fireFrame(int texture, long timestamp) {
        if (!mIsStart) {
            return;
        }
        mHandler.post(() -> {
            mEglEnv.draw(texture, timestamp);
            // 获取对应的数据
            codec(false);
        });
    }

    // 从MediaCodec里面取出已经解码的数据
    private void codec(boolean endOfStream) {

    }

}
