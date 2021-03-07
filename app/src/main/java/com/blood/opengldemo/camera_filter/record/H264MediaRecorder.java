package com.blood.opengldemo.camera_filter.record;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import com.blood.opengldemo.camera_filter.base.FileUtil;
import com.blood.opengldemo.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class H264MediaRecorder {

    private static final String TAG = "H264MediaRecorder";

    private Context mContext;
    private int mWidth;
    private int mHeight;
    private EGLContext mGlContext;
    private MediaCodec mMediaCodec;
    private Surface mSurface;
    private Handler mHandler;
    private boolean mIsStart;
    private EGLEnv mEglEnv;
    private final File mFilterCodecTxt;
    private final File mFilterCodecH264;

    public H264MediaRecorder(Context context, String path, EGLContext glContext, int width, int height) {
        mContext = context.getApplicationContext();
        mWidth = width;
        mHeight = height;
        mGlContext = glContext;

        mFilterCodecTxt = new File(mContext.getExternalCacheDir(), "filter_codec.txt");
        mFilterCodecH264 = new File(mContext.getExternalCacheDir(), "filter_codec.h264");
        checkFileExist(mFilterCodecTxt);
        checkFileExist(mFilterCodecH264);
    }

    private void checkFileExist(File file) {
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                ToastUtil.toast("删除本地文件：" + file.getAbsolutePath());
            }
        }
    }

    public void start(float speed) {
        initMediaCodec();
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
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int index = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10_000);
        Log.i(TAG, "run: " + index);
        if (index >= 0) {
            ByteBuffer buffer = mMediaCodec.getOutputBuffer(index);
            MediaFormat mediaFormat = mMediaCodec.getOutputFormat(index);
            Log.i(TAG, "mediaFormat: " + mediaFormat.toString());
            byte[] outData = new byte[bufferInfo.size];
            buffer.get(outData);
            FileUtil.writeContent(outData, mFilterCodecTxt);
            FileUtil.writeBytes(outData, mFilterCodecH264);
            mMediaCodec.releaseOutputBuffer(index, false);
        }
    }

}
