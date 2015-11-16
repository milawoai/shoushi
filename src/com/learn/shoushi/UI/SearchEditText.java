package com.learn.shoushi.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.learn.shoushi.R;

/**
 * Created by a0153-00401 on 15/11/15.
 */
public class SearchEditText extends EditText {

    /**
     * 监听接口
     * @author xiejing
     *
     */
    public interface Listener {
        void click();
    }


    private Listener listener;

    private BitmapDrawable bdDefault;// editText右边的image默认背景

    private BitmapDrawable bdHover;// editText右边的image按下时候的背景

    private int backgroudDeafult;

    private int backgroudHover;

    private BitmapDrawable leftIcon;// 左边放大镜image

    private boolean iconFlag = true;

    private boolean isShowLeftIcon = true;

    Context context;

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray params = context.obtainStyledAttributes(attrs,
                R.styleable.SearchEditText);
        backgroudDeafult = params.getResourceId(
                R.styleable.SearchEditText_img_background_default, R.drawable.edit_search_clear_icon);
        backgroudHover = params.getResourceId(
                R.styleable.SearchEditText_img_background_hover, R.drawable.edit_search_clear_icon);
        bdDefault = (BitmapDrawable) getResources().getDrawable(
                backgroudDeafult);
        Bitmap bmDefault = bdDefault.getBitmap();
        bdDefault.setBounds(0, 0, bmDefault.getWidth(), bmDefault.getHeight());
        bdHover = (BitmapDrawable) getResources().getDrawable(backgroudHover);
        Bitmap bmHover = bdHover.getBitmap();
        bdHover.setBounds(0, 0, bmHover.getWidth(), bmHover.getHeight());
        leftIcon = (BitmapDrawable) getResources().getDrawable(R.drawable.edit_search_icon);
        Bitmap leftIconBitmap = leftIcon.getBitmap();
        leftIcon.setBounds(0, 0, leftIconBitmap.getWidth(), leftIconBitmap.getHeight());

        this.setHintTextColor(getResources().getColor(R.color.common_edit_text_text_hint));
        params.recycle();
    }

    public void setLeftIcon(int resId) {
        leftIcon = (BitmapDrawable) getResources().getDrawable(resId);
        Bitmap leftIconBitmap = leftIcon.getBitmap();
        leftIcon.setBounds(0, 0, leftIconBitmap.getWidth(), leftIconBitmap.getHeight());
        this.setCompoundDrawables(leftIcon, null, null, null);
    }

    public void setIsShowLeftIcon(boolean flag) {
        isShowLeftIcon = flag;
    }

    public void showDeleteIcon(boolean flag) {
        if (flag) {
            this.setCompoundDrawables(leftIcon, null, bdDefault, null);
            this.setListener(new Listener() {

                public void click() {
                    setText("");
                    removeIcon();
                    iconFlag = false;
                }
            });
        } else {
            setListener(null);
            this.setCompoundDrawables(leftIcon, null, null, null);
        }
    }

    public void showIcon() {
        if(isShowLeftIcon) {
            this.setCompoundDrawables(leftIcon, null, bdDefault, null);
        }else {
            this.setCompoundDrawables(null, null, bdDefault, null);
        }

        /**
         * @author MS
         * @date 2012.11.16
         * @description ANDROID-4453第五处修改（共有六处）
         */
        this.setListener(new Listener() {

            public void click() {
                setText("");
                removeIcon();
//                hideSoftInput();
                iconFlag = false;
            }
        });
    }

    public void removeIcon() {
        if(isShowLeftIcon) {
            this.setCompoundDrawables(leftIcon, null, null, null);
        }else {
            this.setCompoundDrawables(null, null, null, null);
        }

        /**
         * @author MS
         * @date 2012.11.16
         * @description ANDROID-4453第六处修改（共有六处）
         */
        this.setListener(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setListener(Listener l) {
        listener = l;
    }

    /**
     * @author tianlei.gao
     * @describe 隐藏软键盘
     * @date 2013.1.14
     */
    public void hideSoftInput() {
        InputMethodManager im = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(this.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * @author tianlei.gao
         * @date 2013.1.8
         * @describe 解决点击文本框右侧清除按钮区域，无法获取焦点
         * */
        if (this.getCompoundDrawables()[2] == null && iconFlag) {
            return super.onTouchEvent(event);
        }

        // float x1 = event.getRawX();// 获得相对于屏幕中的位置
        // float y1 = event.getRawY();

        float x = event.getX();// 获得相对于父view的位置
        // float y = event.getY();

        // float x2 = this.getWidth() - 40;
        // kuo.yang 2012.11.08 修复bug(ANDROID-4407)，放大点击区域
        float x2 = this.getWidth() - 80;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iconFlag = true;
                if (x > x2 && x < this.getWidth()) {
                    if (listener != null)
                        listener.click();
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            case MotionEvent.ACTION_UP:
                iconFlag = true;
                if (x > x2 && x < this.getWidth()) {
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
        }
        return true;
    }




}
