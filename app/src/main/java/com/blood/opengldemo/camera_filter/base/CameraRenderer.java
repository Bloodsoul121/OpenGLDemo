package com.blood.opengldemo.camera_filter.base;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.blood.opengldemo.camera_filter.filter.BaseFilter;
import com.blood.opengldemo.camera_filter.filter.CameraFilter;
import com.blood.opengldemo.camera_filter.filter.ScreenFilter;
import com.blood.opengldemo.camera_filter.filter.SoulFilter;
import com.blood.opengldemo.camera_filter.filter.WarmFilter;
import com.blood.opengldemo.camera_filter.record.H264MediaRecorder;
import com.blood.opengldemo.camera_filter.record.MediaRecorder;
import com.blood.opengldemo.camera_filter.view.CameraView;
import com.blood.opengldemo.util.LogUtil;
import com.blood.opengldemo.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 预览拉伸的问题，应该是本身图片撑不满全屏，然后渲染全屏后拉伸
 */
public class CameraRenderer implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private static final String SAVE_FILE_NAME = "filter_record.mp4";

    private final CameraXHelper mCameraXHelper;
    private final CameraView mCameraView;
    private final Context mContext;
    private int[] mTextures;
    private SurfaceTexture mCameraTexture;
    private final float[] mtx = new float[16];
    private MediaRecorder mMediaRecorder;
    private H264MediaRecorder mH264MediaRecorder;
    private final List<BaseFilter> mFilters = new ArrayList<>();
    private boolean mIsOutH264;
    private boolean mIsSoulFilterOpen;

    public CameraRenderer(CameraView cameraView) {
        mContext = cameraView.getContext();
        mCameraView = cameraView;
        mCameraXHelper = new CameraXHelper((LifecycleOwner) mContext, this);
        mCameraXHelper.openCamera();
    }

    /**
     * 创建GLSurfaceView时，系统调用一次该方法。使用此方法执行只需要执行一次的操作，例如设置OpenGL环境参数或初始化OpenGL图形对象。
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.log("onSurfaceCreated");

        mTextures = new int[1];

        //让 SurfaceTexture 与 Gpu 共享一个数据源  0-31
        //没有赋值，其实就是0，代表是最上面的那个图层
        mCameraTexture.attachToGLContext(mTextures[0]);

        //监听摄像头数据回调
        mCameraTexture.setOnFrameAvailableListener(this);

        initFilters();

        initMediaRecorder();
    }

    private void initFilters() {
        //滤镜
        CameraFilter cameraFilter = new CameraFilter(mContext);
        //暖色滤镜
        WarmFilter warmFilter = new WarmFilter(mContext);
        //灵魂出窍
        SoulFilter soulFilter = new SoulFilter(mContext);
        //将数据渲染到屏幕
        ScreenFilter screenFilter = new ScreenFilter(mContext);
        //过滤集合
        mFilters.clear();
        mFilters.add(cameraFilter);
        mFilters.add(warmFilter);
        mFilters.add(soulFilter);
        mFilters.add(screenFilter);
    }

    private void initMediaRecorder() {
        //录制每一帧数据
        File saveFile = new File(mContext.getExternalCacheDir(), SAVE_FILE_NAME);
        String savePath = saveFile.getAbsolutePath();
        if (saveFile.exists()) {
            boolean delete = saveFile.delete();
            if (delete) {
                ToastUtil.toast("删除原有文件 " + savePath);
            }
        }

        mMediaRecorder = new MediaRecorder(
                mContext,
                savePath,
                EGL14.eglGetCurrentContext(),
                480,
                640
        );

        mH264MediaRecorder = new H264MediaRecorder(
                mContext,
                savePath,
                EGL14.eglGetCurrentContext(),
                480,
                640
        );
    }

    /**
     * 当GLSurfaceView的发生变化时，系统调用此方法，这些变化包括GLSurfaceView的大小或设备屏幕方向的变化。
     * 例如：设备从纵向变为横向时，系统调用此方法。我们应该使用此方法来响应GLSurfaceView容器的改变。
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.log("onSurfaceChanged " + width + " " + height);

        //宽高
//        mCameraFilter.onSizeChanged(width, height);
//        mRecordFilter.onSizeChanged(width, height);

        for (BaseFilter filter : mFilters) {
            filter.onSizeChanged(width, height);
        }
    }

    /**
     * 系统在每次重画GLSurfaceView时调用这个方法。使用此方法作为绘制（和重新绘制）图形对象的主要执行方法。
     */
    @Override
    public void onDrawFrame(GL10 gl) {
//        LogUtil.log("onDrawFrame");

        //更新摄像头的数据
        mCameraTexture.updateTexImage();
        mCameraTexture.getTransformMatrix(mtx);

//        mCameraFilter.setTransformMatrix(mtx);
//
//        // 返回fbo所在的图层，还没显示到屏幕上
//        int texture = mCameraFilter.onDraw(mTextures[0]);
//
//        // 显示到屏幕上
//        texture = mRecordFilter.onDraw(texture);

        int texture = mTextures[0];
        for (BaseFilter filter : mFilters) {
            if (filter instanceof CameraFilter) {
                ((CameraFilter) filter).setTransformMatrix(mtx);
                texture = filter.onDraw(mTextures[0]);
            } else {
                if (!mIsSoulFilterOpen && filter instanceof SoulFilter) {
                    continue;
                }
                texture = filter.onDraw(texture);
            }
        }

        // 录制，还是fbo的图层，主动调用opengl方法，必须是在egl环境下，即glthread
        if (mIsOutH264) {
            if (mH264MediaRecorder != null) {
                mH264MediaRecorder.fireFrame(texture, mCameraTexture.getTimestamp());
            }
        } else {
            if (mMediaRecorder != null) {
                mMediaRecorder.fireFrame(texture, mCameraTexture.getTimestamp());
            }
        }
    }

    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        LogUtil.log("onUpdated");

        // 摄像头预览到的数据 在这里
        mCameraTexture = output.getSurfaceTexture();

        /*
         * 应用程序会先创建一个SurfaceTexture，然后将SurfaceTexture传递给图形生产者对象（比如Camera，通过调用setPreviewTexture传递），
         * 图形生产者对象生产一帧数据后，会回调onFrameAvailable通知应用程序有新的图像数据可以使用，
         * 应用程序就可以调用updateTexImage将图像数据先送到Texture，之后就可以调用opengl接口做些具体的业务了。
         *
         * 这么看来，SurfaceTexture相当于数据源与接受层之间的一个桥梁，负责传递数据
         * 传递流程：Camera -> SurfaceTexture -> SurfaceView、GLSurfaceView、TextureView -> surfaceFlinger显示
         */
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // 一帧回调时，手动刷新
        mCameraView.requestRender();
    }

    public void startRecord(float speed) {
        if (mIsOutH264) {
            mH264MediaRecorder.start(speed);
        } else {
            mMediaRecorder.start(speed);
        }
    }

    public void stopRecord() {
        if (mIsOutH264) {
            mH264MediaRecorder.stop();
        } else {
            mMediaRecorder.stop();
        }
    }

    public void switchOutH264(boolean isOutH264) {
        mIsOutH264 = isOutH264;
    }

    public void toggleSoulFilter() {
        mIsSoulFilterOpen = !mIsSoulFilterOpen;
    }
}
