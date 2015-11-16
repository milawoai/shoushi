package com.learn.shoushi.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.learn.shoushi.appManager.AppInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class AppMethods {
    public static int computePixelsWithDensity(int dp) {
        return (int) (dp * AppInfo.density + 0.5);
    }

    public static int computePixelsTextSize(int sp) {
        return (int) (sp * AppInfo.density + 0.5);
    }

    /**
     * 是否满足最小api限制
     */
    @SuppressWarnings("deprecation")
    public static boolean fitApiLevel() {
        try {
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
            return true;
           /* if (sdkVersion >= Constants.API_MIN_LEVEL) {
                return true;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return if api level >= level
     */
    @SuppressWarnings("deprecation")
    public static boolean fitApiLevel(int level) {
        try {
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
            if (sdkVersion >= level) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 当前网络是否可用
     * @param
     * @return
     */
    public static boolean isNetworkAvaiable() {
        ConnectivityManager manager = (ConnectivityManager) AppInfo.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
        if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
            if (!fitApiLevel(11)) {
                // 在一款2.3的手机上，明明wifi连接着，但getActiveNetworkInfo()返回空, 所以api小于11就不做判断
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查当前是否是wifi网络
     * @param context
     * @return
     */
    public static boolean checkIsWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是UI线程
     * @return
     */
    public static boolean isUIThread() {
        return AppInfo.getUIThread() == Thread.currentThread();
    }

    /**
     * 拷贝文件
     *
     * @param fromFilePath
     * @param toFilePath
     * @param deleteExist
     * @return
     */
    public static boolean copyFile(String fromFilePath, String toFilePath,
                                   boolean deleteExist) {
        File toFile = new File(toFilePath);
        if (toFile.exists()) {
            if (deleteExist) {
                toFile.delete();
            } else {
                return true;
            }
        }
        try {
            File fromFile = new File(fromFilePath);
            if (!fromFile.exists()) {
                Log.e("ImageLoaderUtils.TAG",
                        "saveImageCacheTo failed, fromFile don't exist, fromFilePath:"
                                + fromFilePath);
                return false;
            }
            if (!fromFile.canRead()) {
                Log.e("ImageLoaderUtils.TAG",
                        "saveImageCacheTo failed, fromFile don't read, fromFilePath:"
                                + fromFilePath);
                return false;
            }

            java.io.FileInputStream fosfrom = new java.io.FileInputStream(
                    fromFilePath);
            java.io.FileOutputStream fosto = new java.io.FileOutputStream(
                    toFilePath);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); // 将内容写到新文件当中
            }
            // 关闭数据流
            fosfrom.close();
            fosto.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将input流转为byte数组，自动关闭
     *
     * @param input
     * @return
     */
    public static byte[] toByteArray(InputStream input) {
        if (input == null) {
            return null;
        }
        ByteArrayOutputStream output = null;
        byte[] result = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 100];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            result = output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            closeQuietly(input);
            closeQuietly(output);
        }
        return result;
    }

    /**
     * 关闭InputStream
     */
    public static void closeQuietly(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 关闭InputStream
     */
    public static void closeQuietly(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (Exception e) {
        }
    }


    /*
  *
  * 判断当前应用是不是在前台
  */
    public static boolean isAppOnForceground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

           // Log.i("appProcess.processName",appProcess.processName);
            if (appProcess.processName.equals(Constant.RENREN_PKG_NAME)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    /**
     * 隐藏软键盘
     * @param view
     */
    public static void hideSoftInputMethods(View view) {
        InputMethodManager imm = (InputMethodManager) view
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    /**
     * 显示软键盘
     * @param view
     */
    public static void showSoftInputMethods(View view) {
        InputMethodManager imm = (InputMethodManager) view
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

}
