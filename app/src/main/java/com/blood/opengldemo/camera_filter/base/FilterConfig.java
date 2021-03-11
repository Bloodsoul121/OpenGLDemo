package com.blood.opengldemo.camera_filter.base;

import android.content.Context;

import com.blood.opengldemo.camera_filter.filter.BaseFilter;
import com.blood.opengldemo.camera_filter.filter.BeautyFilter;
import com.blood.opengldemo.camera_filter.filter.CameraAdaptFilter;
import com.blood.opengldemo.camera_filter.filter.CameraFilter;
import com.blood.opengldemo.camera_filter.filter.ScreenFilter;
import com.blood.opengldemo.camera_filter.filter.SoulFilter;
import com.blood.opengldemo.camera_filter.filter.Split2Filter;
import com.blood.opengldemo.camera_filter.filter.WarmFilter;

public class FilterConfig {

    public static final int FILTER_CAMERA = 0;//滤镜
    public static final int FILTER_CAMERA_ADAPT = 1;//滤镜
    public static final int FILTER_WARM = 2;//暖色滤镜
    public static final int FILTER_SPLIT2 = 3;//分屏2个
    public static final int FILTER_SOUL = 4;//灵魂出窍
    public static final int FILTER_SCREEN = 5;//将数据渲染到屏幕
    public static final int FILTER_BEAUTY = 6;//美颜

    private final Context mContext;
    private final int mId;
    private BaseFilter mFilter;
    private int mWidth;
    private int mHeight;

    public FilterConfig(Context context, int id, boolean isOpen) {
        mContext = context;
        mId = id;
        if (isOpen) mFilter = createFilter();
    }

    public void onSizeChanged(int width, int height) {
        mWidth = width;
        mHeight = height;
        if (mFilter != null) {
            mFilter.onSizeChanged(width, height);
        }
    }

    public int onDraw(int texture) {
        if (mFilter != null) {
            texture = mFilter.onDraw(texture);
        }
        return texture;
    }

    public int getId() {
        return mId;
    }

    public BaseFilter getFilter() {
        return mFilter;
    }

    public void toggle(boolean isOpen) {
        if (isOpen) {
            mFilter = createFilter();
            mFilter.onSizeChanged(mWidth, mHeight);
        } else {
            mFilter.release();
            mFilter = null;
        }
    }

    private BaseFilter createFilter() {
        BaseFilter filter;
        switch (mId) {
            case FILTER_CAMERA:
                filter = new CameraFilter(mContext);
                break;
            case FILTER_CAMERA_ADAPT:
                filter = new CameraAdaptFilter(mContext);
                break;
            case FILTER_WARM:
                filter = new WarmFilter(mContext);
                break;
            case FILTER_SPLIT2:
                filter = new Split2Filter(mContext);
                break;
            case FILTER_SOUL:
                filter = new SoulFilter(mContext);
                break;
            case FILTER_SCREEN:
                filter = new ScreenFilter(mContext);
                break;
            case FILTER_BEAUTY:
                filter = new BeautyFilter(mContext);
                break;
            default:
                filter = null;
                break;
        }
        return filter;
    }

}
