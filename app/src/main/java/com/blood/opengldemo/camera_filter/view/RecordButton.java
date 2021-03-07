package com.blood.opengldemo.camera_filter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

import com.blood.opengldemo.R;

public class RecordButton extends AppCompatTextView {

    private OnRecordListener mListener;

    public RecordButton(Context context) {
        super(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mListener.onRecordStart();
                setBackgroundResource(R.drawable.record_button_background2);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mListener.onRecordStop();
                setBackgroundResource(R.drawable.record_button_background);
                break;
        }
        return true;
    }

    public void setOnRecordListener(OnRecordListener listener) {
        mListener = listener;
    }

    public interface OnRecordListener {

        void onRecordStart();

        void onRecordStop();
    }
}
