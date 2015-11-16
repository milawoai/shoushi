package com.learn.shoushi.aChangeView;

import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.learn.shoushi.R;

/**
 * Created by a0153-00401 on 15/11/13.
 */
public class FullScreenGuideView {
    private Activity mActivity;

    private RelativeLayout mGuideRootView;

    /**
     * added by bingbing.qin
     * 有时给的cover并不能完全盖住原图
     * 对于这种情况可以显示cover时把原图设为INVISIBLE
     * 当cover消失时再把原图设为VISIBLE
     * 当cover消失时有时需要弹键盘，修改shared_prefs的值
     * 所以需要一个回调接口处理业务
     */
    private View.OnClickListener mOnClickRootViewListener;

    private  ViewDismissListener mDismissListener;  //引导隐藏时的回调
    /**
     * 点击蒙层时是否自动消失
     * */
    private boolean isAutoDismiss = true;

    public FullScreenGuideView(Activity activity) {
        this.mActivity = activity;
        mGuideRootView = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.full_screen_guide_layout, null);
        mGuideRootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnClickRootViewListener != null)
                    mOnClickRootViewListener.onClick(v);
                if(isAutoDismiss) {
                    dismiss();
                }
            }
        });
    }

    public void setBackgroundColor(int backgroundColor){ //设置蒙层的背景颜色
        if(mGuideRootView != null)
            mGuideRootView.setBackgroundColor(backgroundColor);
    }
    public void setViewDismissListener(ViewDismissListener listener){
        this.mDismissListener = listener;
    }
    /**
     * 往蒙层上添加一张图片并设定位置
     * @param resId
     * @param gravity 使用{@code Gravity}, 如 Gravity.LEFT|Gravity.BOTTOM
     * @param leftMargin
     * @param topMargin
     * @param rightMargin
     * @param bottomMargin
     * @param onClickListener 可传空
     */
    public void addImageView(int resId, int gravity, int leftMargin,
                             int topMargin, int rightMargin, int bottomMargin, View.OnClickListener onClickListener) {
        ImageView guideImageView = new ImageView(mActivity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        switch (verticalGravity) {
            case Gravity.TOP:
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case Gravity.BOTTOM:
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case Gravity.CENTER_VERTICAL:
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
        }
        int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (horizontalGravity) {
            case Gravity.LEFT:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case Gravity.RIGHT:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case Gravity.CENTER_HORIZONTAL:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
        }
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        mGuideRootView.addView(guideImageView, params);
        guideImageView.setImageResource(resId);
        if(onClickListener != null) {
            guideImageView.setOnClickListener(onClickListener);
        }
    }

    /**
     * 往蒙层上添加一个透明view来设置点击事件
     * @param width view的宽，可以使用LayoutParams.MATCH_PARENT等参数
     * @param height view的高，可以使用LayoutParams.MATCH_PARENT等参数
     * @param gravity 使用{@code Gravity}, 如 Gravity.LEFT|Gravity.BOTTOM
     * @param leftMargin
     * @param topMargin
     * @param rightMargin
     * @param bottomMargin
     * @param onClickListener 不可传空，传空就没意义了
     */
    public void addEmptyView(int width, int height, int gravity, int leftMargin,
                             int topMargin, int rightMargin, int bottomMargin, View.OnClickListener onClickListener) {
        if(onClickListener == null) {
            return;
        }
        View emptyView = new View(mActivity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        switch (verticalGravity) {
            case Gravity.TOP:
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case Gravity.BOTTOM:
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case Gravity.CENTER_VERTICAL:
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
        }
        int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (horizontalGravity) {
            case Gravity.LEFT:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case Gravity.RIGHT:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case Gravity.CENTER_HORIZONTAL:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
        }
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        mGuideRootView.addView(emptyView, params);
        emptyView.setOnClickListener(onClickListener);
    }

    public void addView(View view, int gravity, int leftMargin,
                        int topMargin, int rightMargin, int bottomMargin, View.OnClickListener onClickListener) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        switch (verticalGravity) {
            case Gravity.TOP:
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case Gravity.BOTTOM:
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case Gravity.CENTER_VERTICAL:
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
        }
        int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (horizontalGravity) {
            case Gravity.LEFT:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case Gravity.RIGHT:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case Gravity.CENTER_HORIZONTAL:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
        }
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        mGuideRootView.addView(view, params);

        if(onClickListener != null) {
            view.setOnClickListener(onClickListener);
        }

    }

    public void addFullScreenImageView(int resId, View.OnClickListener onClickListener) {
        ImageView guideImageView = new ImageView(mActivity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mGuideRootView.addView(guideImageView, params);
        guideImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        guideImageView.setImageResource(resId);
        if(onClickListener != null) {
            guideImageView.setOnClickListener(onClickListener);
        }
    }

    public void setRootViewOnTouchListener(View.OnTouchListener onOnTouchListener) {
        mGuideRootView.setOnTouchListener(onOnTouchListener);
    }

    public void setOnClickRootViewListener(View.OnClickListener listener) {
        mOnClickRootViewListener = listener;
    }

    /**
     * 设置点击蒙层是否消失
     * @param isAutoDismiss
     */
    public void setAutoDismiss(boolean isAutoDismiss) {
        this.isAutoDismiss = isAutoDismiss;
    }

    public void show() {
        if(mActivity == null) {
            return;
        }
        View rootView = mActivity.getWindow().getDecorView().getRootView();
        if(rootView instanceof FrameLayout) {
            ((FrameLayout) rootView).addView(mGuideRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }else {
            Log.d("photoDebug", "!rootView instanceof FrameLayout");
        }
    }

    public void dismiss() {
        if(mActivity == null || mGuideRootView == null) {
            return;
        }
        ViewParent rootView = mGuideRootView.getParent();
        if(rootView != null && rootView instanceof ViewGroup) {
            ((ViewGroup)rootView).removeView(mGuideRootView);
        }else {
            Log.d("photoDebug", "!rootView instanceof ViewGroup");
        }
        mGuideRootView.setOnClickListener(null);
        mGuideRootView = null;
        if(mDismissListener != null)
            mDismissListener.viewDismiss();
    }

    public boolean isShowing() {
        if(mGuideRootView != null
                && mGuideRootView.getParent() != null
                && mGuideRootView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }
    public interface  ViewDismissListener{
        void viewDismiss();
    }
}
