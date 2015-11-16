package com.learn.shoushi.aChangeView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.learn.shoushi.R;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class EmptyErrorView extends LinearLayout {
    private Context mContext;
    private LinearLayout mContainer;
    public EmptyErrorView(Context context) {
        super(context);
        initView(context);
    }

    public EmptyErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EmptyErrorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.list_empty_view, null);
        addView(mContainer, lp);
        setGravity(Gravity.CENTER);
    }

}