package com.learn.shoushi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.learn.shoushi.Utils.Methods;
import com.learn.shoushi.appManager.ActivityStack;
import com.learn.shoushi.appManager.AppInfo;

/**
 * Created by a0153-00401 on 15/11/5.
 */
public class BaseActivity extends Activity {
    private static final String tag = "BaseActivity";

    // 查询当前app是否处于后台
    Runnable checkAppBackgroundTask = new Runnable() {
        @Override
        public void run() {
            Log.i(tag,"checkAppBackgroundTask run");
            if (!Methods.isAppOnForceground(BaseActivity.this)) {
                // 当前处于后台
                AppInfo.setAppInBackground(true);

            }
        }
    };

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);
        Log.v(tag, this.getClass().getName() + " onCreate()");
        // 插入activity队列
        ActivityStack.getInstance().add(this);
        // 初始化屏幕参数
        AppInfo.initScreenInfo(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(tag, this.getClass().getName() + " onNewIntent()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(tag, this.getClass().getName() + " onStart()");
        // 删除掉检查后台操作
        AppInfo.getUIHandler().removeCallbacks(checkAppBackgroundTask);
        if (AppInfo.isAppInBackground()) {
            // 把app处于后台复位
            AppInfo.setAppInBackground(false);
            Log.v(tag, this.getClass().getName() + " 从后台恢复");
            // 前后台切换调用函数
            // TODO
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    // 如果app刚从后台恢复
                    Log.d("wht", "BaseActivity在后台");
                   /* if (Methods.needGetLoginInfo()) {
                        // 同步用户信息
                        ServiceProvider.syncUserInfo(BaseActivity.this);
                    }*/
                    return null;
                }

            }.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(tag, this.getClass().getName() + " onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(tag, this.getClass().getName() + " onPause()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(tag, this.getClass().getName() + " onRestoreInstanceState()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(tag, this.getClass().getName() + " onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(tag, this.getClass().getName() + " onStop()");

        // 延迟3秒判断当前是否处于后台
        AppInfo.getUIHandler().postDelayed(checkAppBackgroundTask, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(tag, this.getClass().getName() + " onDestroy()");
        ActivityStack.getInstance().remove(this);
    }


}
