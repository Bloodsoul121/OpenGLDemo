package com.blood.opengldemo.camera_filter;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.blood.opengldemo.util.LogUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraFilterRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private final CameraXHelper mCameraXHelper;
    private final CameraView mCameraView;
    private final Context mContext;
    private int[] mTextures;
    private SurfaceTexture mCameraTexture;
    private CameraFilter mCameraFilter;
    private final float[] mtx = new float[16];

    public CameraFilterRender(CameraView cameraView) {
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

        //滤镜
        mCameraFilter = new CameraFilter(mContext);
    }

    /**
     * 当GLSurfaceView的发生变化时，系统调用此方法，这些变化包括GLSurfaceView的大小或设备屏幕方向的变化。
     * 例如：设备从纵向变为横向时，系统调用此方法。我们应该使用此方法来响应GLSurfaceView容器的改变。
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.log("onSurfaceChanged");

        //宽高
        mCameraFilter.onSizeChanged(width, height);
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
        mCameraFilter.setTransformMatrix(mtx);
        mCameraFilter.onDraw(mTextures[0]);
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
}
