package com.learn.shoushi.aChangeView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import com.learn.shoushi.Utils.Methods;
import com.learn.shoushi.appManager.AppInfo;

import java.text.DecimalFormat;

/**
 * Created by a0153-00401 on 15/11/10.
 */
public class CountTextView extends TextView {

    int duration = 1000;
    float number;

    private int showType = 0;
    public static final int PERCENT_TYPE = 1;
    public static final int PERCENT_TYPE_DECIMAL_2 = 4;
    public static final int CURRENCY_TYPE = 2;
    public static final int CURRENCY_TYPE_DECIMAL_4 = 3;

    public CountTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public CountTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public CountTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(context);
    }


    private void setTypeface(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DIN_Alternate_Bold.ttf");
        setTypeface(typeface);
    }

    public void showNumberWithAnimation(float number) {
        showNumberWithAnimation(number, null);
    }

    public void setCountText(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        float textSize = getAdjustFontSize(String.valueOf(text));
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        super.setText(text, BufferType.NORMAL);
    }
    public void setCountText(CharSequence text ,float textSize) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
//        this.setTextSize(textSize);
        super.setText(text, BufferType.NORMAL);
    }

    public void adjustFontSize(String text) {
        float textSize = getAdjustFontSize(text);
        this.setTextSize(textSize);
    }

    private float getAdjustFontSize(String text) {
        float textSize = getTextSize();
        if (TextUtils.isEmpty(text)) {
            return textSize;
        }
        int width = this.getMeasuredWidth();
        if (width <= 0) {
            this.measure(0, 0);
            width = this.getMeasuredWidth();
        }
        if (width <= 0) {
            width = AppInfo.screenWidthForPortrait - Methods.computePixelsWithDensity(20);
        }
        TextPaint paint = this.getPaint();
        while (paint.measureText(text) >= width) {
            textSize -= 2;
            paint.setTextSize(textSize);
        }
        return textSize; //Methods.px2sp(this.getContext(), textSize);
    }

    public void showNumberWithAnimation(float number, Animator.AnimatorListener listener) {
        //修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number", 0, number);
        objectAnimator.setDuration(duration);
        if (listener != null) {
            objectAnimator.addListener(listener);
        }
        //加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
        setText(getFormatText(number));
    }

    private String getFormatText(float number) {
        if (showType == PERCENT_TYPE) {
            return new DecimalFormat("####").format(number) + "%";
        } else if (showType == CURRENCY_TYPE) {
            return new DecimalFormat("#,###,###,##0.00").format(number);
        } else if (showType == CURRENCY_TYPE_DECIMAL_4) {
            return new DecimalFormat("#,###,###,##0.0000").format(number);
        } else if (showType == PERCENT_TYPE_DECIMAL_2) {
            return new DecimalFormat("###0.00").format(number) + "%";
        } else {
            return new DecimalFormat("####").format(number);
        }
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }
}
