package com.learn.shoushi.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.learn.shoushi.R;
import com.learn.shoushi.appManager.AppInfo;

import java.util.ArrayList;

/**
 * Created by a0153-00401 on 15/11/5.
 */
public class SettingManager {
    private static SettingManager instance;

    private Context appContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static  synchronized  SettingManager getInstance()
    {
        if(instance == null) {
            instance = new SettingManager();
        }
        return instance;
    }

    private SettingManager() {
        appContext = AppInfo.getAppContext();
        sharedPreferences = appContext.getSharedPreferences("setting", 0);
        editor = sharedPreferences.edit();
    }

    public boolean containsKey(String key) {
        return sharedPreferences.contains(key);
    }

    //创建快捷方式
    public void createDeskApp(boolean isFirst) {
        editor.putBoolean("isFirst", isFirst).commit();
    }

    public boolean getDeskApp() {
        return sharedPreferences.getBoolean("isFirst", false);
    }


    public boolean isFirstLaunch(){
        return sharedPreferences.getBoolean(appContext.getString(R.string.is_first_launch), true);
    }

    public void setIsFirstLaunch(boolean flag){
        editor.putBoolean(appContext.getString(R.string.is_first_launch), flag).commit();
    }


    // 设置登录状态
    public void setLoginState(boolean login) {
        editor.putBoolean(AppInfo.getAppContext().getString(R.string.login_state), login).commit();
    }

    // 获取登录状态，看是否已登录
    public boolean isLogin() {
        if(!containsKey(AppInfo.getAppContext().getString(R.string.login_state)))
        {
            return false;
        }
        return sharedPreferences.getBoolean(AppInfo.getAppContext().getString(R.string.login_state), false);
    }

    /**
     * 获取锁定时限
     */

    public long getLockTime() {
        return sharedPreferences.getLong(appContext.getString(R.string.lock_time), 0);
    }

    /**
     * 保存锁定时限
     */
    public void setLockTime(long time) {
        editor.putLong(AppInfo.getAppContext().getString(R.string.lock_time), time).commit();
    }

    /**
     * 获取屏幕锁定时限
     */

    public long getScreeLockTime() {
        return sharedPreferences.getLong(appContext.getString(R.string.lock_time), 0);
    }

    /**
     * 保存屏幕锁定时限
     */
    public void setScreenLockTime(long time) {
        editor.putLong(AppInfo.getAppContext().getString(R.string.lock_time), time).commit();
    }


}
