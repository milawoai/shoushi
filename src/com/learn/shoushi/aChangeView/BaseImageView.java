package com.learn.shoushi.aChangeView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by a0153-00401 on 15/11/5.
 */
public class BaseImageView extends ImageView {

    public static String tag = "BaseImageView";
    public BaseImageView(Context context)
    {
        super(context);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    SizeChangeListener l;
    DrawListener d;

    public void setSizeChangeListener(SizeChangeListener orlExt) {
        l = orlExt;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub

        Log.i(tag, "onSizeChanged");
        if(l != null) {
            l.sizeChanged(w, h, oldw, oldh);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.i(tag, "OnDraw");
        super.onDraw(canvas);
    }


    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        Log.d("mDebug", "onFinishInflate");
    }


    public interface SizeChangeListener {
        public void sizeChanged(int w, int h, int oldw, int oldh);
    }

    public interface DrawListener {
        public void DrawListener(int w, int h, int oldw, int oldh);
    }
}
