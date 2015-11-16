package com.learn.shoushi.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.learn.shoushi.appManager.AppInfo;
import com.learn.shoushi.appManager.HelperApplication;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by a0153-00401 on 15/11/5.
 */
public class Methods {


    public static boolean isAppOnForceground(Context context) {
        String pkgName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // Returns a list of application processes that are running on the device
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // importance:
            // The relative importance level that the system places
            // on this process.
            // May be one of IMPORTANCE_FOREGROUND, IMPORTANCE_VISIBLE,
            // IMPORTANCE_SERVICE, IMPORTANCE_BACKGROUND, or IMPORTANCE_EMPTY.
            // These constants are numbered so that "more important" values are
            // always smaller than "less important" values.
            // processName:
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(pkgName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    /**
     * 显示Toast
     *
     * @param text
     */
    public static void showToast(final CharSequence text) {
        showToast(text, false, true);
    }

    /**
     * @param text
     * @param lengthLong 是否较长时间显示
     */
    public static void showToast(final CharSequence text, final boolean lengthLong) {
        if (text != null) {
            showToast(text, lengthLong, true);
        }
    }

    public static void showToast(final CharSequence text, final boolean lengthLong, boolean show) {
        if (show == false) {
            return;
        }
        Runnable update = new Runnable() {
            public void run() {
                AppInfo.getGlobalToast().setText(text);
                AppInfo.getGlobalToast().setDuration(lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                AppInfo.getGlobalToast().show();
            }
        };
        AppInfo.getUIHandler().post(update);
    }

    /**
     * 直接应用资源Id显示Toast
     *
     * @param resId
     */
    public static void showToast(final int resId) {
        showToast(resId, false, true);
    }

    public static void showToast(int resId, boolean lengthLong) {
        String text = AppInfo.getAppContext().getString(resId);
        if (TextUtils.isEmpty(text))
            showToast(text, lengthLong);
    }

    /**
     * @param resId
     * @param lengthLong
     * @param show
     */

    public static void showToast(final int resId, final boolean lengthLong, boolean show) {
        if (show == false) {
            return;
        }
        Runnable update = new Runnable() {
            public void run() {
                AppInfo.getGlobalToast().setText(resId);
                AppInfo.getGlobalToast().setDuration(lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                AppInfo.getGlobalToast().show();
            }
        };
        AppInfo.getUIHandler().post(update);
    }

    public static int computePixelsWithDensity(int dp) {

        DisplayMetrics displayMetrics = HelperApplication.getContext().getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density + 0.5);
    }

    /**
     * 密度转换像素
     * @param dp
     * @param density
     * @return
     */
    public static int computePixelsWithDensity(int dp,float density){
        float scale;
        if(density!=0){
            scale = density;
        }else {
            scale = HelperApplication.getContext().getResources().getDisplayMetrics().density;
        }
        return (int)(dp*scale+0.5);
    }

    /**
     * 货币数量千分位分隔符添加
     *
     * @param count
     * @return
     */
    public static String currencyFormat(double count) {
        if (count >= 0 && count < 0.001)
            return "0.00";
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");
        return decimalFormat.format(count);
    }

    public static String fundNetFormat(double count) {
        if (count >= 0 && count < 0.01)
            return "0.0000";
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        return decimalFormat.format(count);
    }

    /*
     * double数字转换成0.00的形式
     */
    public static String doubleFormat(double count) {
        if (count >= 0 && count < 0.01)
            return "0.00";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(count);
    }

    /**
     * double转换成百分数形式
     *
     * @param count
     * @return
     */
    public static String convertToPercentage(double count) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);

        return format.format(count);
    }

    public static double getBiggerDouble(double num1, double num2) {
        BigDecimal decimal1 = new BigDecimal(num1);
        BigDecimal decimal2 = new BigDecimal(num2);
        int result = decimal1.compareTo(decimal2);

        if (result == -1) {
            return num2;
        } else {
            return num1;
        }
    }

    public static double getSmallerDouble(double num1, double num2) {
        BigDecimal decimal1 = new BigDecimal(num1);
        BigDecimal decimal2 = new BigDecimal(num2);
        int result = decimal1.compareTo(decimal2);

        if (result == -1) {
            return num1;
        } else {
            return num2;
        }
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideSoftInputMethods(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     *
     * @param view
     */
    public static void showSoftInputMethods(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 获取屏幕密度
     * @param activity
     * @return
     */
    public static float getDensity(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }


}
