package com.learn.shoushi.appManager;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by a0153-00401 on 15/11/5.
 */
public class HelperApplication  extends Application {
    private static Handler mApplicationHandler = null;
    private static Application mContext;
    //用于保存毛玻璃图片。。。。悲剧，为了个毛玻璃，浪费多少内存
    private static Bitmap bitmap = null;

    public synchronized static Application getContext() {
        return mContext;
    }

    public static Handler getApplicationHandler(Runnable runnable) {
        if (mApplicationHandler == null) {
            Looper.prepare();
            mApplicationHandler = new Handler(Looper.getMainLooper());
            Looper.loop();
        }
        return mApplicationHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        if (mApplicationHandler == null) {
            mApplicationHandler = new Handler(Looper.getMainLooper());
        }
        // 初始化全局编译配置信息
        // 初始化appInfo
        AppInfo.initApp(this, AppInfo.isDebug());
    }



    private void initTalkInfo() {
//        TalkManager.INSTANCE.setContext(this);
//        TalkManager.INSTANCE.initAppInfo(this, Integer.valueOf(ServiceProvider.appId),
//                Integer.valueOf(AppConfig.getFromId()), AppInfo.versionName, 1);
//
//        TalkManager.INSTANCE.initUserInfo(this, UserInfo.getInstance().getUserRealName(),
//                UserInfo.getInstance().getCurrentUserId(),UserInfo.getInstance().getCurrentConcreteSecretKey()
//                ,UserInfo.getInstance().getUserHeadUrl());
//        ACTIONS.add(new ActivityNotifyAction());
//        ACTIONS.add(new AdjustmentNotifyAction());
//        ACTIONS.add(new BusinessNotifyAction());
//        ACTIONS.add(new SystemNotifyAction());
    }

    public static void setBitmap(Bitmap bt) {
        bitmap = bt;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void destoryBitmap() {
        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
